package com.quraanali.pos.di

import android.content.Context
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.quraanali.pos.AppDatabase
import com.quraanali.pos.data.HomeLocal
import com.quraanali.pos.data.HomeLocalImpl
import com.quraanali.pos.data.HomeRemote
import com.quraanali.pos.data.HomeRemoteImpl
import com.quraanali.pos.data.HomeRepository
import com.quraanali.pos.screens.detail.DetailViewModel
import com.quraanali.pos.screens.home.HomeViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {
    single {
        val json = Json { ignoreUnknownKeys = true }
        HttpClient {
            install(ContentNegotiation) {
                json(json, contentType = ContentType.Any)
            }
        }
    }

    single<HomeRemote> { HomeRemoteImpl(get()) }
    single<HomeLocal> { HomeLocalImpl(get()) }
    single {
        HomeRepository(get(), get())
    }

    single<SqlDriver> {
        AndroidSqliteDriver(AppDatabase.Schema.synchronous(), get(), "AppDatabase.db")
    }


    single { AppDatabase(get()) }
}

val viewModelModule = module {
    factoryOf(::HomeViewModel)
    factoryOf(::DetailViewModel)

}

fun initKoin(app: Context) {
    startKoin {
        androidContext(app)
        workManagerFactory()
        modules(
            dataModule,
            viewModelModule,
            commonDomainModule
        )
    }
}
