package com.quraanali.pos.domain.cart

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.provider.Settings
import java.util.UUID

class GetDeviceUniqueIdUseCase() {

    @SuppressLint("HardwareIds")
    operator fun invoke(application: Application): String {
        return Settings.Secure.getString(
            application.contentResolver,
            Settings.Secure.ANDROID_ID
        )?.let {
            UUID.nameUUIDFromBytes((it + application.packageName).toByteArray()).toString()
        } ?: run {
            val pseudoId =
                Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10
            UUID.nameUUIDFromBytes((pseudoId.toString() + application.packageName).toByteArray())
                .toString()
        }
    }

}