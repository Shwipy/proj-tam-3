package estgoh.andre.tam_proj

import estgoh.andre.tam_proj.Stuff.QuizAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import estgoh.andre.tam_proj.DataBase.Question.Question
import estgoh.andre.tam_proj.DataBase.Quiz.Quiz
import estgoh.andre.tam_proj.Stuff.Daos

class MainActivity : AppCompatActivity() {
    var quizes : List<Quiz> = emptyList()
    var questions = ArrayList<Question>()
    private val getQuiz = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            fillRecyclerView()
        }
    }
    private val editQuiz = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            fillRecyclerView()
        }
    }

    fun fillRecyclerView(){
            val quizes = Daos(this).quiz.getAll()

            val context = this
            val adapter = QuizAdapter(quizes)
            adapter.onClick = object : QuizAdapter.OnClickListener{
                override fun onClick(quizId: Long) {
                    val numQuest = Daos(context).question.getQuizQuestions(quizId).size

                    if (numQuest > 0){
                        val intent = Intent(context, SolveQuizActivity::class.java)
                        intent.putExtra("quizId",quizId)
                        context.startActivity(intent)
                    }else{
                        Toast.makeText(context, "Quiz ainda não tem questões!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            adapter.onEditClick = object : QuizAdapter.OnEditClickListener{
                override fun onEditClick(quizId: Long) {
                    val intent = Intent(context, EditQuizActivity::class.java)
                    intent.putExtra("id",quizId)
                    editQuiz.launch(intent)
                }

            }
            val recyclerview: RecyclerView = findViewById(R.id.rv_quiz_list)
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.new_quiz_sv)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var btn_add_quiz = findViewById<Button>(R.id.btn_menu_add_quiz)
        btn_add_quiz.setOnClickListener{
            val intent = Intent(this, AddQuizActivity::class.java)
            getQuiz.launch(intent)
        }

        var btn_menu_info = findViewById<ImageButton>(R.id.btn_menu_info)
        btn_menu_info.setOnClickListener{
            val intent = Intent(this, InfoActivity::class.java)
            this.startActivity(intent)
        }

        fillRecyclerView()

    }
}