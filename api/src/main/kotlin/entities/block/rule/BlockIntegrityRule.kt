package entities.block.rule

import entities.block.Block

class BlockIntegrityRule : BlockRule {
    override fun isValid(
        block: Block,
        difficulty: Int,
        previousBlock: Block,
    ) = block.isValid(difficulty)

    override fun getErrorMessage() = "Validation Failed: Data integrity or Difficulty not met."
}
