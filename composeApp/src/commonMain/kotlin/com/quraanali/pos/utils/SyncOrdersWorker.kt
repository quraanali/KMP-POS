package com.quraanali.pos.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.quraanali.pos.data.HomeRepository
import com.quraanali.pos.domain.sync.MarkOrderAsSyncedUseCase
import com.quraanali.pos.domain.sync.SyncOrderUseCase

class SyncOrdersWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val homeRepository: HomeRepository,
    private val syncOrderUseCase: SyncOrderUseCase,
    private val markOrderAsSyncedUseCase: MarkOrderAsSyncedUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val unsyncedOrders = homeRepository.getUnsyncedOrders()
        unsyncedOrders.forEach { order ->
            try {
                val isSuccess = syncOrderUseCase(order.payload)

                if (isSuccess)
                    markOrderAsSyncedUseCase(order.localId)
            } catch (e: Exception) {
                e.printStackTrace()
                return Result.retry()
            }
        }
        return Result.success()
    }
}