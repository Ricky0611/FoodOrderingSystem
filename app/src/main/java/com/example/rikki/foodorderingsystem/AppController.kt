package com.example.rikki.foodorderingsystem

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import java.util.*

class AppController constructor(context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: AppController? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppController(context).also {
                    INSTANCE = it
                }
            }
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }

    private val sharedPref = context.getSharedPreferences(context.applicationInfo.packageName.plus("prefs"), Context.MODE_PRIVATE)

    fun saveUserInfo(name: String, password: String, phone: String, email: String, address: String) {
        with(sharedPref.edit()) {
            putString("UserName", name)
            putString("UserPassword", password)
            putString("UserPhone", phone)
            putString("UserEmail", email)
            putString("UserAddress", address)
            apply()
        }
    }

    fun getUserName() : String {
        return sharedPref.getString("UserName", "") ?: ""
    }

    fun getUserMobile() : String {
        return sharedPref.getString("UserPhone", "") ?: ""
    }

    fun getUserPassword() : String {
        return sharedPref.getString("UserPassword", "") ?: ""
    }

    fun getUserAddress() : String {
        return sharedPref.getString("UserAddress", "") ?: ""
    }

    fun setUserUpdatedAddress(address: String) {
        with(sharedPref.edit()) {
            putString("UserDeliveryAddress", address)
            apply()
        }
    }

    fun getUserUpdatedAddress() : String {
        return sharedPref.getString("UserDeliveryAddress", "") ?: ""
    }

    fun getUserEmail() : String {
        return sharedPref.getString("UserEmail", "") ?: ""
    }

    fun clearUserInfo() : Boolean {
        return with(sharedPref.edit()) {
            clear()
            commit()
        }
    }

    private var mFoodItemList = ArrayList<FoodItem>()

    fun getFoodItemList() : ArrayList<FoodItem> {
        return mFoodItemList
    }

    fun setFoodItemLists(mFoodItemLists: ArrayList<FoodItem>) {
        mFoodItemList = mFoodItemLists
    }

    fun getVegFood(): ArrayList<FoodItem> {
        val vegList = ArrayList<FoodItem>()
        for (foodItem in mFoodItemList) {
            if (foodItem.foodCat.equals("veg", ignoreCase = true))
                vegList.add(foodItem)
        }
        return vegList
    }


    fun getNonVegFood(): ArrayList<FoodItem> {
        val nonVegList = ArrayList<FoodItem>()
        for (foodItem in mFoodItemList) {
            if (foodItem.foodCat.equals("non-veg", ignoreCase = true))
                nonVegList.add(foodItem)
        }
        return nonVegList
    }

    fun getFoodItem(foodItemID: String?): FoodItem? {
        for (foodItem in mFoodItemList) {
            if (foodItem.foodId.equals(foodItemID, ignoreCase = true))
                return foodItem
        }
        return null
    }

    private var mCartItems = CartItems()

    fun getCartItems() : CartItems {
        return mCartItems
    }
}