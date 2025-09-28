package com.quraanali.pos

import android.app.Application
import com.quraanali.pos.di.initKoin

class PosAqApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(this@PosAqApp)
    }
}
