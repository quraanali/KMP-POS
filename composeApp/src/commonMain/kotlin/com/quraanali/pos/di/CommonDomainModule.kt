package com.quraanali.pos.di


import android.content.Context
import androidx.work.WorkerParameters
import com.quraanali.pos.domain.cart.CalculateTotalAndDiscountUseCase
import com.quraanali.pos.domain.cart.CreateOrderUseCase
import com.quraanali.pos.domain.cart.GetDeviceUniqueIdUseCase
import com.quraanali.pos.domain.cart.GetNextOrderLocalIdUseCase
import com.quraanali.pos.domain.home.GetAllProductsUseCase
import com.quraanali.pos.domain.sync.GetUnsyncedOrdersUseCase
import com.quraanali.pos.domain.sync.MarkOrderAsSyncedUseCase
import com.quraanali.pos.domain.sync.SyncAllOrdersUseCase
import com.quraanali.pos.domain.sync.SyncOrderUseCase
import com.quraanali.pos.utils.SyncOrdersWorker
import org.koin.androidx.workmanager.dsl.worker
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


    factory {
        MarkOrderAsSyncedUseCase(get())
    }


    factory {
        SyncAllOrdersUseCase(get(), get(), get())
    }

    factory {
        SyncOrderUseCase(get())
    }
    factory {
        GetUnsyncedOrdersUseCase(get())
    }

    factory {
        SyncOrderUseCase(get())
    }


    worker { (context: Context, params: WorkerParameters) ->
        SyncOrdersWorker(
            context, params, get(), // HomeRepository
            get(), // SyncOrdersUseCase
            get()  // MarkOrderAsSyncedUseCase
        )
    }

}
