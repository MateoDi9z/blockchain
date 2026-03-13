package org.example.crypto.merkle

import org.example.core.transaction.Transaction
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.sql.Timestamp

class MerkleTreeTest {

    private fun tx(a: String, b: String, amount: Double): Transaction {
        return Transaction(a, b, amount)
    }

    @Test
    fun emptyTreeReturnsEmptyRoot() {
        val tree = MerkleTree()
        tree.init(listOf())

        assertEquals("", tree.getRootHash())
    }

    @Test
    fun singleTransactionRootEqualsTxHash() {
        val t = tx("A", "B", 10.0)

        val tree = MerkleTree()
        tree.init(listOf(t))

        assertEquals(t.hash(), tree.getRootHash())
    }

    @Test
    fun twoTransactionsProduceRoot() {
        val txs = listOf(
            tx("A","B",10.0),
            tx("B","C",5.0)
        )

        val tree = MerkleTree()
        tree.init(txs)

        assertTrue(tree.getRootHash().isNotEmpty())
    }

    @Test
    fun oddNumberTransactionsHandledCorrectly() {
        val txs = listOf(
            tx("A","B",10.0),
            tx("B","C",5.0),
            tx("C","D",2.0)
        )

        val tree = MerkleTree()
        tree.init(txs)

        assertTrue(tree.getRootHash().isNotEmpty())
    }

    @Test
    fun deterministicRoot() {
        val txs = listOf(
            tx("A","B",10.0),
            tx("B","C",5.0),
            tx("C","D",2.0),
            tx("D","E",1.0)
        )

        val tree1 = MerkleTree()
        val tree2 = MerkleTree()

        tree1.init(txs)
        tree2.init(txs)

        assertEquals(tree1.getRootHash(), tree2.getRootHash())
    }

    @Test
    fun changingOrderChangesRoot() {
        val tx1 = tx("A","B",10.0)
        val tx2 = tx("B","C",5.0)

        val tree1 = MerkleTree()
        tree1.init(listOf(tx1, tx2))

        val tree2 = MerkleTree()
        tree2.init(listOf(tx2, tx1))

        assertNotEquals(tree1.getRootHash(), tree2.getRootHash())
    }

}