package com.example.rikki.foodorderingsystem.model

data class FoodItem(
    val foodId: String,
    val foodName: String,
    val foodRecipe: String,
    val foodPrice: String,
    val foodCat: String,
    val foodImage: String
)