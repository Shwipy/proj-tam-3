package estgoh.andre.tam_proj.DataBase.Quiz

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quizes")
data class Quiz(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    var title: String,
    var description: String,
    var time: Int
)
