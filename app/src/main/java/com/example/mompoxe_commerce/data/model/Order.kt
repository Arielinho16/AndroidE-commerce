package com.example.mompoxe_commerce.data.model

data class Order(
    val total: Float,
    val nombre: String,
    val apellido: String,
    val telefono: String,
    val identificacion: String,
    val delivery: Boolean,
    val direccion: String?
)
