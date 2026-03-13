package api.entities.rules

import api.entities.Transaction

interface TransactionRule {
     fun isValid(transaction: Transaction): Boolean
     fun getErrorMessage(): String

}

