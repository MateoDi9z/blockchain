package org.example.core.block

data class Block (
    val previousHash: String,
    val merkleRoot: String,
    val timestamp: Long,
    val nonce: Long
)

