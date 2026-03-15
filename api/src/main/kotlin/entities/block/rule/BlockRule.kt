package entities.block.rule

import entities.block.Block

interface BlockRule {
    fun isValid(
        block: Block,
        difficulty: Int,
        previousBlock: Block,
    ): Boolean

    fun getErrorMessage(): String
}
