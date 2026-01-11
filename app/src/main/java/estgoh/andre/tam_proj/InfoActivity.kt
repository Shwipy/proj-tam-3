package estgoh.andre.tam_proj

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
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
import kotlinx.coroutines.launch

class InfoActivity : AppCompatActivity() {

    lateinit var username: TextView
    lateinit var appService: AppService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_info)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btn_leave_info = findViewById<ImageButton>(R.id.btn_leave_info)
        btn_leave_info.setOnClickListener{
            this.finish()
        }

        username = findViewById(R.id.tf_username)
        appService = getRetrofit().create(AppService::class.java)

        lifecycleScope.launch {
            try {
                val sharedPreferences = getSharedPreferences("UserPref", MODE_PRIVATE)
                val token = sharedPreferences.getString("token","")

                val response = appService.getUser("Bearer $token")
                val body = response.body()

                when (response.code()) {
                    200 -> {
                        if (body == null) {
                            showToast("Null object received")
                        } else {
                            val name: User = body
                            username.text = name.name
                        }
                    }

                    400 -> showToast("Response code 400: bad request.")
                    else -> showToast("Response code: ${response.code()}")
                }
            } catch (e: Exception) {
                showToast("Exception: ${e.message}")
            }
        }
    }

    fun onLogoutClick(v: View){

        val sharedPreferences = getSharedPreferences("UserPref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("token")
        editor.apply()

//      https://stackoverflow.com/a/20421207
        val intent = Intent(this, LoginActivity::class.java)
        intent.setFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        startActivity(intent)
        this.finish()
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}