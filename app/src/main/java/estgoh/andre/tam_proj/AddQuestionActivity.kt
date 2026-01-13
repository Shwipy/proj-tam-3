package estgoh.andre.tam_proj

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import estgoh.andre.tam_proj.DataBase.APIResponses.ErrorResponse
import estgoh.andre.tam_proj.DataBase.APIResponses.OkResponse
import estgoh.andre.tam_proj.DataBase.AppService
import estgoh.andre.tam_proj.DataBase.Question
import estgoh.andre.tam_proj.DataBase.getRetrofit
import kotlinx.coroutines.launch

//import estgoh.andre.tam_proj.DataBase.Question.QuestionRoom
//import estgoh.andre.tam_proj.Stuff.Daos

class AddQuestionActivity : AppCompatActivity() {

    lateinit var appService: AppService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_question)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.new_quiz_sv)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val quizId = intent.getLongExtra("quizId", 0)
//        showToast("Quiz ID: ${quizId}")

        val tf_question = findViewById<EditText>(R.id.tf_question)

        var selected_answer_num = findViewById<RadioButton>(R.id.rb_two_questions)
        var selected_correct_answer = findViewById<RadioButton>(R.id.rb_one_correct)
        val group_num_answer = findViewById<RadioGroup>(R.id.group_num_answers)
        val group_correct_answer = findViewById<RadioGroup>(R.id.group_correct_answer)

        val tf_answer_one = findViewById<EditText>(R.id.tf_answer_one)
        val tf_answer_two = findViewById<EditText>(R.id.tf_answer_two)
        val tf_answer_three = findViewById<EditText>(R.id.tf_answer_three)
        val tf_answer_four = findViewById<EditText>(R.id.tf_answer_four)

        val label_answer3 = findViewById<TextView>(R.id.label_new_question_answer3)
        val label_answer4 = findViewById<TextView>(R.id.label_new_question_answer4)

        val rb_three_correct = findViewById<RadioButton>(R.id.rb_three_correct)
        val rb_four_correct = findViewById<RadioButton>(R.id.rb_four_correct)


        val tf_img_url = findViewById<EditText>(R.id.tf_img_url)

        group_num_answer.setOnCheckedChangeListener { group, checkedId ->
            selected_answer_num = findViewById<View?>(checkedId) as RadioButton

            val num_answers = selected_answer_num.text.toString().toInt()
            if(num_answers ==2 ){
                label_answer3.visibility = View.GONE
                tf_answer_three.visibility = View.GONE
                rb_three_correct.visibility = View.GONE
                label_answer4.visibility = View.GONE
                tf_answer_four.visibility = View.GONE
                rb_four_correct.visibility = View.GONE
                rb_three_correct.isChecked= false
                rb_four_correct.isChecked= false
                tf_answer_three.setText("")
                tf_answer_four.setText("")
            }
            if (num_answers >= 3){
                label_answer3.visibility = View.VISIBLE
                tf_answer_three.visibility = View.VISIBLE
                rb_three_correct.visibility = View.VISIBLE
                label_answer4.visibility = View.GONE
                tf_answer_four.visibility = View.GONE
                rb_four_correct.visibility = View.GONE
                rb_four_correct.isChecked= false
                tf_answer_four.setText("")
            }
            if (num_answers == 4){
                label_answer3.visibility = View.VISIBLE
                tf_answer_three.visibility = View.VISIBLE
                rb_three_correct.visibility = View.VISIBLE
                label_answer4.visibility = View.VISIBLE
                tf_answer_four.visibility = View.VISIBLE
                rb_four_correct.visibility = View.VISIBLE
            }
        }

        group_correct_answer.setOnCheckedChangeListener { group, checkedId ->
            selected_correct_answer = findViewById<View?>(checkedId) as RadioButton
        }

        var btn_cancel = findViewById<Button>(R.id.btn_cancel_question)
        btn_cancel.setOnClickListener {
            this.finish()
        }

        var btn_add = findViewById<Button>(R.id.btn_add_question)
        btn_add.setOnClickListener {
            val context = this

            val question = tf_question.text.toString()

            val answer_one = tf_answer_one.text.toString()
            val answer_two = tf_answer_two.text.toString()
            val answer_three = tf_answer_three.text.toString()
            val answer_four = tf_answer_four.text.toString()

            val num_answers = selected_answer_num.text.toString().toInt()
            val correct_answer = selected_correct_answer.text.toString().toIntOrNull()

            val img_url = tf_img_url.text.toString().ifEmpty {
                null
            }

            if(!question.trim().isEmpty()){

                if ( correct_answer == null || correct_answer > num_answers){
                    Toast.makeText(this,"Resposta Correta Inválida", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener

                } else if (num_answers == 2 && !answer_one.trim().isEmpty() && !answer_two.trim().isEmpty()){
                    val questionData = Question(
                        quiz_id = quizId,
                        question = question,
                        correct_answer = correct_answer,
                        answer1 = answer_one,
                        answer2 = answer_two,
                        img = img_url
                    )
                    postQuestion(questionData, context)

//                    this.setResult(Activity.RESULT_OK,Intent())
//                    this.finish()

                }else if (num_answers == 3 && !answer_one.trim().isEmpty() && !answer_two.trim().isEmpty() && !answer_three.trim().isEmpty()){

                    val questionData = Question(
                        quiz_id = quizId,
                        question = question,
                        correct_answer = correct_answer,
                        answer1 = answer_one,
                        answer2 = answer_two,
                        answer3 = answer_three,
                        img = img_url
                    )
                    postQuestion(questionData, context)

//                    this.setResult(Activity.RESULT_OK,Intent())
//                    this.finish()

                }else if (num_answers == 4 && !answer_one.trim().isEmpty() && !answer_two.trim().isEmpty() && !answer_three.trim().isEmpty() && !answer_four.trim().isEmpty()){

                    val questionData = Question(
                        quiz_id = quizId,
                        question = question,
                        correct_answer = correct_answer,
                        answer1 = answer_one,
                        answer2 = answer_two,
                        answer3 = answer_three,
                        answer4 = answer_four,
                        img = img_url
                    )
                    postQuestion(questionData, context)

//                    this.setResult(Activity.RESULT_OK,Intent())
//                    this.finish()

                }else{
                    Toast.makeText(this,"Resposta(s) não Introduzidas", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            } else{
                Toast.makeText(this,"Questão não foi Introduzida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

        }
    }

    fun postQuestion(questionData: Question, context: Context){
        appService = getRetrofit().create(AppService::class.java)

        lifecycleScope.launch{
            try {
                val sharedPreferences = getSharedPreferences("UserPref", MODE_PRIVATE)
                val token = sharedPreferences.getString("token","")

                val response = appService.addQuestion(questionData,"Bearer $token")
                val body = response.body()

                when (response.code()) {
                    200 -> {
                        showToast("Question adicionada com sucesso.")
                        finish()
                    }
                    400 -> showToast("Response code 400: bad request.")
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