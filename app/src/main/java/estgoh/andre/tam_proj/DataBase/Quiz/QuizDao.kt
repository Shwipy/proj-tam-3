//package estgoh.andre.tam_proj.DataBase.Quiz
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.Query
//import androidx.room.Update
//
//@Dao
//interface QuizDao {
//    @Insert
//    fun insert(quizRoom: QuizRoom): Long
//
//    @Update
//    fun update(quizRoom: QuizRoom): Int
//
//    @Query("SELECT * FROM quizes WHERE id = :quizId")
//    fun getQuiz(quizId: Long): QuizRoom
//
//    @Query("SELECT * FROM quizes")
//    fun getAll(): List<QuizRoom>
//
//    @Query("DELETE FROM quizes WHERE id = :quizId")
//    fun delete(quizId: Long): Int
//}