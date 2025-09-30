package com.quraanali.pos.domain.sync

import android.util.Log
import com.quraanali.pos.data.HomeRepository
import comquraanalipos.Orders


class GetUnsyncedOrdersUseCase(
    private val homeRepository: HomeRepository,
) {
    suspend operator fun invoke(): List<Orders> {
        return homeRepository.getUnsyncedOrders()
        Log.d("Syncing", "GetUnsyncedOrdersUseCase")

    }
}