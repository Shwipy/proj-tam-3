//package estgoh.andre.tam_proj.DataBase
//
//import android.content.Context
//import androidx.room.Database
//
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import estgoh.andre.tam_proj.DataBase.Question.QuestionRoom
//import estgoh.andre.tam_proj.DataBase.Question.QuestionDao
//import estgoh.andre.tam_proj.DataBase.Quiz.QuizRoom
//import estgoh.andre.tam_proj.DataBase.Quiz.QuizDao
//
//@Database(entities = [QuizRoom::class, QuestionRoom::class], version = 1)
//abstract class QuizDatabase : RoomDatabase() {
//
//    abstract fun quizDao(): QuizDao
//    abstract fun questionDao(): QuestionDao
//
//    companion object {
//        @Volatile private var INSTANCE: QuizDatabase? = null
//
//        fun getInstance(context: Context): QuizDatabase{
//            var tempInstance = INSTANCE
//            if(tempInstance != null){
//                return tempInstance
//            }
//            synchronized(this){
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    QuizDatabase::class.java,
//                    "QuizDatabase"
//                ).allowMainThreadQueries().build()
//                INSTANCE = instance
//                return instance
//            }
//        }
//    }
//}