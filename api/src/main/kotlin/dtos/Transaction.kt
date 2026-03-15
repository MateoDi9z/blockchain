package api.dtos

data class Transaction(
    val from: String,
    val to: String,
    val amount: Long,
    val signature: String,
)
