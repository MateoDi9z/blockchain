package testutils

import api.dtos.Transaction
import dtos.BlockDTO
import entities.block.Block

object TestBuilders {

    fun makeTransaction(
        from: String = "a",
        to: String = "b",
        amount: Long = 1L,
        signature: String = "sig",
    ): Transaction = Transaction(from = from, to = to, amount = amount, signature = signature)

    fun makeBlock(
        index: Int = 1,
        timestamp: Long = System.currentTimeMillis(),
        transactions: List<Transaction> = emptyList(),
        previousHash: String = "0",
        hash: String = "",
        nonce: Long = 0L,
    ): Block =
        Block(
            index = index,
            timestamp = timestamp,
            transactions = transactions,
            previousHash = previousHash,
            hash = hash,
            nonce = nonce,
        )

    fun makeMinedBlock(
        difficulty: Int,
        index: Int = 1,
        previousHash: String = "0",
        transactions: List<Transaction> = emptyList(),
    ): Block {
        val b = makeBlock(index = index, transactions = transactions, previousHash = previousHash)
        return b.mineBlock(difficulty)
    }

    fun toDto(block: Block): BlockDTO =
        BlockDTO(
            index = block.index,
            timestamp = block.timestamp,
            transactions = block.transactions,
            previousHash = block.previousHash,
            hash = block.hash,
            nonce = block.nonce,
        )
}
