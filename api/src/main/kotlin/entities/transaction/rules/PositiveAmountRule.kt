package api.entities.transaction.rules

import api.entities.transaction.Transaction

class PositiveAmountRule : TransactionRule {
    override fun isValid(transaction: Transaction): Boolean = transaction.amount > 0.0

    override fun getErrorMessage() = "The amount of transactions must be positive"
}
