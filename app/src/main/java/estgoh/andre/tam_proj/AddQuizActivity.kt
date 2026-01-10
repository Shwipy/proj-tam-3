package estgoh.andre.tam_proj

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import estgoh.andre.tam_proj.DataBase.Quiz.Quiz
import estgoh.andre.tam_proj.DataBase.QuizDatabase
import estgoh.andre.tam_proj.Stuff.Daos

class AddQuizActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_quiz)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btn_cancel = findViewById<Button>(R.id.btn_cancel_quiz)
        btn_cancel.setOnClickListener{
            this.finish()
        }

        val btn_add = findViewById<Button>(R.id.btn_add_quiz)
        btn_add.setOnClickListener{
            val tf_title = findViewById<EditText>(R.id.tf_title)
            val tf_description = findViewById<EditText>(R.id.tf_description)
            val tf_time = findViewById<EditText>(R.id.tf_time)

            val title = tf_title.text.toString()
            val description = tf_description.text.toString()
            val time = tf_time.text.toString().toIntOrNull()

            if( title.trim().isEmpty() || description.trim().isEmpty() || time == null || time <= 0){
                Toast.makeText(this,"Campos Inválidos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val entry = Quiz(title = title, description = description, time = time)

            Daos(this).quiz.insert(entry)

            this.setResult(Activity.RESULT_OK,Intent())

            this.finish()

        }


    }
}