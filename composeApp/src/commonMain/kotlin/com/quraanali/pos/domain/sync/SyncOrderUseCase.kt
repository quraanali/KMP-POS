package com.quraanali.pos.domain.sync

import android.util.Log
import com.quraanali.pos.data.HomeRepository


class SyncOrderUseCase(
    private val homeRepository: HomeRepository,
) {
    suspend operator fun invoke(payload: String): Boolean {
        Log.d("Syncing", "SyncOrderUseCase")

        return homeRepository.syncOrders(payload)
    }
}