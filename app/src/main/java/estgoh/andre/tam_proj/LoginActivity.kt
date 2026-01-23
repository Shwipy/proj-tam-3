package estgoh.andre.tam_proj

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import estgoh.andre.tam_proj.DataBase.APIResponses.ErrorResponse
import estgoh.andre.tam_proj.DataBase.APIResponses.LoginResponse
import estgoh.andre.tam_proj.DataBase.AppService
import estgoh.andre.tam_proj.DataBase.User
import estgoh.andre.tam_proj.DataBase.getRetrofit
import estgoh.andre.tam_proj.Stuff.renewToken
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var appService: AppService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        username = findViewById(R.id.tf_user_name)
        password = findViewById(R.id.tf_user_password)

        appService = getRetrofit().create(AppService::class.java)
    }

    fun onCreateClick(v: View){
        val name = username.getText().toString()
        val pass = password.getText().toString()


        if (name.trim().isEmpty() || pass.trim().isEmpty()) {
            showToast("Player name or password cannot be empty")
            return
        }
        val newUser = User(0, name, pass)

        lifecycleScope.launch{
            try {
                val response = appService.addUser(newUser)
                when (response.code()) {
                    200 -> {
                        val user = response.body()
                        if (user == null) {
                            showToast("Null object received")
                        }
                        else{
                            showToast("Utilizador criado com sucesso.")
                        }
                    }
                    400 -> showToast("Novo utilizador precisa de nome e password.")
                    else -> {

                        val body = response.errorBody()?.string()
                        val gson = com.google.gson.Gson()
                        val errorObj = gson.fromJson(body, ErrorResponse::class.java)

                        showToast("Response: ${errorObj.error}")
                    }
                }
            }
            catch (e: Exception){
                showToast("Exception: ${e.message}")
            }
        }
    }

    fun onLoginClick(v: View){
        val name = username.getText().toString()
        val pass = password.getText().toString()
        val context = this


        if (name.trim().isEmpty() || pass.trim().isEmpty()) {
            showToast("Dados de Login em falta.")
            return
        }
        val userData = User(0, name, pass)

        lifecycleScope.launch{
            try {
                val response = appService.loginUser(userData)
                val body = response.body()
                when (response.code()) {
                    200 -> {
                        if (body == null) {
                            showToast("Null object received")
                        }
                        else{
                            val token: LoginResponse = body

                            val sharedPreferences = getSharedPreferences("UserPref", MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("token", token.token)
                            editor.apply()

                            renewToken(context)
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                            finish()

                        }
                    }
                    400 -> showToast("Dados de Login em falta.")
                    401 -> showToast("Password incorreta.")
                    404 -> showToast("User não existe.")
                    else -> {
                        val body = response.errorBody()?.string()
                        val gson = com.google.gson.Gson()
                        val errorObj = gson.fromJson(body, ErrorResponse::class.java)

                        showToast("Response: ${errorObj.error}")
                    }
                }
            }
            catch (e: Exception){
                showToast("Exception: ${e.message}")
            }
        }

    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}