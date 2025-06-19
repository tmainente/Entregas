package com.example.entregas.util

import kotlinx.coroutines.test.StandardTestDispatcher

val dispatcher = StandardTestDispatcher()

 val testDispatcherProvider = object : DispatcherProvider {
    override val main = dispatcher
    override val io = dispatcher
    override val default = dispatcher
}