package com.quraanali.pos.domain.cart

import android.app.Application
import com.quraanali.pos.data.HomeRepository
import com.quraanali.pos.utils.hasInternetConnection
import kotlinx.coroutines.delay

class CreateOrderUseCase(
    private val homeRepository: HomeRepository,
    private val getNextOrderLocalIdUseCase: GetNextOrderLocalIdUseCase,
    private val getDeviceUniqueIdUseCase: GetDeviceUniqueIdUseCase,

    ) {
    suspend operator fun invoke(application: Application, payload: String): Boolean {
        delay(2000)

        val orderId = getDeviceUniqueIdUseCase(application) + getNextOrderLocalIdUseCase()

        if (application.hasInternetConnection()) {
            // make some api call to create order if u online
            if (!homeRepository.syncOrders(payload)) {
                homeRepository.saveOrderLocally(orderId, payload)
                return false
            } else {
                return true
            }

        } else {
            // save order locally if u offline
            homeRepository.saveOrderLocally(orderId, payload)
            return false
        }
    }
}