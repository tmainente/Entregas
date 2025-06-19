package com.example.entregas.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface CityService {
        @GET("localidades/estados/{uf}/municipios")
        suspend fun getCidadesPorUf(@Path("uf") uf: String): List<CityResponse>
    }
