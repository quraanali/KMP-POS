package com.quraanali.pos.screens.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quraanali.pos.data.models.Product
import com.quraanali.pos.domain.cart.CalculateTotalAndDiscountUseCase
import com.quraanali.pos.domain.cart.CreateOrderUseCase
import com.quraanali.pos.domain.home.GetAllProductsUseCase
import com.quraanali.pos.domain.sync.SyncAllOrdersUseCase
import com.quraanali.pos.utils.NetworkMonitor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val application: Application,
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val calculateTotalAndDiscountUseCase: CalculateTotalAndDiscountUseCase,
    private val syncAllOrdersUseCase: SyncAllOrdersUseCase,
) : ViewModel() {
    private val networkMonitor = NetworkMonitor(application)
    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                isLoading = true,
            )
        }

        // observing intenet connectivity
        viewModelScope.launch {
            networkMonitor.isConnected.collect { isConnected ->

                // for syncing
                if (isConnected) {
                    syncAllOrdersUseCase()
                }
//                WorkManager.getInstance(application).enqueue(
//                    OneTimeWorkRequestBuilder<SyncOrdersWorker>().build()
//                )

                // for UI
                _uiState.update { current ->
                    current.copy(isConnected = isConnected)
                }
            }
        }

        viewModelScope.launch {
            delay(2000)
            getProducts()
        }
    }

    fun getProducts() {
        _uiState.update {
            it.copy(
                productsList = getAllProductsUseCase(),
                isLoading = false
            )
        }

    }


    fun addProductToCart(product: Product) {
        if (product.stock == 0) {
            _uiState.update {
                it.copy(
                    errorMessage = "OPS! Out of stock"
                )
            }
            return@addProductToCart
        }

        _uiState.update { currentState ->
            val existing = currentState.selectedProductList.find { it.id == product.id }

            val updatedList = if (existing != null) {
                currentState.selectedProductList.map {
                    if (it.id == product.id) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                currentState.selectedProductList + SelectedProduct(
                    id = product.id,
                    name = product.name,
                    price = product.price,
                    quantity = 1,
                    taxable = product.taxable,
                    thumb = product.thumb
                )
            }

            currentState.copy(selectedProductList = updatedList.toMutableList())
        }
        calculateTotal()
    }

    fun removeProductFromCart(product: SelectedProduct) {
        _uiState.update { currentState ->
            val existing = currentState.selectedProductList.find { it.id == product.id }

            val updatedList = when {
                existing == null -> currentState.selectedProductList
                existing.quantity > 1 -> currentState.selectedProductList.map {
                    if (it.id == product.id) it.copy(quantity = it.quantity - 1) else it
                }

                else -> currentState.selectedProductList.filterNot { it.id == product.id }
            }

            currentState.copy(selectedProductList = updatedList.toMutableList())
        }
        calculateTotal()
    }

    fun calculateTotal() {
        val state = _uiState.value

        val pair = calculateTotalAndDiscountUseCase(state.selectedProductList)

        _uiState.update {
            it.copy(
                total = pair.first,
                discount = pair.second
            )
        }
    }

    fun clearErrorMsg() {
        _uiState.update {
            it.copy(
                errorMessage = null
            )
        }
    }


    override fun onCleared() {
        super.onCleared()
        networkMonitor.stop()
    }

    fun checkoutOrder() {
        _uiState.update {
            it.copy(
                isLoading = true
            )
        }

        viewModelScope.launch {
            if (createOrderUseCase(
                    application = application,
                    _uiState.value.selectedProductList.toString()
                )
            ) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        selectedProductList = mutableListOf(),
                        total = null,
                        errorMessage = "Checkout Success!",
                        discount = null
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        selectedProductList = mutableListOf(),
                        total = null,
                        discount = null,
                        errorMessage = "Checkout went wrong!, Switch to OFFLINE mode"
                    )

                }
            }
        }
    }
}
