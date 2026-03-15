package entities.block

import api.dtos.Transaction
import api.entities.hash.Hash

class Block(
    val index: Int,
    val timestamp: Long,
    val transactions: List<Transaction>,
    val previousHash: String,
    val hash: String = "",
    val nonce: Long = 0,
) {

    fun calculateHash(nonceToUse: Long = this.nonce): String {
        val dataToHash = "$index$timestamp$transactions$previousHash$nonceToUse"
        return Hash.sha256(dataToHash)
    }

    fun mineBlock(difficulty: Int): Block {
        val target = "0".repeat(difficulty)

        var currentNonce = 0L
        var currentHash = calculateHash(currentNonce)

        while (!currentHash.startsWith(target)) {
            currentNonce++
            currentHash = calculateHash(currentNonce)
        }

        return Block(index, timestamp, transactions, previousHash, currentHash, currentNonce)
    }

    fun isValid(difficulty: Int): Boolean {
        val computedHash = calculateHash()

        val target = "0".repeat(difficulty)

        val hasValidHash = (this.hash == computedHash)
        val meetsDifficulty = this.hash.startsWith(target)

        return hasValidHash && meetsDifficulty
    }
}
