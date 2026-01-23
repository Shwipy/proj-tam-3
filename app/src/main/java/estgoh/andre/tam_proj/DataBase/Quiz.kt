package estgoh.andre.tam_proj.DataBase

data class Quiz(
    val id: Long = 0,
    val title: String = "",
    val description: String = "",
    val duration: Int = 0,
    val playerId: Long = 0,
    val owned: Boolean = false
)

