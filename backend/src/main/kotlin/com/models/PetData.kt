import kotlinx.serialization.Serializable

@Serializable
data class PetDto(
    val id: Int,
    val name: String,
    val species: String,
    val age: Int,
    val sex: String,
    val castrated: Boolean,
    val photoUrl: String,
    val description: String,
    val owner: String,
    val ownerID: Int
)
