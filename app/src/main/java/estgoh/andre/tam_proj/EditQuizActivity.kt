package estgoh.andre.tam_proj

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import estgoh.andre.tam_proj.Stuff.Daos

class EditQuizActivity : AppCompatActivity() {

    fun showDialog(quizId: Long){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Pretende mesmo apagar este quiz?")
            .setPositiveButton("Yes") { dialog, id ->
                Daos(this).quiz.delete(quizId)
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
        setContentView(R.layout.activity_edit_quiz)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val quizId = intent.getLongExtra("id", 0)

        var quiz = Daos(this).quiz.getQuiz(quizId)

        val tf_edit_title = findViewById<EditText>(R.id.tf_edit_title)
        val tf_edit_description = findViewById<EditText>(R.id.tf_edit_description)
        val tf_edit_time = findViewById<EditText>(R.id.tf_edit_time)

        tf_edit_title.setText(quiz.title)
        tf_edit_description.setText(quiz.description)
        tf_edit_time.setText(quiz.time.toString())

        val btn_leave_edit_quiz = findViewById<ImageButton>(R.id.btn_leave_edit_quiz)
        btn_leave_edit_quiz.setOnClickListener{
            this.setResult(Activity.RESULT_OK,Intent())
            this.finish()
        }

        val btn_delete_quiz = findViewById<ImageButton>(R.id.btn_delete_quiz)
        btn_delete_quiz.setOnClickListener {
            showDialog(quizId)
        }

        val btn_confirm_edit_quiz = findViewById<Button>(R.id.btn_confirm_edit_quiz)
        btn_confirm_edit_quiz.setOnClickListener {
            val title = tf_edit_title.text.toString()
            val description = tf_edit_description.text.toString()
            val time = tf_edit_time.text.toString().toIntOrNull()

            if( title.trim().isEmpty() || description.trim().isEmpty() || time == null || time <= 0){
                Toast.makeText(this,"Campos Inválidos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            quiz.title = title
            quiz.description = description
            quiz.time = time

            Daos(this).quiz .update(quiz)

            Toast.makeText(this,"Quiz foi alterado!", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }
        val btn_view_questions = findViewById<Button>(R.id.btn_view_questions)
        btn_view_questions.setOnClickListener {
            val intent = Intent(this, ViewQuestionActivity::class.java)
            intent.putExtra("id",quizId)
            this.startActivity(intent)
        }
    }
}