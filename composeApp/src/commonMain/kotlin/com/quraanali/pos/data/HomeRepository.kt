package com.quraanali.pos.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeRepository(
    private val homeApi: HomeApi,
    private val homeStorage: HomeStorage,
) {
    private val scope = CoroutineScope(SupervisorJob())

    fun initialize() {
        scope.launch {
            refresh()
        }
    }

    suspend fun refresh() {
        homeStorage.saveObjects(homeApi.getData())
    }


    suspend fun getNextOrderLocalId(): Int = homeStorage.getNextOrderLocalId()
}
