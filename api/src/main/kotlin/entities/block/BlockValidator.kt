package entities.block

import dtos.BlockDTO
import entities.transaction.validator.TransactionValidator
import entities.block.rule.BlockIntegrityRule
import entities.block.rule.ChainLinkRule
import entities.block.rule.CompositeBlockRule

object BlockValidator {
    fun validateReceivedBlock(
        dto: BlockDTO,
        difficulty: Int,
        previousBlock: Block,
        txValidator: TransactionValidator,
    ): Boolean {
        val block = mapToEntity(dto)
        val rules = listOf(ChainLinkRule(), BlockIntegrityRule())

        return verifyTransactions(block, txValidator) &&
            CompositeBlockRule(rules).isValid(block, difficulty, previousBlock)
    }

    private fun mapToEntity(dto: BlockDTO) =
        Block(
            index = dto.index,
            timestamp = dto.timestamp,
            transactions = dto.transactions,
            previousHash = dto.previousHash,
            hash = dto.hash,
            nonce = dto.nonce,
        )

    private fun verifyTransactions(
        block: Block,
        validator: TransactionValidator,
    ): Boolean {
        val invalidResults =
            block.transactions
                .map { validator.validate(it) }
                .filter { !it.isValid }

        return invalidResults.isEmpty()
    }
}
