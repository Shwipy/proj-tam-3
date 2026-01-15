package estgoh.andre.tam_proj

import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import estgoh.andre.tam_proj.DataBase.APIResponses.ErrorResponse
import estgoh.andre.tam_proj.DataBase.AppService
import estgoh.andre.tam_proj.DataBase.Question
import estgoh.andre.tam_proj.DataBase.Quiz
import estgoh.andre.tam_proj.DataBase.getRetrofit
import estgoh.andre.tam_proj.Stuff.QuizAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep
import java.net.URL

lateinit var imgView: ImageView
lateinit var tv_question: TextView
lateinit var btn_op_1: Button
lateinit var btn_op_2: Button
lateinit var btn_op_3: Button
lateinit var btn_op_4: Button

lateinit var appService: AppService

class SolveQuizActivity : AppCompatActivity() {

    var rightAnswers = 0
    var solveId = 0
    var lastQuestion = false
    var acabou = false
    var timeLeft = -1
    var countdownJob: Job? = null

    var numQuestions: Int = 0

    private fun disableButtons(){
        btn_op_1 = findViewById<Button>(R.id.btn_option_1_solve)
        btn_op_2 = findViewById<Button>(R.id.btn_option_2_solve)
        btn_op_3 = findViewById<Button>(R.id.btn_option_3_solve)
        btn_op_4 = findViewById<Button>(R.id.btn_option_4_solve)

        btn_op_1.isEnabled = false
        btn_op_2.isEnabled = false
        btn_op_3.isEnabled = false
        btn_op_4.isEnabled = false

    }
    private fun resetButtons(){
        btn_op_1 = findViewById<Button>(R.id.btn_option_1_solve)
        btn_op_2 = findViewById<Button>(R.id.btn_option_2_solve)
        btn_op_3 = findViewById<Button>(R.id.btn_option_3_solve)
        btn_op_4 = findViewById<Button>(R.id.btn_option_4_solve)

        val default_color = ContextCompat.getColor(this, R.color.standard)
        val default_text_color = ContextCompat.getColor(this, R.color.black)

        btn_op_1.isEnabled = true
        btn_op_1.backgroundTintList = ColorStateList.valueOf(default_color)
        btn_op_1.setTextColor(ColorStateList.valueOf(default_text_color))
        btn_op_2.isEnabled = true
        btn_op_2.backgroundTintList = ColorStateList.valueOf(default_color)
        btn_op_2.setTextColor(ColorStateList.valueOf(default_text_color))
        btn_op_3.isEnabled = true
        btn_op_3.backgroundTintList = ColorStateList.valueOf(default_color)
        btn_op_3.setTextColor(ColorStateList.valueOf(default_text_color))
        btn_op_4.isEnabled = true
        btn_op_4.backgroundTintList = ColorStateList.valueOf(default_color)
        btn_op_4.setTextColor(ColorStateList.valueOf(default_text_color))

    }

    private fun showCorrect(correct:Int){
        btn_op_1 = findViewById<Button>(R.id.btn_option_1_solve)
        btn_op_2 = findViewById<Button>(R.id.btn_option_2_solve)
        btn_op_3 = findViewById<Button>(R.id.btn_option_3_solve)
        btn_op_4 = findViewById<Button>(R.id.btn_option_4_solve)

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

    fun fillQuestion(question: Question){
        tv_question = findViewById<TextView>(R.id.tv_question_solve)
        btn_op_1 = findViewById<Button>(R.id.btn_option_1_solve)
        btn_op_2 = findViewById<Button>(R.id.btn_option_2_solve)
        btn_op_3 = findViewById<Button>(R.id.btn_option_3_solve)
        btn_op_4 = findViewById<Button>(R.id.btn_option_4_solve)
        val btn_next = findViewById<ImageButton>(R.id.btn_next_question_quiz)
        btn_next.visibility = View.GONE
        resetButtons()

        val correct_answer = question.correct_answer
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

        imgView = findViewById<ImageView>(R.id.url_img_solve)
        imgView.visibility = View.GONE
        //https://www.geeksforgeeks.org/android/running-user-interface-thread-in-android-using-kotlin/
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
            btn_op_3.visibility = View.GONE
        }else{
            btn_op_3.visibility = View.VISIBLE
        }
        if (answer_four == null){
            btn_op_4.visibility = View.GONE
        }else{
            btn_op_4.visibility = View.VISIBLE
        }

        btn_op_1.setOnClickListener {

            if (correct_answer == 1){
//              https://www.geeksforgeeks.org/android/how-to-change-the-background-color-of-button-in-android-using-colorstatelist
                btn_op_1.backgroundTintList = ColorStateList.valueOf(correct_color)
                btn_op_1.setTextColor(ColorStateList.valueOf(new_text_color))
                rightAnswers += 1
            }else{
                btn_op_1.backgroundTintList = ColorStateList.valueOf(wrong_color)
                btn_op_1.setTextColor(ColorStateList.valueOf(new_text_color))
                showCorrect(correct_answer)
            }
            disableButtons()
            btn_next.visibility = View.VISIBLE
            if (lastQuestion){
                acabou = true
                showDialog()
            }
        }

        btn_op_2.setOnClickListener {
            if (correct_answer == 2){
                btn_op_2.backgroundTintList = ColorStateList.valueOf(correct_color)
                btn_op_2.setTextColor(ColorStateList.valueOf(new_text_color))
                rightAnswers += 1
            }else{
                btn_op_2.backgroundTintList = ColorStateList.valueOf(wrong_color)
                btn_op_2.setTextColor(ColorStateList.valueOf(new_text_color))
                showCorrect(correct_answer)
            }
            disableButtons()
            btn_next.visibility = View.VISIBLE
            if (lastQuestion){
                acabou = true
                showDialog()
            }
        }

        btn_op_3.setOnClickListener {
            if (correct_answer == 3){
                btn_op_3.backgroundTintList = ColorStateList.valueOf(correct_color)
                btn_op_3.setTextColor(ColorStateList.valueOf(new_text_color))
                rightAnswers += 1
            }else{
                btn_op_3.backgroundTintList = ColorStateList.valueOf(wrong_color)
                btn_op_3.setTextColor(ColorStateList.valueOf(new_text_color))
                showCorrect(correct_answer)
            }
            disableButtons()
            btn_next.visibility = View.VISIBLE
            if (lastQuestion){
                acabou = true
                showDialog()
            }
        }

        btn_op_4.setOnClickListener {
            if (correct_answer == 4){
                btn_op_4.backgroundTintList = ColorStateList.valueOf(correct_color)
                btn_op_4.setTextColor(ColorStateList.valueOf(new_text_color))
                rightAnswers += 1
            }else{
                btn_op_4.backgroundTintList = ColorStateList.valueOf(wrong_color)
                btn_op_4.setTextColor(ColorStateList.valueOf(new_text_color))
                showCorrect(correct_answer)
            }
            disableButtons()
            btn_next.visibility = View.VISIBLE
            if (lastQuestion){
                acabou = true
                showDialog()
            }
        }

        val btn_leave_quiz = findViewById<ImageButton>(R.id.btn_leave_quiz)
        btn_leave_quiz.setOnClickListener{
            acabou = true
            showDialog()

        }
    }

