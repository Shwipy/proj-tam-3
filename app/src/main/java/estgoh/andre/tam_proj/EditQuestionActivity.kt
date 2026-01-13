package estgoh.andre.tam_proj

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
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
import estgoh.andre.tam_proj.DataBase.AppService
import estgoh.andre.tam_proj.DataBase.Question
import estgoh.andre.tam_proj.DataBase.Quiz
import estgoh.andre.tam_proj.DataBase.getRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.text.compareTo

//import estgoh.andre.tam_proj.DataBase.Question.QuestionRoom
//import estgoh.andre.tam_proj.Stuff.Daos


class EditQuestionActivity : AppCompatActivity() {
    lateinit var question: EditText
    lateinit var answer_one: EditText
    lateinit var answer_two: EditText
    lateinit var answer_three: EditText
    lateinit var answer_four: EditText
    lateinit var img: EditText

    lateinit var appService: AppService

    var correctAnswer: Int? = null
    var numberofAnswers: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_question)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.new_quiz_sv)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val questId = intent.getLongExtra("questId", 0)

        question = findViewById<EditText>(R.id.tf_edit_question)
        answer_one = findViewById<EditText>(R.id.tf_edit_answer_one)
        answer_two = findViewById<EditText>(R.id.tf_edit_answer_two)
        answer_three = findViewById<EditText>(R.id.tf_edit_answer_three)
        answer_four = findViewById<EditText>(R.id.tf_edit_answer_four)
        img = findViewById<EditText>(R.id.tf_edit_img_url)

        val group_num_answer = findViewById<RadioGroup>(R.id.group_edit_num_answers)
        val group_correct_answer = findViewById<RadioGroup>(R.id.group_edit_correct_answer)

        val label_answer3 = findViewById<TextView>(R.id.label_edit_question_answer3)
        val label_answer4 = findViewById<TextView>(R.id.label_edit_question_answer4)

        val rb_three_correct = findViewById<RadioButton>(R.id.rb_edit_three_correct)
        val rb_four_correct = findViewById<RadioButton>(R.id.rb_edit_four_correct)



        appService = getRetrofit().create(AppService::class.java)

        lifecycleScope.launch{

            try {
                val sharedPreferences = getSharedPreferences("UserPref", MODE_PRIVATE)
                val token = sharedPreferences.getString("token","")

                val response = appService.getQuestion("Bearer $token", questId)
                val body: Question? = response.body()
                when (response.code()) {
                    200 -> if (body != null) {
                        val questionData: Question = body
                        withContext(Dispatchers.Main) {

                            question.setText(questionData.question)
                            question.setText(questionData.question)
                            img.setText(questionData.img)

                            answer_one.setText(questionData.answer1)
                            answer_two.setText(questionData.answer2)
                            answer_three.setText(questionData.answer3)
                            answer_four.setText(questionData.answer4)

                            numberofAnswers = getNumberAnswers(questionData)


                            when (numberofAnswers) {
                                2 -> {
                                    group_num_answer.check(R.id.rb_edit_two_questions)
                                }
                                3 -> {
                                    group_num_answer.check(R.id.rb_edit_three_questions)
                                    label_answer3.visibility = View.VISIBLE
                                    answer_three.visibility = View.VISIBLE
                                    rb_three_correct.visibility = View.VISIBLE
                                }
                                4 -> {
                                    group_num_answer.check(R.id.rb_edit_four_questions)
                                    label_answer3.visibility = View.VISIBLE
                                    answer_three.visibility = View.VISIBLE
                                    rb_three_correct.visibility = View.VISIBLE
                                    label_answer4.visibility = View.VISIBLE
                                    answer_four.visibility = View.VISIBLE
                                    rb_four_correct.visibility = View.VISIBLE
                                }
                            }

                            when (questionData.correct_answer) {
                                1 -> group_correct_answer.check(R.id.rb_edit_one_correct)
                                2 -> group_correct_answer.check(R.id.rb_edit_two_correct)
                                3 -> group_correct_answer.check(R.id.rb_edit_three_correct)
                                4 -> group_correct_answer.check(R.id.rb_edit_four_correct)
                            }
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

        group_correct_answer.setOnCheckedChangeListener { group, checkedId ->

            when(checkedId){
                R.id.rb_edit_one_correct -> {
                    correctAnswer = 1
                }
                R.id.rb_edit_two_correct -> {
                    correctAnswer = 2
                }
                R.id.rb_edit_three_correct -> {
                    correctAnswer = 3
                }
                R.id.rb_edit_four_correct -> {
                    correctAnswer = 4
                }
                else -> correctAnswer = null
            }
            Log.d("right", correctAnswer.toString())
        }

        group_num_answer.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rb_edit_two_questions -> {
                    label_answer3.visibility = View.GONE
                    answer_three.visibility = View.GONE
                    rb_three_correct.visibility = View.GONE
                    label_answer4.visibility = View.GONE
                    answer_four.visibility = View.GONE
                    rb_four_correct.visibility = View.GONE
                    rb_three_correct.isChecked= false
                    rb_four_correct.isChecked= false
                    answer_three.setText("")
                    answer_four.setText("")
                    numberofAnswers = 2

                }
                R.id.rb_edit_three_questions -> {
                    label_answer3.visibility = View.VISIBLE
                    answer_three.visibility = View.VISIBLE
                    rb_three_correct.visibility = View.VISIBLE
                    label_answer4.visibility = View.GONE
                    answer_four.visibility = View.GONE
                    rb_four_correct.visibility = View.GONE
                    rb_four_correct.isChecked= false
                    answer_four.setText("")
                    numberofAnswers = 3
                }
                R.id.rb_edit_four_questions -> {
                    label_answer3.visibility = View.VISIBLE
                    answer_three.visibility = View.VISIBLE
                    rb_three_correct.visibility = View.VISIBLE
                    label_answer4.visibility = View.VISIBLE
                    answer_four.visibility = View.VISIBLE
                    rb_four_correct.visibility = View.VISIBLE
                    numberofAnswers = 4
                }
            }

            if(correctAnswer !=null && correctAnswer!! > numberofAnswers){
                correctAnswer = null
            }
        }

        val btn_leave_edit_quest = findViewById<ImageButton>(R.id.btn_leave_edit_quest)
        btn_leave_edit_quest.setOnClickListener {
            this.finish()
        }

//            if(!editedQuestion.trim().isEmpty()){
//
//                if (correctAnswer == null){
//                    Toast.makeText(this,"Resposta Correta Inválida", Toast.LENGTH_SHORT).show()
//                    return@setOnClickListener
//
//                } else if (numberofAnswers == 2 && !answer_one.trim().isEmpty() && !answer_two.trim().isEmpty()){
//                    val entry = QuestionRoom(id = questId ,quizId = quizId, question = editedQuestion, correctAnswer = correctAnswer!!, answer1 = answer_one, answer2 = answer_two, img = img_url)
//                    Daos(this).question.update(entry)
//
//                    this.setResult(Activity.RESULT_OK,Intent())
//                    this.finish()
//
//                }else if (numberofAnswers == 3 && !answer_one.trim().isEmpty() && !answer_two.trim().isEmpty() && !answer_three.trim().isEmpty()){
//
//                    val entry = QuestionRoom(id = questId ,quizId = quizId, question = editedQuestion, correctAnswer = correctAnswer!!, answer1 = answer_one, answer2 = answer_two, answer3 = answer_three, img = img_url)
//                    Daos(this).question.update(entry)
//
//                    this.setResult(Activity.RESULT_OK,Intent())
//                    this.finish()
//
//                }else if (numberofAnswers == 4 && !answer_one.trim().isEmpty() && !answer_two.trim().isEmpty() && !answer_three.trim().isEmpty() && !answer_four.trim().isEmpty()){
//
//                    val entry = QuestionRoom(id = questId ,quizId = quizId, question = editedQuestion, correctAnswer = correctAnswer!!, answer1 = answer_one, answer2 = answer_two, answer3 = answer_three, answer4 = answer_four, img = img_url)
//                    Daos(this).question.update(entry)
//
//                    this.setResult(Activity.RESULT_OK,Intent())
//                    this.finish()
//
//                }else{
//                    Toast.makeText(this,"Resposta(s) não Introduzidas", Toast.LENGTH_SHORT).show()
//                    return@setOnClickListener
//                }
//            } else{
//                Toast.makeText(this,"Questão não foi Introduzida", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }

//        }
    }

    fun getNumberAnswers(question: Question): Int{
        return when {
            !question.answer4.isNullOrEmpty() -> 4
            !question.answer3.isNullOrEmpty() -> 3
            else -> 2
        }
    }

    fun onUpdateClick(v: View){

        val questId = intent.getLongExtra("questId", 0)
        val question = question.text.toString()

        val answer1 = answer_one.text.toString()
        val answer2 = answer_two.text.toString()
        val answer3 = answer_three.text.toString()
        val answer4 = answer_four.text.toString()

        val img = img.text.toString().ifEmpty {
            null
        }



//        if( question.trim().isEmpty() || answer1.trim().isEmpty() || answer2.trim().isEmpty() || correctAnswer!! <= 0){
//            showToast("Campos Inválidos")
//            return
//        }

        val updatedQuestion = Question(id = questId, question = question, correct_answer = correctAnswer!!, answer1 = answer1, answer2 = answer2, answer3 = answer3, answer4 = answer4, img = img)

        lifecycleScope.launch{
            try {
                val sharedPreferences = getSharedPreferences("UserPref", MODE_PRIVATE)
                val token = sharedPreferences.getString("token","")

                val response = appService.updateQuestion("Bearer $token", questId, updatedQuestion)

                when (response.code()) {
                    200 -> {
                        showToast("Quiz editado com sucesso.")
                        finish()
                    }
                    400 -> showToast("Algo de errado não está certo.")
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
        val questId = intent.getLongExtra("questId", 0)

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Pretende mesmo apagar esta question?")
            .setPositiveButton("Yes") { dialog, id ->
                lifecycleScope.launch{
                    try {
                        val sharedPreferences = getSharedPreferences("UserPref", MODE_PRIVATE)
                        val token = sharedPreferences.getString("token","")

                        val response = appService.deleteQuestion("Bearer $token", questId)

                        when (response.code()) {
                            200 -> {
                                showToast("Question apagada com sucesso.")
                                finish()
                            }
                            400 -> showToast("Response code 400: bad request.")
                            401 -> showToast("Não pode apagar Questions de outro User.")
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