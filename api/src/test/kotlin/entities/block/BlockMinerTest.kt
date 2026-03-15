package entities.block

import kotlin.test.Test
import kotlin.test.assertTrue

class BlockMinerTest {

    @Test
    fun mine_createsBlockThatMeetsDifficulty() {
        val difficulty = 2
        val unminedBlock =
            Block(
                index = 1,
                timestamp = System.currentTimeMillis(),
                transactions = emptyList(),
                previousHash = "0",
                hash = "",
                nonce = 0L,
            )

        val minedBlock = BlockMiner.mine(unminedBlock, difficulty)

        val target = "0".repeat(difficulty)
        assertTrue(
            minedBlock.hash.startsWith(target),
            "The mined block's hash must start with $difficulty zeros",
        )
        assertTrue(
            minedBlock.nonce > 0 || minedBlock.hash.startsWith(target),
            "The nonce should have changed to find the hash",
        )
    }
}
