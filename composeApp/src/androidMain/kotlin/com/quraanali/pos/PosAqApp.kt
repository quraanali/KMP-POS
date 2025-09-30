package com.quraanali.pos

import android.app.Application
import androidx.work.Configuration
import com.quraanali.pos.di.initKoin
import org.koin.androidx.workmanager.factory.KoinWorkerFactory

class PosAqApp : Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
        initKoin(this@PosAqApp)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(KoinWorkerFactory())
            .build()

}
