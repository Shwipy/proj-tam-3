package estgoh.andre.tam_proj.DataBase.Question

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

import estgoh.andre.tam_proj.DataBase.Quiz.Quiz
@Entity(tableName = "questions",
        foreignKeys = [
            ForeignKey(
                entity = Quiz::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("quizId"),
                onDelete = ForeignKey.CASCADE)])

data class Question(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(index = true)
    val quizId: Long,

    var question: String,
    var correctAnswer: Int,
    var answer1: String,
    var answer2: String,
    var answer3: String? = null,
    var answer4: String? = null,
    var img: String? = null
)

