package estgoh.andre.tam_proj

import android.R.id.message
import estgoh.andre.tam_proj.Stuff.QuizAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import estgoh.andre.tam_proj.DataBase.AppService
//import estgoh.andre.tam_proj.DataBase.Question.QuestionRoom
import estgoh.andre.tam_proj.DataBase.Quiz
//import estgoh.andre.tam_proj.DataBase.Quiz.QuizRoom
import estgoh.andre.tam_proj.DataBase.User
import estgoh.andre.tam_proj.DataBase.getRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    lateinit var appService: AppService
    var mine: Boolean = false

//    var quizes : List<QuizRoom> = emptyList()
//    var questionRooms = ArrayList<QuestionRoom>()
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

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun fillRecyclerView(){
        val context = this

        lifecycleScope.launch{
            try {
                val sharedPreferences = getSharedPreferences("UserPref", MODE_PRIVATE)
                val token = sharedPreferences.getString("token","")

                val response = appService.getAllQuizes("Bearer $token")
                val body: List<Quiz>? = response.body()
                when (response.code()) {
                    200 -> if (body != null) {
                        val allQuizes: List<Quiz> = body

                        withContext(Dispatchers.Main){
                            val adapter = if (mine){
                                val myQuizes = allQuizes.filter { it.owned }
                                QuizAdapter(myQuizes)
                            } else{
                                QuizAdapter(allQuizes)
                            }

                            adapter.onClick = object : QuizAdapter.OnClickListener{
                                override fun onClick(quizId: Long, duration: Int) {

                                        val intent = Intent(context, SolveQuizActivity::class.java)
                                        intent.putExtra("quizId",quizId)
                                        intent.putExtra("duration",duration)
                                        context.startActivity(intent)
                                }
                            }

                            adapter.onEditClick = object : QuizAdapter.OnEditClickListener{
                                override fun onEditClick(quizId: Long, owned: Boolean) {

                                    val intent = Intent(context, EditQuizActivity::class.java)
                                    intent.putExtra("id",quizId)
                                    intent.putExtra("owned",owned)
                                    editQuiz.launch(intent)
                                }

                            }
                            val recyclerview: RecyclerView = findViewById(R.id.rv_quiz_list)
                            recyclerview.layoutManager = LinearLayoutManager(context)
                            recyclerview.adapter = adapter
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
    }

    override fun onResume() {
        super.onResume()
        fillRecyclerView()
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

        appService = getRetrofit().create(AppService::class.java)

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

//        https://www.geeksforgeeks.org/kotlin/switch-in-kotlin/
        val switch: SwitchCompat = findViewById(R.id.switch_myQuizes)
        switch.setOnCheckedChangeListener({ _ , isChecked ->
            mine = isChecked
//            Toast.makeText(this, "mudou",Toast.LENGTH_SHORT).show()
            fillRecyclerView()
        })



    }
}