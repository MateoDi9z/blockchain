package api.entities.transaction.rules

import api.entities.transaction.Transaction

interface TransactionRule {
    fun isValid(transaction: Transaction): Boolean

    fun getErrorMessage(): String
}
