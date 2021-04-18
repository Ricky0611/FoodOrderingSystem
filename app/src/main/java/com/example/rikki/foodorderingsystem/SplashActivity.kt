package com.example.rikki.foodorderingsystem

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest

class SplashActivity : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val context = this
        val mobile = AppController.getInstance(this.applicationContext).getUserMobile()
        val password = AppController.getInstance(this.applicationContext).getUserPassword()
        Log.d(TAG, "$mobile, $password")
        if (mobile.isNotBlank() && password.isNotBlank()) {
            checkLoginStatus(context, mobile, password)
        } else {
            startActivity(Intent(context, LoginActivity::class.java))
            context.finish()
        }
    }

    private fun checkLoginStatus(context: AppCompatActivity, phone: String, password: String) {
        val loginUrl = String.format(resources.getString(R.string.url_login), phone, password)
        val loginRequest = StringRequest(
            Request.Method.GET,
            loginUrl,
            { response ->
                Log.d(TAG, response)
                if (response.contains(LoginActivity.SUCCESS)) {
                    startActivity(Intent(context, MainActivity::class.java))
                    context.finish()
                } else {
                    startActivity(Intent(context, LoginActivity::class.java))
                    context.finish()
                }
            }, { error ->
                Log.d(TAG, error.toString())
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            })
        AppController.getInstance(context.applicationContext).addToRequestQueue(loginRequest)
    }
}