package entities.block.rule

import entities.block.Block
import entities.results.ValidationResult

class TimestampRule : BlockRule {

    override fun validate(
        block: Block,
        difficulty: Int,
        previousBlock: Block,
    ): ValidationResult {
        val isValid = block.timestamp > previousBlock.timestamp

        return result(isValid)
    }

    private fun result(isValid: Boolean): ValidationResult =
        if (isValid) {
            ValidationResult(isValid = true, errorList = emptyList())
        } else {
            ValidationResult(
                isValid = false,
                errorList =
                    listOf(
                        "Validation Failed: Block timestamp must be strictly " +
                            "greater than the previous block's timestamp.",
                    ),
            )
        }
}
