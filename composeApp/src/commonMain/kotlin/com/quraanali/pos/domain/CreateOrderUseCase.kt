package com.quraanali.pos.domain

import com.quraanali.pos.data.HomeRepository
import kotlinx.coroutines.delay


class CreateOrderUseCase(
    private val homeRepository: HomeRepository,
    private val getNextOrderLocalIdUseCase: GetNextOrderLocalIdUseCase,
    getDeviceUniqueIdUseCase: GetDeviceUniqueIdUseCase,
) {
    suspend operator fun invoke(): Boolean {
        // make some api call to create order if u online
        // save order locally if u offline
        delay(2000)
        return System.currentTimeMillis().toInt() % 2 == 0
    }
}