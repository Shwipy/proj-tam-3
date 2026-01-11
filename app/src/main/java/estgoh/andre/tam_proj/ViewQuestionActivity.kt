//package estgoh.andre.tam_proj
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.ImageButton
//import androidx.activity.enableEdgeToEdge
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import estgoh.andre.tam_proj.Stuff.Daos
//import estgoh.andre.tam_proj.Stuff.QuestionAdapter
//
//class ViewQuestionActivity : AppCompatActivity() {
//
//
//    private val getQuestion = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == RESULT_OK) {
//            fillRecyclerView()
//        }
//    }
//
//    private val editQuestion = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == RESULT_OK) {
//            fillRecyclerView()
//        }
//    }
//
//    fun fillRecyclerView(){
//        var quizId = intent.getLongExtra("id", 0)
//
//        val questions = Daos(this).question.getQuizQuestions(quizId)
//        val context = this
//        val adapter = QuestionAdapter(questions)
//        adapter.onClick = object : QuestionAdapter.OnClickListener{
//            override fun onClick(questionId: Long) {
//                val intent = Intent(context, QuestionTestActivity::class.java)
//                intent.putExtra("id",questionId)
//                context.startActivity(intent)
//            }
//        }
//
//        adapter.onEditClick = object : QuestionAdapter.OnEditClickListener{
//            override fun onEditClick(questionId: Long) {
//                val intent = Intent(context, EditQuestionActivity::class.java)
//                intent.putExtra("quizId",quizId)
//                intent.putExtra("questId",questionId)
//                editQuestion.launch(intent)
//            }
//
//        }
//        val recyclerview: RecyclerView = findViewById(R.id.rv_question_list)
//        recyclerview.layoutManager = LinearLayoutManager(this)
//        recyclerview.adapter = adapter
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_view_question)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//        var quizId = intent.getLongExtra("id", 0)
//
//        var btn_add_new_question = findViewById<Button>(R.id.btn_add_new_question)
//        btn_add_new_question.setOnClickListener{
//            val intent = Intent(this, AddQuestionActivity::class.java)
//            intent.putExtra("id",quizId)
//            getQuestion.launch(intent)
//        }
//
//        val btn_leave_view_quest = findViewById<ImageButton>(R.id.btn_leave_view_quest)
//        btn_leave_view_quest.setOnClickListener{
//            this.finish()
//        }
//
//        fillRecyclerView()
//
//    }
//}
//
