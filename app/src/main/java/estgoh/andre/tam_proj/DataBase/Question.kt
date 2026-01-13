package estgoh.andre.tam_proj.DataBase

data class Question(
    val id: Long = 0,

    val question: String = "",
    val correct_answer: Int = 0,
    val answer1: String = "",
    val answer2: String = "",
    val answer3: String? = null,
    val answer4: String? = null,
    val img: String? = null,

    val quiz_id: Long = 0
)


