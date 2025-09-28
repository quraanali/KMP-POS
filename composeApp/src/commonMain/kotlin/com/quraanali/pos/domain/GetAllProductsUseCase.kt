package com.quraanali.pos.domain

import android.app.Application
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quraanali.pos.R
import com.quraanali.pos.screens.home.Product
import java.io.InputStream
import java.io.InputStreamReader

class GetAllProductsUseCase(private val application: Application) {
    operator fun invoke(query: String? = null): MutableList<Product> {
        val inputStream: InputStream = application.resources.openRawResource(R.raw.products)
        val reader = InputStreamReader(inputStream)
        val gson = Gson()
        val productListType = object : TypeToken<List<Product>>() {}.type
        val products: MutableList<Product> = gson.fromJson(reader, productListType)
        reader.close()

        return products
    }
}