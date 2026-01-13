package estgoh.andre.tam_proj

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import estgoh.andre.tam_proj.DataBase.APIResponses.ErrorResponse
import estgoh.andre.tam_proj.DataBase.AppService
import estgoh.andre.tam_proj.DataBase.Quiz
import estgoh.andre.tam_proj.DataBase.getRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


//import estgoh.andre.tam_proj.Stuff.Daos

class EditQuizActivity : AppCompatActivity() {
    lateinit var title: EditText
    lateinit var description: EditText
    lateinit var duration: EditText

    lateinit var appService: AppService

//    fun showDialog(quizId: Long){
//        val builder = AlertDialog.Builder(this)
//        builder.setMessage("Pretende mesmo apagar este quiz?")
//            .setPositiveButton("Yes") { dialog, id ->
//                Daos(this).quiz.delete(quizId)
//                this.setResult(Activity.RESULT_OK,Intent())
//                finish()
//            }
//            .setNegativeButton("No") { dialog, id ->
//            }
//        builder.create().show()
//    }

    //https://stackoverflow.com/questions/4297763/disabling-of-edittext-in-android
    fun disableEditText(editText: EditText) {
        editText.setFocusable(false)
        editText.setEnabled(false)
        editText.setCursorVisible(false)
        editText.setKeyListener(null)
        editText.setBackgroundColor(Color.TRANSPARENT)

        val default_text_color = ContextCompat.getColor(this, R.color.black)
        editText.setTextColor(ColorStateList.valueOf(default_text_color))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_quiz)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        title = findViewById(R.id.tf_edit_title)
        description = findViewById(R.id.tf_edit_description)
        duration = findViewById(R.id.tf_edit_time)
        val btn_delete_quiz = findViewById<ImageButton>(R.id.btn_delete_quiz)
        val btn_confirm_edit_quiz = findViewById<Button>(R.id.btn_confirm_edit_quiz)

        appService = getRetrofit().create(AppService::class.java)

        val quizId = intent.getLongExtra("id", 0)
        val owned = intent.getBooleanExtra("owned", false)

        if(!owned) {
            disableEditText(title)
            disableEditText(description)
            disableEditText(duration)
            btn_delete_quiz.visibility = View.GONE
            btn_confirm_edit_quiz.visibility = View.GONE
        }

        val context = this
        lifecycleScope.launch{

            try {
                val sharedPreferences = getSharedPreferences("UserPref", MODE_PRIVATE)
                val token = sharedPreferences.getString("token","")

                val response = appService.getQuiz("Bearer $token", quizId)
                val body: Quiz? = response.body()
                when (response.code()) {
                    200 -> if (body != null) {
                        val quiz: Quiz = body
                        withContext(Dispatchers.Main) {

                            title.setText(quiz.title)
                            description.setText(quiz.description)
                            duration.setText(quiz.duration.toString())
                        }

                    } else {
                        showToast("Null object received")
                    }
                    else -> showToast("Response code: ${response.code()}")
                }
            }
            catch (e: Exception){
                showToast("Exception: ${e.message}")
            }
        }

        val btn_leave_edit_quiz = findViewById<ImageButton>(R.id.btn_leave_edit_quiz)
        btn_leave_edit_quiz.setOnClickListener{
            this.setResult(Activity.RESULT_OK,Intent())
            this.finish()
        }

        val btn_view_questions = findViewById<Button>(R.id.btn_view_questions)
        btn_view_questions.setOnClickListener {
            val intent = Intent(this, ViewQuestionActivity::class.java)
            intent.putExtra("id",quizId)
            intent.putExtra("owned",owned)
            this.startActivity(intent)
        }
    }
    fun onUpdateClick(v: View){
        val context = this
        val quizId = intent.getLongExtra("id", 0)
        val title = title.text.toString()
        val description = description.text.toString()
        val duration = duration.text.toString().toIntOrNull()

        if( title.trim().isEmpty() || description.trim().isEmpty() || duration == null || duration <= 0){
            showToast("Campos Inválidos")
            return
            }

        val updatedQuiz = Quiz(id = quizId,title = title, description = description, duration = duration!!)

        lifecycleScope.launch{
            try {
                val sharedPreferences = getSharedPreferences("UserPref", MODE_PRIVATE)
                val token = sharedPreferences.getString("token","")

                val response = appService.updateQuiz("Bearer $token", quizId, updatedQuiz)

                when (response.code()) {
                    200 -> {
                        showToast("Quiz editado com sucesso.")
                    }
                    400 -> showToast("Response code 400: bad request.")
                    401 -> showToast("Não pode editar Quizes de outro User.")
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

    fun onDeleteClick(v: View){
        val context = this
        val quizId = intent.getLongExtra("id", 0)

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Pretende mesmo apagar este quiz?")
            .setPositiveButton("Yes") { dialog, id ->
                lifecycleScope.launch{
                    try {
                        val sharedPreferences = getSharedPreferences("UserPref", MODE_PRIVATE)
                        val token = sharedPreferences.getString("token","")

                        val response = appService.deleteQuiz("Bearer $token", quizId)
                        val body = response.body()

                        when (response.code()) {
                            200 -> {
                                showToast("Quiz apagado com sucesso.")
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                                finish()
                            }
                            400 -> showToast("Response code 400: bad request.")
                            401 -> showToast("Não pode apagar Quizes de outro User.")
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
            .setNegativeButton("No") { dialog, id ->
            }
        builder.create().show()
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}