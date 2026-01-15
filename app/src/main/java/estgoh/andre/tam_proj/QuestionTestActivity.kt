package estgoh.andre.tam_proj

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import estgoh.andre.tam_proj.DataBase.AppService
import estgoh.andre.tam_proj.DataBase.Question
import estgoh.andre.tam_proj.DataBase.getRetrofit
//import estgoh.andre.tam_proj.Stuff.Daos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL


class QuestionTestActivity : AppCompatActivity() {
    lateinit var imgView: ImageView
    lateinit var tv_question: TextView
    lateinit var btn_op_1: Button
    lateinit var btn_op_2: Button
    lateinit var btn_op_3: Button
    lateinit var btn_op_4: Button

    lateinit var appService: AppService

    private fun disableButtons(){
        val btn_op_1 = findViewById<Button>(R.id.btn_option_1_test)
        val btn_op_2 = findViewById<Button>(R.id.btn_option_2_test)
        val btn_op_3 = findViewById<Button>(R.id.btn_option_3_test)
        val btn_op_4 = findViewById<Button>(R.id.btn_option_4_test)

        btn_op_1.isEnabled = false
        btn_op_2.isEnabled = false
        btn_op_3.isEnabled = false
        btn_op_4.isEnabled = false

    }

    private fun showCorrect(correct:Int){
        val btn_op_1 = findViewById<Button>(R.id.btn_option_1_test)
        val btn_op_2 = findViewById<Button>(R.id.btn_option_2_test)
        val btn_op_3 = findViewById<Button>(R.id.btn_option_3_test)
        val btn_op_4 = findViewById<Button>(R.id.btn_option_4_test)

        val correct_color = ContextCompat.getColor(this, R.color.correct)
        val new_text_color = ContextCompat.getColor(this, R.color.white)

        val right_answer_button = when(correct){
            1 -> btn_op_1
            2 -> btn_op_2
            3 -> btn_op_3
            else -> btn_op_4
        }
        right_answer_button.backgroundTintList = ColorStateList.valueOf(correct_color)
        right_answer_button.setTextColor(ColorStateList.valueOf(new_text_color))
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_question_test)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val questId = intent.getLongExtra("id", 0)
        appService = getRetrofit().create(AppService::class.java)
//        val question = Daos(this).question.getQuestion(questId)

        tv_question = findViewById<TextView>(R.id.tv_question_test)
        btn_op_1 = findViewById<Button>(R.id.btn_option_1_test)
        btn_op_2 = findViewById<Button>(R.id.btn_option_2_test)
        btn_op_3 = findViewById<Button>(R.id.btn_option_3_test)
        btn_op_4 = findViewById<Button>(R.id.btn_option_4_test)

        var correct_answer: Int
        var answer_three: String?
        var answer_four: String?

        val correct_color = ContextCompat.getColor(this, R.color.correct)
        val wrong_color = ContextCompat.getColor(this, R.color.wrong)
        val new_text_color = ContextCompat.getColor(this, R.color.white)

        lifecycleScope.launch{

            try {
                val sharedPreferences = getSharedPreferences("UserPref", MODE_PRIVATE)
                val token = sharedPreferences.getString("token","")

                val response = appService.getQuestion("Bearer $token", questId)
                val body: Question? = response.body()
                when (response.code()) {
                    200 -> if (body != null) {
                        val questionData: Question = body

                        correct_answer = questionData.correct_answer
                        answer_three = questionData.answer3
                        answer_four = questionData.answer4

                        withContext(Dispatchers.Main) {

                            tv_question.text = questionData.question
                            btn_op_1.text = questionData.answer1
                            btn_op_2.text = questionData.answer2
                            btn_op_3.text = questionData.answer3
                            btn_op_4.text = questionData.answer4

                            //https://www.geeksforgeeks.org/android/running-user-interface-thread-in-android-using-kotlin/
                            imgView = findViewById<ImageView>(R.id.url_img)

                            if (questionData.img != null){
                                val url = URL(questionData.img)
                                val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())

                                imgView.visibility = View.VISIBLE
                                imgView.setImageBitmap(bmp)

                                if (answer_three == null){

                                    //https://www.geeksforgeeks.org/kotlin/how-to-make-a-button-invisible-in-android
                                    btn_op_3.visibility = View.GONE
                                }
                                if (answer_four == null){
                                    btn_op_4.visibility = View.GONE
                                }

                            }
                            btn_op_1.setOnClickListener {

                                if (correct_answer == 1){
//                                  https://www.geeksforgeeks.org/android/how-to-change-the-background-color-of-button-in-android-using-colorstatelist
                                    btn_op_1.backgroundTintList = ColorStateList.valueOf(correct_color)
                                    btn_op_1.setTextColor(ColorStateList.valueOf(new_text_color))
                                }else{
                                    btn_op_1.backgroundTintList = ColorStateList.valueOf(wrong_color)
                                    btn_op_1.setTextColor(ColorStateList.valueOf(new_text_color))
                                    showCorrect(correct_answer)
                                }
                                disableButtons()
                            }

                            btn_op_2.setOnClickListener {
                                if (correct_answer == 2){
                                    btn_op_2.backgroundTintList = ColorStateList.valueOf(correct_color)
                                    btn_op_2.setTextColor(ColorStateList.valueOf(new_text_color))
                                }else{
                                    btn_op_2.backgroundTintList = ColorStateList.valueOf(wrong_color)
                                    btn_op_2.setTextColor(ColorStateList.valueOf(new_text_color))
                                    showCorrect(correct_answer)
                                }
                                disableButtons()

                            }

                            btn_op_3.setOnClickListener {
                                if (correct_answer == 3){
                                    btn_op_3.backgroundTintList = ColorStateList.valueOf(correct_color)
                                    btn_op_3.setTextColor(ColorStateList.valueOf(new_text_color))
                                }else{
                                    btn_op_3.backgroundTintList = ColorStateList.valueOf(wrong_color)
                                    btn_op_3.setTextColor(ColorStateList.valueOf(new_text_color))
                                    showCorrect(correct_answer)
                                }
                                disableButtons()

                            }

                            btn_op_4.setOnClickListener {
                                if (correct_answer == 4){
                                    btn_op_4.backgroundTintList = ColorStateList.valueOf(correct_color)
                                    btn_op_4.setTextColor(ColorStateList.valueOf(new_text_color))
                                }else{
                                    btn_op_4.backgroundTintList = ColorStateList.valueOf(wrong_color)
                                    btn_op_4.setTextColor(ColorStateList.valueOf(new_text_color))
                                    showCorrect(correct_answer)
                                }
                                disableButtons()

                            }
                        }

                    }
                    404 -> {
                        showToast("Quiz já não existe.")
                        finish()
                    }
                    else -> showToast("Response code: ${response.code()}")
                }
            }
            catch (e: Exception){
                showToast("Exception: ${e.message}")
            }
        }

        val btn_leave_test_question = findViewById<ImageButton>(R.id.btn_leave_test_question)
        btn_leave_test_question.setOnClickListener{
            this.finish()
        }


    }
}