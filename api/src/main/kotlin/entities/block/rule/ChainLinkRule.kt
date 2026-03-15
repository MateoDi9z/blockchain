package entities.block.rule

import entities.block.Block

class ChainLinkRule : BlockRule {
    override fun isValid(
        block: Block,
        difficulty: Int,
        previousBlock: Block,
    ) = block.previousHash == previousBlock.hash

    override fun getErrorMessage() = "Validation Failed: Previous hash mismatch."
}
