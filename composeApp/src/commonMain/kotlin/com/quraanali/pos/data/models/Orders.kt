package com.quraanali.pos.data.models

data class Orders(
    val id: Long,
    val localId: String,
    val payload: String,
    val synced: Long
)