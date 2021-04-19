package com.example.rikki.foodorderingsystem

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class FoodCatageroyAdapter(fm: FragmentManager, private var tabCount: Int) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> VegFragment()
            else -> NonVegFragment()
        }
    }

    override fun getCount(): Int {
        return tabCount
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "VEG FOOD"
            else -> "NON-VEG FOOD"
        }
    }
}