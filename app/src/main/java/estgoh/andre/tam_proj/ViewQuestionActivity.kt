package estgoh.andre.tam_proj

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import estgoh.andre.tam_proj.DataBase.AppService
import estgoh.andre.tam_proj.DataBase.Question
import estgoh.andre.tam_proj.DataBase.Quiz
import estgoh.andre.tam_proj.DataBase.getRetrofit
import estgoh.andre.tam_proj.Stuff.QuestionAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//import estgoh.andre.tam_proj.Stuff.Daos
//import estgoh.andre.tam_proj.Stuff.QuestionAdapter

class ViewQuestionActivity : AppCompatActivity() {

    lateinit var appService: AppService

    private val getQuestion = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            fillRecyclerView()
        }
    }

    private val editQuestion = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            fillRecyclerView()
        }
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun fillRecyclerView(){
        val quizId = intent.getLongExtra("id", 0)
        val owned = intent.getBooleanExtra("owned", false)
        val context = this

        lifecycleScope.launch{
            try {
                val sharedPreferences = getSharedPreferences("UserPref", MODE_PRIVATE)
                val token = sharedPreferences.getString("token","")

                val response = appService.getQuizQuestions("Bearer $token", quizId)
                val body: List<Question>? = response.body()

                when (response.code()) {
                    200 -> if (body != null) {
                        val questions: List<Question> = body

                        withContext(Dispatchers.Main){

                            val adapter = QuestionAdapter(questions, owned)
                            adapter.onClick = object : QuestionAdapter.OnClickListener{
                                override fun onClick(questionId: Long) {
//                                    val intent = Intent(context, QuestionTestActivity::class.java)
//                                    intent.putExtra("id",questionId)
//                                    context.startActivity(intent)
                                }
                            }

                            adapter.onEditClick = object : QuestionAdapter.OnEditClickListener{
                                override fun onEditClick(questionId: Long) {
                                    val intent = Intent(context, EditQuestionActivity::class.java)
                                    intent.putExtra("questId",questionId)
                                    editQuestion.launch(intent)
                                }

                            }
                            val recyclerview: RecyclerView = findViewById(R.id.rv_question_list)
                            recyclerview.layoutManager = LinearLayoutManager(context)
                            recyclerview.adapter = adapter
                        }

                    }else {
                        showToast("Null object received")
                    }

                    else -> showToast("Response code: ${response.code()}")
                }

            }
            catch (e: Exception){
                showToast("Exception: ${e.message}")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fillRecyclerView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_question)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        appService = getRetrofit().create(AppService::class.java)

        val quizId = intent.getLongExtra("id", 0)
        val owned = intent.getBooleanExtra("owned", false)

//        showToast("Quiz ID: ${quizId}")
        val btn_add_new_question = findViewById<Button>(R.id.btn_add_new_question)
        if (!owned){
            btn_add_new_question.visibility = View.GONE
        }

        btn_add_new_question.setOnClickListener{
            val intent = Intent(this, AddQuestionActivity::class.java)
            intent.putExtra("quizId",quizId)
            getQuestion.launch(intent)
        }

        val btn_leave_view_quest = findViewById<ImageButton>(R.id.btn_leave_view_quest)
        btn_leave_view_quest.setOnClickListener{
            this.finish()
        }

        fillRecyclerView()

    }
}

