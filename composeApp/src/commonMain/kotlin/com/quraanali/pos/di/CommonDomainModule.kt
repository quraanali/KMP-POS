package com.quraanali.pos.di


import com.quraanali.pos.domain.CalculateTotalAndDiscountUseCase
import com.quraanali.pos.domain.CreateOrderUseCase
import com.quraanali.pos.domain.GetAllProductsUseCase
import com.quraanali.pos.domain.GetDeviceUniqueIdUseCase
import com.quraanali.pos.domain.GetNextOrderLocalIdUseCase
import org.koin.dsl.module

val commonDomainModule = module {

    factory {
        GetAllProductsUseCase(get())
    }

    factory {
        GetDeviceUniqueIdUseCase()
    }

    factory {
        GetNextOrderLocalIdUseCase(get())
    }


    factory {
        CalculateTotalAndDiscountUseCase(get())
    }


    factory {
        CreateOrderUseCase(get(), get(), get())
    }


}
