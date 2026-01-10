package estgoh.andre.tam_proj.DataBase.Quiz

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface QuizDao {
    @Insert
    fun insert(quiz: Quiz): Long

    @Update
    fun update(quiz: Quiz): Int

    @Query("SELECT * FROM quizes WHERE id = :quizId")
    fun getQuiz(quizId: Long): Quiz

    @Query("SELECT * FROM quizes")
    fun getAll(): List<Quiz>

    @Query("DELETE FROM quizes WHERE id = :quizId")
    fun delete(quizId: Long): Int
}