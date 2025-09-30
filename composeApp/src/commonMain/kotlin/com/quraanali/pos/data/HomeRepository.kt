package com.quraanali.pos.data

import com.quraanali.pos.data.models.ProductObject
import comquraanalipos.Orders

class HomeRepository(
    private val homeRemote: HomeRemote,
    private val homeLocal: HomeLocal,
) : HomeRemote, HomeLocal {
    override suspend fun getNextOrderLocalId(): Int = homeLocal.getNextOrderLocalId()


    override suspend fun getData(): List<ProductObject> {
        return homeRemote.getData()
    }

    override suspend fun syncOrders(payload: String): Boolean {
        return homeRemote.syncOrders(payload)
    }

    override suspend fun createOrder(payload: String): Boolean {
        return homeRemote.createOrder(payload)
    }

    override suspend fun saveOrderLocally(localId: String, payload: String) {
        homeLocal.saveOrderLocally(localId, payload)
    }

    override suspend fun getUnsyncedOrders(): List<Orders> {
        return homeLocal.getUnsyncedOrders()
    }

    override suspend fun markAsSynced(localId: String) {
        homeLocal.getUnsyncedOrders()
    }
}
