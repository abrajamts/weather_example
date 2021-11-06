package com.abrahammontes.prueba.di

import android.content.Context
import com.abrahammontes.prueba.common.GeneralViewModel
import com.abrahammontes.prueba.data.PrApiService
import com.abrahammontes.prueba.data.dummy.DummyCache
import com.abrahammontes.prueba.data.dummy.DummyCacheImpl
import com.abrahammontes.prueba.data.factory.ApiService
import com.abrahammontes.prueba.data.factory.ApiServiceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

object KoinHelper {
    @JvmStatic
    fun start(appContext: Context) = startKoin {
        modules(listOf(appModule))
        androidContext(appContext)
    }

    @JvmStatic
    fun stop() = stopKoin()

    private val appModule
        get() = module {
            factory<DummyCache> { DummyCacheImpl(androidApplication().assets) }
            single {
                ApiServiceImpl.Builder(get())
                    .baseUrl("http://api.openweathermap.org/")
                    .build()
            }
            single { get<ApiService>().create(PrApiService::class.java)  }
            viewModel { GeneralViewModel(get(), get()) }
        }
}