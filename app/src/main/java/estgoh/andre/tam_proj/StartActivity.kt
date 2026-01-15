package estgoh.andre.tam_proj

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import estgoh.andre.tam_proj.DataBase.APIResponses.LoginResponse
import estgoh.andre.tam_proj.DataBase.AppService
import estgoh.andre.tam_proj.DataBase.getRetrofit
import estgoh.andre.tam_proj.Stuff.renewToken
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_start)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }


        val sharedPreferences = getSharedPreferences("UserPref", MODE_PRIVATE)
        val savedToken = sharedPreferences.getString("token", "")
        val context = this
        lifecycleScope.launch {
            if(!savedToken.isNullOrEmpty() && checkToken()){
                renewToken(context)
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
                context.finish()

            }else{

                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
                context.finish()
            }
        }


    }

    suspend fun checkToken(): Boolean {
        val appService = getRetrofit().create(AppService::class.java)
        val sharedPreferences = getSharedPreferences("UserPref", MODE_PRIVATE)
        val savedToken = sharedPreferences.getString("token", "")

        try {
            val response = appService.tokenRenew("Bearer $savedToken")
            val body = response.body()
            when (response.code()) {
                200 -> {
//                    showToast("200")
                    return true
                }
                400->{
                    val editor = sharedPreferences.edit()
                    editor.remove("token")
                    editor.apply()
                    showToast("Token expirated")
                    return false
                }
            }
        } catch (e: Exception) {
            showToast("Exception: ${e.message}")
        }
        return false
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}