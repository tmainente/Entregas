package com.example.entregas.data

import com.example.entregas.data.local.DeliveryEntity
import com.example.entregas.domain.model.Delivery

fun Delivery.toEntity() = DeliveryEntity(
    id, quantPackage, dateLimit, nameClient, cpfClient, cep,
    uf, city, neighborhood, street, number, complement
)

fun DeliveryEntity.toModel() = Delivery(
    id, quantPackage, dateLimit, nameClient, cpfClient, cep,
    uf, city, neighborhood, street, number, complement
)