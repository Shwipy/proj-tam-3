package estgoh.andre.tam_proj

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.WorkDuration
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import estgoh.andre.tam_proj.DataBase.APIResponses.ErrorResponse
import estgoh.andre.tam_proj.DataBase.APIResponses.LoginResponse
import estgoh.andre.tam_proj.DataBase.APIResponses.OkResponse
import estgoh.andre.tam_proj.DataBase.AppService
import estgoh.andre.tam_proj.DataBase.Quiz
import estgoh.andre.tam_proj.DataBase.getRetrofit
import kotlinx.coroutines.launch

//import estgoh.andre.tam_proj.DataBase.Quiz.QuizRoom
//import estgoh.andre.tam_proj.Stuff.Daos

class AddQuizActivity : AppCompatActivity() {
    lateinit var appService: AppService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_quiz)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btn_cancel = findViewById<Button>(R.id.btn_cancel_quiz)
        btn_cancel.setOnClickListener{
            this.finish()
        }

        val btn_add = findViewById<Button>(R.id.btn_add_quiz)
        btn_add.setOnClickListener{
            val context = this

            val tf_title = findViewById<EditText>(R.id.tf_title)
            val tf_description = findViewById<EditText>(R.id.tf_description)
            val tf_time = findViewById<EditText>(R.id.tf_time)

            val title = tf_title.text.toString()
            val description = tf_description.text.toString()
            val duration = tf_time.text.toString().toIntOrNull()

            if( title.trim().isEmpty() || description.trim().isEmpty() || duration == null || duration <= 0){
                Toast.makeText(this,"Campos Inválidos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val quizData = Quiz(title = title, description = description, duration = duration)
            appService = getRetrofit().create(AppService::class.java)

            lifecycleScope.launch{
                try {
                    val sharedPreferences = getSharedPreferences("UserPref", MODE_PRIVATE)
                    val token = sharedPreferences.getString("token","")

                    val response = appService.addQuiz(quizData,"Bearer $token")
                    val body = response.body()

                    when (response.code()) {
                        200 -> {
                            if (body == null) {
                                showToast("Null object received")
                            }
                            else{
                                val token: OkResponse = body

                                showToast("Quiz adicionado com sucesso.")
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                                finish()

                            }
                        }
                        400 -> showToast("Response code 400: bad request.")
                        401 -> showToast("Password incorreta.")
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
    }
    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}