package entities.block

import dtos.BlockDTO

object BlockMapper {
    fun toEntity(dto: BlockDTO): Block =
        Block(
            index = dto.index,
            timestamp = dto.timestamp,
            transactions = dto.transactions,
            previousHash = dto.previousHash,
            hash = dto.hash,
            nonce = dto.nonce,
        )
}
