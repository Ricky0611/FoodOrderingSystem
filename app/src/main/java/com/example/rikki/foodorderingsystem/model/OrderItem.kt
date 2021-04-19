package com.example.rikki.foodorderingsystem.model

data class OrderItem(
    val id: String,
    val name: String,
    val quantity: String,
    val totalCost: String,
    val status: String,
    val address: String,
    val date: String
)
