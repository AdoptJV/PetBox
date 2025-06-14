import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val from: String,
    val to: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
