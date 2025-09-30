package com.quraanali.pos.data

import com.quraanali.pos.data.models.ProductObject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.utils.io.CancellationException
import kotlin.random.Random

interface HomeRemote {
    suspend fun getData(): List<ProductObject>
    suspend fun syncOrders(payload: String): Boolean
    suspend fun createOrder(payload: String): Boolean
}

class HomeRemoteImpl(private val client: HttpClient) : HomeRemote {
    companion object {
        private const val API_URL =
            "https://raw.githubusercontent.com/Kotlin/KMP-App-Template/main/list.json"
    }

    override suspend fun getData(): List<ProductObject> {
        return try {
            client.get(API_URL).body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()

            emptyList()
        }
    }

    override suspend fun syncOrders(payload: String): Boolean {
        return Random.nextBoolean()
    }


    private var isFirstCall = true
    override suspend fun createOrder(payload: String): Boolean {
        if (isFirstCall) {
            isFirstCall = false
            return false
        }
        return Random.nextBoolean()
    }
}
