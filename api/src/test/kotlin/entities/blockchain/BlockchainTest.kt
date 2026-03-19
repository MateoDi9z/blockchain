package entities.blockchain

import entities.results.OperationResult
import entities.transaction.validator.TransactionValidator
import entities.transaction.rules.PositiveAmountRule
import testutils.TestBuilders
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BlockchainTest {

    private val testDifficulty = 2
    private val txValidator = TransactionValidator(listOf(PositiveAmountRule()))

    private fun createTestBlockchain(): Blockchain {
        val pool = TransactionPool(txValidator)
        val ledger = Chain(testDifficulty, txValidator = txValidator)
        return Blockchain(testDifficulty, pool, ledger)
    }

    @Test
    fun addTransaction_delegatesToPool() {
        val blockchain = createTestBlockchain()
        val validTx = TestBuilders.makeTransaction(amount = 50L)

        val result = blockchain.addTransaction(validTx)

        assertTrue(result is OperationResult.Success)
        assertEquals(
            1,
            blockchain.pendingTransactions.size,
            "Transaction should be in pending list",
        )
    }
}
