package com.quraanali.pos.domain.cart

import android.app.Application
import com.quraanali.pos.screens.home.SelectedProduct

class CalculateTotalAndDiscountUseCase(private val application: Application) {
    operator fun invoke(selectedProductList: MutableList<SelectedProduct>): Pair<String?, String?> {
        val subtotal = selectedProductList.sumOf { it.price * it.quantity }

        //  tax: 10% on taxable items only
        val tax = selectedProductList
            .filter { it.taxable }
            .sumOf { it.price * it.quantity * 0.10 }

        // fiscount: 5% if subtotal >= 50
        val discount = if (subtotal >= 50) subtotal * 0.05 else 0.0

        //total
        val total = subtotal + tax - discount


        val displayTotal = String.format("%.2f", total)
        val displayDiscount = String.format("%.2f", discount)


        return Pair(
            if (total > 0.0) "Total: $displayTotal" else null,
            if (discount > 0.0) "Discount: - $displayDiscount" else null
        )

    }
}