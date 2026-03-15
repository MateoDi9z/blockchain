package entities.block

import entities.block.rule.ChainLinkRule
import entities.block.rule.CompositeBlockRule
import entities.block.rule.ProofOfWorkRule
import entities.block.rule.TimestampRule
import entities.block.rule.ValidHashRule
import testutils.TestBuilders
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class BlockRulesTest {

    private val difficulty = 2
    private val dummyPrevBlock = TestBuilders.makeBlock(index = 0, timestamp = 1000L)

    // --- ChainLinkRule ---
    @Test
    fun chainLinkRule_returnsValid_whenPreviousHashMatches() {
        val prevBlock = TestBuilders.makeBlock(index = 1, hash = "previous_hash")
        val newBlock = TestBuilders.makeBlock(index = 2, previousHash = "previous_hash")

        val result = ChainLinkRule().validate(newBlock, difficulty, prevBlock)
        assertTrue(result.isValid)
    }

    @Test
    fun chainLinkRule_returnsInvalid_whenPreviousHashIsWrong() {
        val prevBlock = TestBuilders.makeBlock(index = 1, hash = "real_hash")
        val newBlock = TestBuilders.makeBlock(index = 2, previousHash = "fake_hash")

        val result = ChainLinkRule().validate(newBlock, difficulty, prevBlock)
        assertFalse(result.isValid)
        assertTrue(result.errorList.any { it.contains("Previous hash mismatch") })
    }

    // --- ValidHashRule ---
    @Test
    fun validHashRule_returnsValid_whenHashIsUntampered() {
        val block = BlockMiner.mine(TestBuilders.makeBlock(index = 1), difficulty)
        val result = ValidHashRule().validate(block, difficulty, dummyPrevBlock)
        assertTrue(result.isValid)
    }

    @Test
    fun validHashRule_returnsInvalid_whenDataIsTampered() {
        val validBlock = BlockMiner.mine(TestBuilders.makeBlock(index = 1), difficulty)
        // We modify a field without re-mining
        val tamperedBlock = validBlock.copy(index = 999)

        val result = ValidHashRule().validate(tamperedBlock, difficulty, dummyPrevBlock)
        assertFalse(result.isValid)
        assertTrue(result.errorList.any { it.contains("tampered with") })
    }

    // --- ProofOfWorkRule ---
    @Test
    fun proofOfWorkRule_returnsValid_whenDifficultyIsMet() {
        val block = BlockMiner.mine(TestBuilders.makeBlock(index = 1), difficulty)
        val result = ProofOfWorkRule().validate(block, difficulty, dummyPrevBlock)
        assertTrue(result.isValid)
    }

    @Test
    fun proofOfWorkRule_returnsInvalid_whenDifficultyNotMet() {
        val block = TestBuilders.makeBlock(index = 1, hash = "0xHasNoZeros")
        val result = ProofOfWorkRule().validate(block, difficulty, dummyPrevBlock)
        assertFalse(result.isValid)
        assertTrue(result.errorList.any { it.contains("required difficulty") })
    }

    // --- TimestampRule ---
    @Test
    fun timestampRule_returnsValid_whenTimeFlowsForward() {
        val prevBlock = TestBuilders.makeBlock(timestamp = 1000L)
        val newBlock = TestBuilders.makeBlock(timestamp = 2000L)
        val result = TimestampRule().validate(newBlock, difficulty, prevBlock)
        assertTrue(result.isValid)
    }

    @Test
    fun timestampRule_returnsInvalid_whenTimeGoesBackwards() {
        val prevBlock = TestBuilders.makeBlock(timestamp = 2000L)
        val newBlock = TestBuilders.makeBlock(timestamp = 1000L)
        val result = TimestampRule().validate(newBlock, difficulty, prevBlock)
        assertFalse(result.isValid)
    }

    @Test
    fun compositeRule_aggregatesAllErrors_whenMultipleRulesFail() {
        val prevBlock = TestBuilders.makeBlock(index = 1, hash = "hash1", timestamp = 5000L)

        val badBlock =
            Block(
                index = 2,
                timestamp = 1000L,
                transactions = emptyList(),
                previousHash = "wrong_hash",
                hash = "invalid_hash_no_zeroes",
                nonce = 0L,
            )

        val composite =
            CompositeBlockRule(
                listOf(
                    ChainLinkRule(),
                    ValidHashRule(),
                    ProofOfWorkRule(),
                    TimestampRule(),
                ),
            )

        val result = composite.validate(badBlock, difficulty, prevBlock)

        assertFalse(result.isValid)
        assertEquals(
            4,
            result.errorList.size,
            "Should capture exactly the 4 errors at once",
        )
    }
}
