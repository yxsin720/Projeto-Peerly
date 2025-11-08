data class SessionDto(
    val id: String,
    val title: String,
    val description: String?,
    val startsAt: String,
    val endsAt: String,
    val status: String,
    val priceTotalCents: Int,
    val tutor: TutorSlimDto
)

data class TutorSlimDto(
    val id: String,
    val name: String,
    val avatarUrl: String?
)