    fun showDialog(){
        userEndQuiz()
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Acertou ${rightAnswers} de ${numQuestions} Questões!!! :D")
            .setPositiveButton("OK") { dialog, id ->
                finish()
            }
        builder.create().show()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_solve_quiz)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if(savedInstanceState != null){
            timeLeft = savedInstanceState.getInt("time")
            solveId = savedInstanceState.getInt("solveId")
            rightAnswers = savedInstanceState.getInt("rightAnswers")

            val tv_time_left = findViewById<TextView>(R.id.tv_time_left)
            tv_time_left.text = "Time Left: ${timeLeft / 1000}s"
        }

        val quizId = intent.getLongExtra("quizId", 0)
        appService = getRetrofit().create(AppService::class.java)

        lifecycleScope.launch{

            try {
                val sharedPreferences = getSharedPreferences("UserPref", MODE_PRIVATE)
                val token = sharedPreferences.getString("token","")

                userStartQuiz()

                val response = appService.getQuizQuestions("Bearer $token", quizId)
                val body: List<Question>? = response.body()
                when (response.code()) {
                    200 -> if (body != null) {
                        val questions: List<Question> = body
                        numQuestions = questions.size
                        if (numQuestions == 1){
                            lastQuestion = true
                        }

                        withContext(Dispatchers.Main) {
                            fillQuestion(questions[solveId])

                            val btn_next_question = findViewById<ImageButton>(R.id.btn_next_question_quiz)
                            btn_next_question.setOnClickListener{

                                solveId += 1

                                if (solveId == numQuestions-1){
                                    lastQuestion = true
                                }

                                if(solveId <= numQuestions - 1) {
                                    fillQuestion(questions[solveId])
                                }else{
                                    acabou = true
                                    showDialog()
                                }
                            }

                            createAndRunCoroutine()
                        }

                    } else {
                        showToast("Null object received")
                    }
                    400 -> {
                        showToast("Quiz ainda não tem Questões")
                        finish()
                    }
                    else -> {
                        showToast("Response code: ${response.code()}")
                        finish()
                    }
                }
            }
            catch (e: Exception){
                showToast("Exception: ${e.message}")
                finish()
            }
        }
    }
    fun createAndRunCoroutine(){
        val duration = intent.getIntExtra("duration", 0)
        val tv_time_left = findViewById<TextView>(R.id.tv_time_left)
        val quizTime = (duration) * 1000

        if(timeLeft == -1){
            timeLeft = quizTime
        }

        countdownJob = CoroutineScope(Dispatchers.IO).launch{
            while (timeLeft > 0){
                if(!acabou) {
                    withContext(Dispatchers.Main){
                        tv_time_left.text = "Time Left: ${timeLeft / 1000}s"
                    }

                    sleep(1000)
                    timeLeft -= 1000
                }
            }

            withContext(Dispatchers.Main) {
                tv_time_left.text = "Time Left: 0s"
                showDialog()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("time", timeLeft)
        outState.putInt("solveId", solveId)
        outState.putInt("rightAnswers",rightAnswers)

        countdownJob?.cancel()

        super.onSaveInstanceState(outState)
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    fun userStartQuiz(){
        lifecycleScope.launch{
            try {
                val sharedPreferences = getSharedPreferences("UserPref", MODE_PRIVATE)
                val token = sharedPreferences.getString("token","")
                val quizId = intent.getLongExtra("quizId", 0)

                val response = appService.addPlayer("Bearer $token", quizId)
                when (response.code()) {
                    200 -> showToast("Quiz Iniciado.")
                    404 -> showToast("Quiz não está disponivel")

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

    fun userEndQuiz(){
        lifecycleScope.launch{
            try {
                val sharedPreferences = getSharedPreferences("UserPref", MODE_PRIVATE)
                val token = sharedPreferences.getString("token","")

                val response = appService.removePlayer("Bearer $token")
                when (response.code()) {
                    200 -> showToast("Quiz Terminado.")

                    else -> {
                        showToast("Response code: ${response.code()}")
                    }
                }

            }
            catch (e: Exception){
                showToast("Exception: ${e.message}")
            }
        }
    }

}