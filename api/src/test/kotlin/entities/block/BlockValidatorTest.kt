package entities.block

import api.entities.transaction.rules.PositiveAmountRule
import entities.block.rule.BlockValidatorFactory
import entities.transaction.validator.TransactionValidator
import testutils.TestBuilders
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BlockValidatorTest {

    private val txValidator = TransactionValidator(listOf(PositiveAmountRule()))
    private val difficulty = 2
    private val blockValidator = BlockValidatorFactory.createDefault()

    @Test
    fun validateReceivedBlock_returnsValid_whenEverythingIsCorrect() {
        val prevBlock =
            BlockMiner.mine(TestBuilders.makeBlock(index = 1, timestamp = 1000L), difficulty)

        val validTx = TestBuilders.makeTransaction(amount = 10L)
        val unminedNewBlock =
            TestBuilders.makeBlock(
                index = 2,
                timestamp = 2000L,
                previousHash = prevBlock.hash,
                transactions = listOf(validTx),
            )
        val newBlock = BlockMiner.mine(unminedNewBlock, difficulty)

        val result =
            blockValidator.validateReceivedBlock(
                block = newBlock,
                difficulty = difficulty,
                previousBlock = prevBlock,
                txValidator = txValidator,
            )

        assertTrue(result.isValid, "The block should be accepted")
        assertTrue(result.errorList.isEmpty())
    }

    @Test
    fun validateReceivedBlock_returnsCombinedErrors_fromBlockAndTransactions() {
        val prevBlock =
            BlockMiner.mine(TestBuilders.makeBlock(index = 1, timestamp = 5000L), difficulty)

        val invalidTx = TestBuilders.makeTransaction(amount = -50L)

        val badBlock =
            Block(
                index = 2,
                timestamp = 1000L,
                transactions = listOf(invalidTx),
                previousHash = "wrong_hash",
                hash = "bad_hash",
                nonce = 0L,
            )

        val result =
            blockValidator.validateReceivedBlock(
                block = badBlock,
                difficulty = difficulty,
                previousBlock = prevBlock,
                txValidator = txValidator,
            )

        assertFalse(result.isValid)
        assertTrue(result.errorList.size >= 5, "Should collect both block and transaction errors")
        assertTrue(result.errorList.any { it.contains("amount") }, "Missing transaction error")
    }
}
