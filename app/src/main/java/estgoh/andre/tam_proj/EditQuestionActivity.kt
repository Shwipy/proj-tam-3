package estgoh.andre.tam_proj

import android.app.Activity
import android.app.AlertDialog
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
import estgoh.andre.tam_proj.DataBase.Question.Question
import estgoh.andre.tam_proj.Stuff.Daos


class EditQuestionActivity : AppCompatActivity() {

    fun getNumberAnswers(question: Question): Int{
        return when {
            !question.answer4.isNullOrEmpty() -> 4
            !question.answer3.isNullOrEmpty() -> 3
            else -> 2
        }
    }

    fun showDialog(questId: Long){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Pretende mesmo apagar a questões?")
            .setPositiveButton("Yes") { dialog, id ->
                Daos(this).question.delete(questId)
                this.setResult(Activity.RESULT_OK,Intent())
                finish()

            }
            .setNegativeButton("No") { dialog, id ->
            }
        builder.create().show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_question)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.new_quiz_sv)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val quizId = intent.getLongExtra("quizId", 0)
        val questId = intent.getLongExtra("questId", 0)
        var question = Daos(this).question.getQuestion(questId)
        var correctAnswer: Int? = question.correctAnswer
        var numberofAnswers = getNumberAnswers(question)


        val tf_question = findViewById<EditText>(R.id.tf_edit_question)

        val group_num_answer = findViewById<RadioGroup>(R.id.group_edit_num_answers)
        val group_correct_answer = findViewById<RadioGroup>(R.id.group_edit_correct_answer)

        val tf_answer_one = findViewById<EditText>(R.id.tf_edit_answer_one)
        val tf_answer_two = findViewById<EditText>(R.id.tf_edit_answer_two)
        val tf_answer_three = findViewById<EditText>(R.id.tf_edit_answer_three)
        val tf_answer_four = findViewById<EditText>(R.id.tf_edit_answer_four)

        val label_answer3 = findViewById<TextView>(R.id.label_edit_question_answer3)
        val label_answer4 = findViewById<TextView>(R.id.label_edit_question_answer4)

        val rb_three_correct = findViewById<RadioButton>(R.id.rb_edit_three_correct)
        val rb_four_correct = findViewById<RadioButton>(R.id.rb_edit_four_correct)


        val tf_edit_img_url = findViewById<EditText>(R.id.tf_edit_img_url)
        tf_question.setText(question.question)
        tf_edit_img_url.setText(question.img)


        when (numberofAnswers) {
            2 -> {
                group_num_answer.check(R.id.rb_edit_two_questions)
            }
            3 -> {
                group_num_answer.check(R.id.rb_edit_three_questions)
                label_answer3.visibility = View.VISIBLE
                tf_answer_three.visibility = View.VISIBLE
                rb_three_correct.visibility = View.VISIBLE
            }
            4 -> {
                group_num_answer.check(R.id.rb_edit_four_questions)
                label_answer3.visibility = View.VISIBLE
                tf_answer_three.visibility = View.VISIBLE
                rb_three_correct.visibility = View.VISIBLE
                label_answer4.visibility = View.VISIBLE
                tf_answer_four.visibility = View.VISIBLE
                rb_four_correct.visibility = View.VISIBLE
            }
        }

        when (question.correctAnswer) {
            1 -> group_correct_answer.check(R.id.rb_edit_one_correct)
            2 -> group_correct_answer.check(R.id.rb_edit_two_correct)
            3 -> group_correct_answer.check(R.id.rb_edit_three_correct)
            4 -> group_correct_answer.check(R.id.rb_edit_four_correct)
        }

        tf_answer_one.setText(question.answer1)
        tf_answer_two.setText(question.answer2)
        tf_answer_three.setText(question.answer3)
        tf_answer_four.setText(question.answer4)

        tf_edit_img_url.setText(question.img)

        group_num_answer.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rb_edit_two_questions -> {
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
                    numberofAnswers = 2

                }
                R.id.rb_edit_three_questions -> {
                    label_answer3.visibility = View.VISIBLE
                    tf_answer_three.visibility = View.VISIBLE
                    rb_three_correct.visibility = View.VISIBLE
                    label_answer4.visibility = View.GONE
                    tf_answer_four.visibility = View.GONE
                    rb_four_correct.visibility = View.GONE
                    rb_four_correct.isChecked= false
                    tf_answer_four.setText("")
                    numberofAnswers = 3
                }
                R.id.rb_edit_four_questions -> {
                    label_answer3.visibility = View.VISIBLE
                    tf_answer_three.visibility = View.VISIBLE
                    rb_three_correct.visibility = View.VISIBLE
                    label_answer4.visibility = View.VISIBLE
                    tf_answer_four.visibility = View.VISIBLE
                    rb_four_correct.visibility = View.VISIBLE
                    numberofAnswers = 4
                }
            }

            if(correctAnswer !=null && correctAnswer!! > numberofAnswers){
                correctAnswer = null
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

        val btn_leave_edit_quest = findViewById<ImageButton>(R.id.btn_leave_edit_quest)
        btn_leave_edit_quest.setOnClickListener {
            this.finish()
        }

        val btn_delete_question = findViewById<Button>(R.id.btn_delete_question)
        btn_delete_question.setOnClickListener {
            showDialog(questId)
        }

        val btn_save_question = findViewById<Button>(R.id.btn_save_question)
        btn_save_question.setOnClickListener {

            val editedQuestion = tf_question.text.toString()

            val answer_one = tf_answer_one.text.toString()
            val answer_two = tf_answer_two.text.toString()
            val answer_three = tf_answer_three.text.toString()
            val answer_four = tf_answer_four.text.toString()

            val img_url = tf_edit_img_url.text.toString().ifEmpty {
                null
            }

            if(!editedQuestion.trim().isEmpty()){

                if (correctAnswer == null){
                    Toast.makeText(this,"Resposta Correta Inválida", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener

                } else if (numberofAnswers == 2 && !answer_one.trim().isEmpty() && !answer_two.trim().isEmpty()){
                    val entry = Question(id = questId ,quizId = quizId, question = editedQuestion, correctAnswer = correctAnswer!!, answer1 = answer_one, answer2 = answer_two, img = img_url)
                    Daos(this).question.update(entry)

                    this.setResult(Activity.RESULT_OK,Intent())
                    this.finish()

                }else if (numberofAnswers == 3 && !answer_one.trim().isEmpty() && !answer_two.trim().isEmpty() && !answer_three.trim().isEmpty()){

                    val entry = Question(id = questId ,quizId = quizId, question = editedQuestion, correctAnswer = correctAnswer!!, answer1 = answer_one, answer2 = answer_two, answer3 = answer_three, img = img_url)
                    Daos(this).question.update(entry)

                    this.setResult(Activity.RESULT_OK,Intent())
                    this.finish()

                }else if (numberofAnswers == 4 && !answer_one.trim().isEmpty() && !answer_two.trim().isEmpty() && !answer_three.trim().isEmpty() && !answer_four.trim().isEmpty()){

                    val entry = Question(id = questId ,quizId = quizId, question = editedQuestion, correctAnswer = correctAnswer!!, answer1 = answer_one, answer2 = answer_two, answer3 = answer_three, answer4 = answer_four, img = img_url)
                    Daos(this).question.update(entry)

                    this.setResult(Activity.RESULT_OK,Intent())
                    this.finish()

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
}