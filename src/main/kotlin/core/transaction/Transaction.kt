package org.example.core.transaction

import org.example.crypto.hash.Hash
import kotlin.math.pow

// Usamos 6 decimales en el LONG
// => valor real = Long / 10^6
val DECIMALS = 6

data class Transaction (
    val from: String,
    val to: String,
    val value: Long,
    val timestamp: Long = System.currentTimeMillis(),
) {
    constructor(
        from: String,
        to: String,
        realValue: Double,
        timestamp: Long = System.currentTimeMillis()
    ) : this(
        from,
        to,
        (realValue * 10.0.pow(DECIMALS)).toLong(),
        timestamp
    )


    fun hash() = Hash.sha256(serialize())

    private fun serialize(): String {
        return "$from|$to|$value|$timestamp"
    }

    fun getRealValue(): Double = value / 10.0.pow(DECIMALS);
}