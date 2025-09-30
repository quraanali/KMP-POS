package com.quraanali.pos.domain.sync

import android.util.Log
import com.quraanali.pos.data.HomeRepository


class MarkOrderAsSyncedUseCase(
    private val homeRepository: HomeRepository,
) {
    suspend operator fun invoke(localId: String) {
        Log.d("Syncing", "MarkOrderAsSyncedUseCase")
        return homeRepository.markAsSynced(localId)
    }
}