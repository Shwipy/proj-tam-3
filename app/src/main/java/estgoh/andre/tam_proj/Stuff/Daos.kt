package estgoh.andre.tam_proj.Stuff

import android.content.Context
import estgoh.andre.tam_proj.DataBase.QuizDatabase

class Daos(val context: Context) {
    val quiz = QuizDatabase.getInstance(context).quizDao()
    val question = QuizDatabase.getInstance(context).questionDao()
}