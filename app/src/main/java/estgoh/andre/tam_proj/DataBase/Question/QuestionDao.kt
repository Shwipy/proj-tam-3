//package estgoh.andre.tam_proj.DataBase.Question
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.Query
//import androidx.room.Update
//
//@Dao
//interface QuestionDao {
//    @Insert
//    fun insert(questionRoom: QuestionRoom): Long
//
//    @Update
//    fun update(questionRoom: QuestionRoom): Int
//
//    @Query("SELECT * FROM questions WHERE id = :questionId")
//    fun getQuestion(questionId: Long): QuestionRoom
//
//    @Query("SELECT * FROM questions WHERE quizId = :quizId")
//    fun getQuizQuestions(quizId: Long): List<QuestionRoom>
//
//    @Query("SELECT correctAnswer FROM questions WHERE question = :questionId")
//    fun getCorrectAnswer(questionId: Long): Int
//
//    @Query("DELETE FROM questions WHERE id = :questionId")
//    fun delete(questionId: Long): Int
//}