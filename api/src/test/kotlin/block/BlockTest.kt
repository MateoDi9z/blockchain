package block

import api.dtos.Transaction
import api.entities.Block
import api.entities.crypto.hash.Hash
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNotEquals

class BlockTest {

    @Test
    fun initialization_setsAllProperties() {
        val tx =
            Transaction(from = "0xRocio", to = "0xPedro", amount = 100.0f, signature = "0x....")
        val transactions = listOf(tx)
        val block =
            Block(
                index = 1,
                timestamp = 1700000000L,
                transactions = transactions,
                previousHash = "0x0081c4",
                hash = "",
            )

        assertEquals(1, block.index)
        assertEquals(1700000000L, block.timestamp)
        assertEquals(transactions, block.transactions)
        assertEquals("0x0081c4", block.previousHash)
    }

    @Test
    fun calculateHash_usesExternalHashObject() {
        val block = Block(1, 100L, emptyList(), "prev")
        val manualHash =
            Hash.sha256(
                "${block.index}${block.timestamp}${block.transactions}${block.previousHash}${block.nonce}",
            )

        assertEquals(
            manualHash,
            block.calculateHash(),
            "The block must use the Hash.sha256 logic",
        )
    }

    @Test
    fun hashChangesWhenDataIsModified() {
        val block = Block(1, 100L, emptyList(), "prev")
        val originalHash = block.hash

        block.nonce = 1L
        val newHash = block.calculateHash()

        assertNotEquals(originalHash, newHash, "If you change the nonce, the hash MUST change")
    }

    @Test
    fun mineBlock_satisfiesDifficulty() {
        val difficulty = 3
        val block = Block(2, 200L, emptyList(), "prevHash")

        block.mineBlock(difficulty)

        val target = "0".repeat(difficulty)
        assertTrue(block.hash.startsWith(target), "The mined hash must start with $target")
        assertTrue(block.nonce > 0, "The nonce must have been incremented to find the hash")
    }

    @Test
    fun genesisBlock_structure() {
        val genesis = Block(0, System.currentTimeMillis(), emptyList(), "0")

        assertEquals(0, genesis.index, "The genesis block usually has index 0")
        assertEquals(
            "0",
            genesis.previousHash,
            "The initial block does not have a real previous hash",
        )
    }
}
