package com.quraanali.pos.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface HomeStorage {
    suspend fun saveObjects(newObjects: List<ProductObject>)

    fun getObjectById(objectId: Int): Flow<ProductObject?>

    fun getObjects(): Flow<List<ProductObject>>

    suspend fun getNextOrderLocalId(): Int
}

class InMemoryHomeStorage : HomeStorage {
    private val storedObjects = MutableStateFlow(emptyList<ProductObject>())

    override suspend fun saveObjects(newObjects: List<ProductObject>) {
        storedObjects.value = newObjects
    }

    override fun getObjectById(objectId: Int): Flow<ProductObject?> {
        return storedObjects.map { objects ->
            objects.find { it.objectID == objectId }
        }
    }

    override fun getObjects(): Flow<List<ProductObject>> = storedObjects

    private val storedNextOrderLocalId = MutableStateFlow(0)

    override suspend fun getNextOrderLocalId(): Int {
        storedNextOrderLocalId.value++
        return storedNextOrderLocalId.value
    }
}
