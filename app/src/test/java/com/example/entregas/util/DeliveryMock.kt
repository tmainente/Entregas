package com.example.entregas.util

import com.example.entregas.domain.model.Delivery

val mockDelivery = Delivery(
    id = 1,
    quantPackage = 3,
    dateLimit = "30/06/96",
    nameClient = "Maria",
    cpfClient = "12345678901",
    cep = "12345-678",
    uf = "SP",
    city = "São Paulo",
    neighborhood = "Centro",
    street = "Rua A",
    number = "123",
    complement = "aa"
)
