package org.example.core.transaction

import org.example.crypto.hash.Hash
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow

// Usamos 6 decimales en el LONG
// => valor real = Long / 10^6
private const val DECIMALS = 6

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
        BigDecimal
            .valueOf(realValue)
            .setScale(DECIMALS, RoundingMode.HALF_UP)
            .unscaledValue()
            .longValueExact(),
        timestamp
    )


    fun hash() = Hash.sha256(serialize())

    private fun serialize(): String {
        return "$from|$to|$value|$timestamp"
    }

    fun getRealValue(): Double = value / 10.0.pow(DECIMALS);
}