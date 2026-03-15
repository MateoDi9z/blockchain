package entities.block.rule

import entities.block.Block

class CompositeBlockRule(
    private val rules: List<BlockRule>,
) : BlockRule {
    private var lastError = ""

    override fun isValid(
        block: Block,
        difficulty: Int,
        previousBlock: Block,
    ): Boolean {
        for (rule in rules) {
            if (!rule.isValid(block, difficulty, previousBlock)) {
                lastError = rule.getErrorMessage()
                return false
            }
        }
        return true
    }

    override fun getErrorMessage(): String = lastError
}
