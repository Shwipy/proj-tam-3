package estgoh.andre.tam_proj.Stuff

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import android.widget.Toast
import estgoh.andre.tam_proj.DataBase.APIResponses.LoginResponse
import estgoh.andre.tam_proj.DataBase.AppService
import estgoh.andre.tam_proj.DataBase.getRetrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun renewToken(context: Context){
    CoroutineScope(Dispatchers.Default).launch{
        var run = true
        while (run){

            val sharedPreferences = context.getSharedPreferences("UserPref", MODE_PRIVATE)
            val savedToken = sharedPreferences.getString("token", "")
            Log.d("TOKEN2", savedToken.toString())
            if (savedToken.isNullOrEmpty()){
                run = false
            }

            val appService = getRetrofit().create(AppService::class.java)

            try {
                val response = appService.tokenRenew("Bearer $savedToken")
                val body = response.body()
                when (response.code()) {
                    200 -> {
                        val token: LoginResponse? = body
                        val editor = sharedPreferences.edit()
                        editor.putString("token", token?.token)
                        editor.apply()
                        Log.d("TOKEN", token?.token.toString())
                    }
                }
            }
            catch (e: Exception){
                Toast.makeText(context, "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
            }

            delay(5000)
        }
    }

}