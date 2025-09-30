package com.quraanali.pos.data

import com.quraanali.pos.AppDatabase

interface HomeLocal {
    suspend fun getNextOrderLocalId(): Int
    suspend fun saveOrderLocally(localId: String, payload: String)
    suspend fun getUnsyncedOrders(): List<comquraanalipos.Orders>
    suspend fun markAsSynced(localId: String)
}

class HomeLocalImpl(
    db: AppDatabase,
) : HomeLocal {
    private val queries = db.appDatabaseQueries

    override suspend fun getNextOrderLocalId(): Int {
        queries.insertInitialLocalId()

        val current = queries.selectNextLocalId().executeAsOne().toInt()
        queries.updateOrderLocalId()
        return current
    }


    override suspend fun saveOrderLocally(localId: String, payload: String) {
        queries.insertOrder(localId, payload, 0)
    }

    override suspend fun getUnsyncedOrders(): List<comquraanalipos.Orders> {
        return queries.selectUnsynced().executeAsList()
    }

    override suspend fun markAsSynced(localId: String) {
        queries.updateSynced(localId)
    }
}
