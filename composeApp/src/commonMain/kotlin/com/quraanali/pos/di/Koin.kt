package com.quraanali.pos.di

import android.content.Context
import com.quraanali.pos.data.HomeApi
import com.quraanali.pos.data.HomeRepository
import com.quraanali.pos.data.HomeStorage
import com.quraanali.pos.data.InMemoryHomeStorage
import com.quraanali.pos.data.KtorHomeApi
import com.quraanali.pos.screens.detail.DetailViewModel
import com.quraanali.pos.screens.home.HomeViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
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

    single<HomeApi> { KtorHomeApi(get()) }
    single<HomeStorage> { InMemoryHomeStorage() }
    single {
        HomeRepository(get(), get()).apply {
            initialize()
        }
    }
}

val viewModelModule = module {
    factoryOf(::HomeViewModel)
    factoryOf(::DetailViewModel)

}

fun initKoin(app: Context) {
    startKoin {
        androidContext(app)
        modules(
            dataModule,
            viewModelModule,
            commonDomainModule
        )
    }
}
