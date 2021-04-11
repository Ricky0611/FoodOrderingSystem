package com.example.rikki.foodorderingsystem

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val context = this
        Timer(false).schedule(1000) {
            startActivity(Intent(context, LoginActivity::class.java))
        }
    }
}