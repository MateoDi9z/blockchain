package dtos

import api.dtos.Transaction

data class BlockDTO(
    val index: Int,
    val timestamp: Long,
    val transactions: List<Transaction>,
    val previousHash: String,
    val hash: String,
    val nonce: Long,
)
