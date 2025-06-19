package com.example.entregas.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.entregas.data.local.DeliveryDatabase
import com.example.entregas.data.remote.CityService
import com.example.entregas.data.repository.CityRepositoryImpl
import com.example.entregas.domain.repository.CityRepository
import com.example.entregas.domain.repository.DeliveryRepository
import com.example.entregas.presentation.DeliveryFormViewModel
import com.example.entregas.presentation.DeliveryViewModel
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.check.checkModules
import retrofit2.Retrofit
import kotlin.test.assertNotNull

class KoinAppModulesTest : KoinTest {


    @Test
    fun `checkModules should verify MovieModule definitions`() {
        val fakeModule = module {
            single {
                mockk<Retrofit>(relaxed = true) {
                    every { create(CityService::class.java) } returns mockk<CityService>(relaxed = true)
                }
            }
            single { mockk<DeliveryRepository>(relaxed = true) }
            single<CityRepository> { CityRepositoryImpl(get()) }
        }
        koinApplication {
            modules(fakeModule, networkModule, useCaseModule, viewModelModule) // Mock Context
            checkModules()
        }
    }

}