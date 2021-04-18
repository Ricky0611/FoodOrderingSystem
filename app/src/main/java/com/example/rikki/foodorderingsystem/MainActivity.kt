package com.example.rikki.foodorderingsystem

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        const val defaultCity = "Delhi"
        lateinit var appContext: AppController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appContext = AppController.getInstance(this)
        // bottom navigation menu
        BottomNavigationViewHelper.disableShiftMode(bottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            val fragment = when (item.itemId) {
                R.id.bottom_home -> HomeFragment.newInstance(defaultCity)
                R.id.bottom_orders -> OrdersFragment()
                R.id.bottom_personalInfo -> UserInfoFragment()
                R.id.bottom_checkout -> CheckOutFragment()
                else -> HomeFragment.newInstance(defaultCity)
            }
            supportFragmentManager.beginTransaction().replace(R.id.fragment_replaceable, fragment).commit()
            true
        }
        // initialize
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_replaceable, HomeFragment.newInstance(defaultCity)
        ).commit()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_replaceable)
        when (fragment) {
            is ReviewFragment -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_replaceable, OrdersFragment()).commit()
            }
            is FoodItemViewFragment -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_replaceable, HomeFragment.newInstance(defaultCity)
                ).commit()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }
}