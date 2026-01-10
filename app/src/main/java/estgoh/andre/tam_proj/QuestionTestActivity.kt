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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import estgoh.andre.tam_proj.Stuff.Daos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL


class QuestionTestActivity : AppCompatActivity() {

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
        val question = Daos(this).question.getQuestion(questId)

        val tv_question = findViewById<TextView>(R.id.tv_question_test)
        val btn_op_1 = findViewById<Button>(R.id.btn_option_1_test)
        val btn_op_2 = findViewById<Button>(R.id.btn_option_2_test)
        val btn_op_3 = findViewById<Button>(R.id.btn_option_3_test)
        val btn_op_4 = findViewById<Button>(R.id.btn_option_4_test)

        val correct_answer = question.correctAnswer
        val answer_three = question.answer3
        val answer_four = question.answer4

        val correct_color = ContextCompat.getColor(this, R.color.correct)
        val wrong_color = ContextCompat.getColor(this, R.color.wrong)
        val new_text_color = ContextCompat.getColor(this, R.color.white)

        tv_question.text = question.question
        btn_op_1.text = question.answer1
        btn_op_2.text = question.answer2
        btn_op_3.text = question.answer3
        btn_op_4.text = question.answer4

        //https://www.geeksforgeeks.org/android/running-user-interface-thread-in-android-using-kotlin/
        val imgView = findViewById<ImageView>(R.id.url_img)

        if (question.img != null){
            lifecycleScope.launch(Dispatchers.IO){
                try {
                    val url = URL(question.img)
//                  val url = URL("https://hips.hearstapps.com/hmg-prod/images/dog-puppy-on-garden-royalty-free-image-1586966191.jpg?crop=0.752xw:1.00xh;0.175xw,0&resize=1200:*")
                    val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    withContext(Dispatchers.Main) {
                        imgView.visibility = View.VISIBLE
                        imgView.setImageBitmap(bmp)
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }


        if (answer_three == null){

            //https://www.geeksforgeeks.org/kotlin/how-to-make-a-button-invisible-in-android
            btn_op_3.visibility = View.GONE
        }
        if (answer_four == null){
            btn_op_4.visibility = View.GONE
        }

        btn_op_1.setOnClickListener {

            if (correct_answer == 1){
//              https://www.geeksforgeeks.org/android/how-to-change-the-background-color-of-button-in-android-using-colorstatelist
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

        val btn_leave_test_question = findViewById<ImageButton>(R.id.btn_leave_test_question)
        btn_leave_test_question.setOnClickListener{
            this.finish()
        }
    }
}