package entities.transaction.rules

data class ValidationResult(
    val isValid: Boolean,
    val errorList: List<String>,
) {
    // Mantenemos tu lógica original para obtener el string formateado
    fun getErrors(): String =
        if (errorList.isNotEmpty()) {
            errorList.joinToString(separator = " | ")
        } else {
            "No errors found"
        }
}
