package entities.block

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertEquals
import testutils.TestBuilders

class BlockTest {

    @Test
    fun mineBlock_createsHashMeetingDifficulty() {
        val difficulty = 3
        val block = TestBuilders.makeBlock(index = 1, previousHash = "0")

        val minedBlock = block.mineBlock(difficulty)

        assertTrue(minedBlock.hash.startsWith("0".repeat(difficulty)))
        assertTrue(minedBlock.isValid(difficulty))
    }

    @Test
    fun isValid_returnsFalse_whenHashTampered() {
        val difficulty = 2
        val block =
            TestBuilders.makeMinedBlock(
                difficulty = difficulty,
                index = 1,
                previousHash = "0",
            )

        val tamperedBlock =
            Block(
                index = block.index,
                timestamp = block.timestamp,
                transactions = block.transactions,
                previousHash = block.previousHash,
                hash = "corrupted_hash",
                nonce = block.nonce,
            )

        assertFalse(tamperedBlock.isValid(difficulty))
    }

    @Test
    fun isValid_returnsFalse_whenDifficultyNotMetEvenIfHashMatches() {
        val difficulty = 2
        val block = TestBuilders.makeBlock(index = 2, previousHash = "0")

        val calc = block.calculateHash()

        val unminedButHashedBlock =
            Block(
                index = block.index,
                timestamp = block.timestamp,
                transactions = block.transactions,
                previousHash = block.previousHash,
                hash = calc,
                nonce = block.nonce,
            )

        assertFalse(unminedButHashedBlock.isValid(difficulty))
    }

    @Test
    fun calculateHash_isDeterministic_forSameInput() {
        val now = System.currentTimeMillis()
        val txs = emptyList<api.dtos.Transaction>()

        val a =
            TestBuilders.makeBlock(
                index = 1,
                timestamp = now,
                transactions = txs,
                previousHash = "0",
                nonce = 0L,
            )
        val b =
            TestBuilders.makeBlock(
                index = 1,
                timestamp = now,
                transactions = txs,
                previousHash = "0",
                nonce = 0L,
            )

        assertEquals(a.calculateHash(), b.calculateHash())
    }
}
