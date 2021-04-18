package com.example.rikki.foodorderingsystem

import java.util.*

class CartItems {

    private var cartItemsList: ArrayList<FoodItem> = ArrayList<FoodItem>()
    private var itemQuantity: HashMap<FoodItem, Int> = HashMap<FoodItem, Int>()

    fun getItemQuantity(): HashMap<FoodItem, Int> {
        return itemQuantity
    }


    fun getCartItemsList(): ArrayList<FoodItem> {
        return cartItemsList
    }

    fun addToCartList(selectedProduct: FoodItem, i: Int) {
        cartItemsList.add(selectedProduct)
        itemQuantity[selectedProduct] = i
    }

    fun putintoHashMap(selectedProduct: FoodItem, i: Int) {
        itemQuantity[selectedProduct]?.let {
            itemQuantity[selectedProduct] = it + i
        }
    }

    fun clearCart() {
        cartItemsList.clear()
        itemQuantity.clear()
    }

}