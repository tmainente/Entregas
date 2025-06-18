package com.example.entregas.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Delivery")
data class DeliveryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
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
    val complement: String?
)