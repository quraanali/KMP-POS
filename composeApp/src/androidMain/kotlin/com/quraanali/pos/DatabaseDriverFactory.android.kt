package com.quraanali.pos

import android.content.Context
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.koin.mp.KoinPlatform

actual class DatabaseDriverFactory(private val context: Context) {
    actual suspend fun createDriver(): SqlDriver {


        return AndroidSqliteDriver(
            AppDatabase.Schema.synchronous(),
            KoinPlatform.getKoin().get(),
            "AppDatabase.db"
        )
    }
}