package api.entities

import entities.block.Block

class Blockchain(
    val chain: List<Block>,
    val difficulty: Int,
)
