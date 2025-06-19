package com.example.entregas.domain.model


@kotlinx.serialization.Serializable
data class Delivery(
    val id: Long = 0L,
    val quantPackage: Int,
    val dateLimit: String,
    val nameClient: String,
    val cpfClient: String,
    val cep: String,
    val uf: String,
    val city: String,
    val neighborhood: String,
    val street: String,
    val number: String,
    val complement: String? = null

)
