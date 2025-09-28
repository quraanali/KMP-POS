package com.quraanali.pos.screens.home

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("price")
    var price: Double,
    @SerializedName("stock")
    var stock: Int,
    @SerializedName("taxable")
    var taxable: Boolean,
    @SerializedName("thumb")
    var thumb: String
)

