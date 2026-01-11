//package estgoh.andre.tam_proj
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import android.widget.Button
//import android.widget.EditText
//import android.widget.RadioButton
//import android.widget.RadioGroup
//import android.widget.TextView
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import estgoh.andre.tam_proj.DataBase.Question.QuestionRoom
//import estgoh.andre.tam_proj.Stuff.Daos
//
//class AddQuestionActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_add_question)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.new_quiz_sv)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        val quizId = intent.getLongExtra("id", 0)
//
//        val tf_question = findViewById<EditText>(R.id.tf_question)
//
//        var selected_answer_num = findViewById<RadioButton>(R.id.rb_two_questions)
//        var selected_correct_answer = findViewById<RadioButton>(R.id.rb_one_correct)
//        val group_num_answer = findViewById<RadioGroup>(R.id.group_num_answers)
//        val group_correct_answer = findViewById<RadioGroup>(R.id.group_correct_answer)
//
//        val tf_answer_one = findViewById<EditText>(R.id.tf_answer_one)
//        val tf_answer_two = findViewById<EditText>(R.id.tf_answer_two)
//        val tf_answer_three = findViewById<EditText>(R.id.tf_answer_three)
//        val tf_answer_four = findViewById<EditText>(R.id.tf_answer_four)
//
//        val label_answer3 = findViewById<TextView>(R.id.label_new_question_answer3)
//        val label_answer4 = findViewById<TextView>(R.id.label_new_question_answer4)
//
//        val rb_three_correct = findViewById<RadioButton>(R.id.rb_three_correct)
//        val rb_four_correct = findViewById<RadioButton>(R.id.rb_four_correct)
//
//
//        val tf_img_url = findViewById<EditText>(R.id.tf_img_url)
//
//        group_num_answer.setOnCheckedChangeListener { group, checkedId ->
//            selected_answer_num = findViewById<View?>(checkedId) as RadioButton
//
//            val num_answers = selected_answer_num.text.toString().toInt()
//            if(num_answers ==2 ){
//                label_answer3.visibility = View.GONE
//                tf_answer_three.visibility = View.GONE
//                rb_three_correct.visibility = View.GONE
//                label_answer4.visibility = View.GONE
//                tf_answer_four.visibility = View.GONE
//                rb_four_correct.visibility = View.GONE
//                rb_three_correct.isChecked= false
//                rb_four_correct.isChecked= false
//                tf_answer_three.setText("")
//                tf_answer_four.setText("")
//            }
//            if (num_answers >= 3){
//                label_answer3.visibility = View.VISIBLE
//                tf_answer_three.visibility = View.VISIBLE
//                rb_three_correct.visibility = View.VISIBLE
//                label_answer4.visibility = View.GONE
//                tf_answer_four.visibility = View.GONE
//                rb_four_correct.visibility = View.GONE
//                rb_four_correct.isChecked= false
//                tf_answer_four.setText("")
//            }
//            if (num_answers == 4){
//                label_answer3.visibility = View.VISIBLE
//                tf_answer_three.visibility = View.VISIBLE
//                rb_three_correct.visibility = View.VISIBLE
//                label_answer4.visibility = View.VISIBLE
//                tf_answer_four.visibility = View.VISIBLE
//                rb_four_correct.visibility = View.VISIBLE
//            }
//        }
//
//        group_correct_answer.setOnCheckedChangeListener { group, checkedId ->
//            selected_correct_answer = findViewById<View?>(checkedId) as RadioButton
//        }
//
//        var btn_cancel = findViewById<Button>(R.id.btn_cancel_question)
//        btn_cancel.setOnClickListener {
//            this.finish()
//        }
//
//        var btn_add = findViewById<Button>(R.id.btn_add_question)
//        btn_add.setOnClickListener {
//
//            val question = tf_question.text.toString()
//
//            val answer_one = tf_answer_one.text.toString()
//            val answer_two = tf_answer_two.text.toString()
//            val answer_three = tf_answer_three.text.toString()
//            val answer_four = tf_answer_four.text.toString()
//
//            val num_answers = selected_answer_num.text.toString().toInt()
//            val correct_answer = selected_correct_answer.text.toString().toIntOrNull()
//
//            val img_url = tf_img_url.text.toString().ifEmpty {
//                null
//            }
//
//            if(!question.trim().isEmpty()){
//
//                if ( correct_answer == null || correct_answer > num_answers){
//                    Toast.makeText(this,"Resposta Correta Inválida", Toast.LENGTH_SHORT).show()
//                    return@setOnClickListener
//
//                } else if (num_answers == 2 && !answer_one.trim().isEmpty() && !answer_two.trim().isEmpty()){
//                    val entry = QuestionRoom(quizId = quizId, question = question, correctAnswer = correct_answer, answer1 = answer_one, answer2 = answer_two, img = img_url)
//                    Daos(this).question.insert(entry)
//
//                    this.setResult(Activity.RESULT_OK,Intent())
//                    this.finish()
//
//                }else if (num_answers == 3 && !answer_one.trim().isEmpty() && !answer_two.trim().isEmpty() && !answer_three.trim().isEmpty()){
//
//                    val entry = QuestionRoom(quizId = quizId, question = question, correctAnswer = correct_answer, answer1 = answer_one, answer2 = answer_two, answer3 = answer_three, img = img_url)
//                    Daos(this).question.insert(entry)
//
//                    this.setResult(Activity.RESULT_OK,Intent())
//                    this.finish()
//
//                }else if (num_answers == 4 && !answer_one.trim().isEmpty() && !answer_two.trim().isEmpty() && !answer_three.trim().isEmpty() && !answer_four.trim().isEmpty()){
//
//                    val entry = QuestionRoom(quizId = quizId, question = question, correctAnswer = correct_answer, answer1 = answer_one, answer2 = answer_two, answer3 = answer_three, answer4 = answer_four, img = img_url)
//                    Daos(this).question.insert(entry)
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
//
//        }
//    }
//}