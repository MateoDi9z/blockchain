package entities.block

import entities.block.rule.BlockIntegrityRule
import entities.block.rule.ChainLinkRule
import testutils.TestBuilders
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BlockRulesTest {

    @Test
    fun chainLinkRule_returnsTrue_whenPreviousHashMatches() {
        val difficulty = 2
        val prevBlock = TestBuilders.makeMinedBlock(difficulty, index = 1, previousHash = "0")

        val newBlock =
            TestBuilders.makeMinedBlock(
                difficulty,
                index = 2,
                previousHash = prevBlock.hash,
            )

        val rule = ChainLinkRule()
        assertTrue(rule.isValid(newBlock, difficulty, prevBlock))
    }

    @Test
    fun chainLinkRule_returnsFalse_whenPreviousHashIsWrong() {
        val difficulty = 2
        val prevBlock = TestBuilders.makeMinedBlock(difficulty, index = 1, previousHash = "0")

        val newBlock =
            TestBuilders.makeMinedBlock(
                difficulty,
                index = 2,
                previousHash = "hash_inventado",
            )

        val rule = ChainLinkRule()
        assertFalse(rule.isValid(newBlock, difficulty, prevBlock))
    }

    @Test
    fun blockIntegrityRule_returnsTrue_forProperlyMinedBlock() {
        val difficulty = 2
        val block = TestBuilders.makeMinedBlock(difficulty, index = 1, previousHash = "0")
        val dummyPrevBlock = TestBuilders.makeBlock(index = 0) // No importa para esta regla

        val rule = BlockIntegrityRule()
        assertTrue(rule.isValid(block, difficulty, dummyPrevBlock))
    }

    @Test
    fun blockIntegrityRule_returnsFalse_forTamperedBlock() {
        val difficulty = 2
        val originalBlock = TestBuilders.makeMinedBlock(difficulty, index = 1, previousHash = "0")

        val tamperedBlock =
            Block(
                index = originalBlock.index,
                timestamp = originalBlock.timestamp,
                transactions = originalBlock.transactions,
                previousHash = originalBlock.previousHash,
                hash = "corrupted_hash",
                nonce = originalBlock.nonce,
            )
        val dummyPrevBlock = TestBuilders.makeBlock(index = 0)

        val rule = BlockIntegrityRule()
        assertFalse(rule.isValid(tamperedBlock, difficulty, dummyPrevBlock))
    }
}
