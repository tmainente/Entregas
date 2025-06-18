package com.example.entregas.di

import androidx.room.Room
import com.example.entregas.data.local.DeliveryDatabase
import com.example.entregas.data.remote.CityService
import com.example.entregas.data.repository.CityRepositoryImpl
import com.example.entregas.data.repository.DeliveryRepositoryImpl
import com.example.entregas.domain.repository.CityRepository
import com.example.entregas.domain.repository.DeliveryRepository
import com.example.entregas.domain.usecases.SaveDeliveryUseCase
import com.example.entregas.domain.usecases.DeleteDeliveryUseCase
import com.example.entregas.domain.usecases.UpdateDeliveryUseCase
import com.example.entregas.domain.usecases.ShowDeliveryUseCase
import com.example.entregas.domain.usecases.ShowCityUseCase
import com.example.entregas.presentation.DeliveryFormViewModel
import com.example.entregas.presentation.DeliveryViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.SECONDS

private const val BASE_URL = "https://servicodados.ibge.gov.br/api/v1/"
private const val TIMEOUT = 30L

val databaseModule = module {
        single {
            Room.databaseBuilder(
                androidApplication(),
                DeliveryDatabase::class.java,
                "delivery_db"
            ).build()
        }
        single { get<DeliveryDatabase>().deliveryDao() }
    }

    val networkModule = module {
        val client = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, SECONDS)
            .readTimeout(TIMEOUT, SECONDS)
            .writeTimeout(TIMEOUT, SECONDS)
            .build()
        single {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
        single { get<Retrofit>().create(CityService::class.java) }
    }

    val repositoryModule = module {
        single<DeliveryRepository> { DeliveryRepositoryImpl(get()) }
        single<CityRepository> { CityRepositoryImpl(get()) }
    }

    val useCaseModule = module {
        single { SaveDeliveryUseCase(get()) }
        single { DeleteDeliveryUseCase(get()) }
        single { UpdateDeliveryUseCase(get()) }
        single { ShowDeliveryUseCase(get()) }
        single { ShowCityUseCase(get()) }
    }

val viewModelModule = module {
    viewModel {
        DeliveryViewModel(
            listDeliveriesUseCase = get(),
            deleteDeliveryUseCase = get(),
        )
    }
    viewModel {
        DeliveryFormViewModel(
            saveDeliveryUseCase = get(),
            updateDeliveryUseCase = get(),
            fetchCitiesByUfUseCase = get()
        )
    }
}

    val appModules = listOf(databaseModule, networkModule, repositoryModule, useCaseModule, viewModelModule)
