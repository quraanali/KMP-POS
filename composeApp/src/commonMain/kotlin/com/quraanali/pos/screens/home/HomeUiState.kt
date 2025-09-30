package com.quraanali.pos.screens.home

import com.quraanali.pos.data.models.Product
import com.quraanali.pos.data.models.ProductObject

data class HomeUiState(
    val isLoading: Boolean = true,
    val isConnected: Boolean = false,
    val errorMessage: String? = null,
    val productList: MutableList<ProductObject> = mutableListOf(),
    val productsList: MutableList<Product> = mutableListOf(),
    val selectedProductList: MutableList<SelectedProduct> = mutableListOf(),
    val total: String? = null,
    val discount: String? = null,
)
