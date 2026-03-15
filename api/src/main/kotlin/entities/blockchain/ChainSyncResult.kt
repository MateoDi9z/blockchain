package entities.blockchain

sealed class ChainSyncResult {
    object Success : ChainSyncResult()

    data class Rejected(
        val reason: String,
    ) : ChainSyncResult()
}
