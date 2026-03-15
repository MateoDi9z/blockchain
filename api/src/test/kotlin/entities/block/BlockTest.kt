package entities.block

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BlockTest {

    @Test
    fun calculateHash_isDeterministic_forSameInput() {
        val now = System.currentTimeMillis()

        val blockA =
            Block(
                index = 1,
                timestamp = now,
                transactions = emptyList(),
                previousHash = "0",
                nonce = 0L,
            )
        val blockB =
            Block(
                index = 1,
                timestamp = now,
                transactions = emptyList(),
                previousHash = "0",
                nonce = 0L,
            )

        assertEquals(blockA.calculateHash(), blockB.calculateHash())
    }

    @Test
    fun calculateHash_changes_whenNonceChanges() {
        val block =
            Block(
                index = 1,
                timestamp = System.currentTimeMillis(),
                transactions = emptyList(),
                previousHash = "0",
                nonce = 0L,
            )

        val hash1 = block.calculateHash(nonceToUse = 0L)
        val hash2 = block.calculateHash(nonceToUse = 1L)

        assertNotEquals(hash1, hash2)
    }
}
