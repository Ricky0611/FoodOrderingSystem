package com.example.rikki.foodorderingsystem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_food_category.view.*

class NonVegFragment : Fragment()  {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_food_category, container, false)
        val recyclerView = view.food_catageroy_recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val foodCatAdapter = FoodCatAdapter(requireActivity(), FoodCatAdapter.NON_VEG)
        recyclerView.adapter = foodCatAdapter
        return view
    }
}