package com.quraanali.pos.domain.sync

import android.util.Log


class SyncAllOrdersUseCase(
    private val getUnsyncedOrdersUseCase: GetUnsyncedOrdersUseCase,
    private val syncOrderUseCase: SyncOrderUseCase,
    private val markOrderAsSyncedUseCase: MarkOrderAsSyncedUseCase,
) {
    suspend operator fun invoke() {

        val unsyncedOrders = getUnsyncedOrdersUseCase()

        unsyncedOrders.forEach { order ->
            try {
                val isSuccess = syncOrderUseCase(order.payload)

                if (isSuccess) markOrderAsSyncedUseCase(order.localId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        Log.d("Syncing", "SyncAllOrdersUseCase")
    }
}