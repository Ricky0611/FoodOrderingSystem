package com.example.rikki.foodorderingsystem

import android.content.Context
import android.graphics.Bitmap
import androidx.collection.LruCache
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley

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

    val imageLoader: ImageLoader by lazy {
        ImageLoader(requestQueue,
            object : ImageLoader.ImageCache {
                private val cache = LruCache<String, Bitmap>(20)
                override fun getBitmap(url: String): Bitmap? {
                    return cache.get(url)
                }
                override fun putBitmap(url: String, bitmap: Bitmap) {
                    cache.put(url, bitmap)
                }
            })
    }
}