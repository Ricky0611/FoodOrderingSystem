package com.example.rikki.foodorderingsystem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.rikki.foodorderingsystem.MainActivity.Companion.appContext
import com.example.rikki.foodorderingsystem.MainActivity.Companion.defaultCity
import com.example.rikki.foodorderingsystem.model.FoodItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*

class HomeFragment : Fragment() {

    private val TAG = this.javaClass.simpleName
    private lateinit var homeScreen: View
    private lateinit var childFm : FragmentManager
    private var selected = true

    companion object {
        private const val FROMCITY = "fromCity"
        private lateinit var city: String

        @JvmStatic
        fun newInstance(fromCity: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(FROMCITY, fromCity)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        city = arguments?.getString(FROMCITY, defaultCity) ?: defaultCity
        childFm = childFragmentManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeScreen = inflater.inflate(R.layout.fragment_home, container, false)
        // start loading
        homeScreen.loading.visibility = View.VISIBLE
        // init view
        (requireActivity() as AppCompatActivity).setSupportActionBar(homeScreen.hometoolbar)
        manageToolBar()
        getFoodList()
        return homeScreen
    }

    private fun manageToolBar() {
        val address = appContext.getUserAddress()
        homeScreen.toAddress.text = address
        val selectedDeliveryCity = homeScreen.select_delivery_city
        val foodcityAdapter = ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.foodCities,
            android.R.layout.simple_spinner_dropdown_item
        )
        val index = resources.getStringArray(R.array.foodCities).indexOf(city)
        selectedDeliveryCity.apply {
            adapter = foodcityAdapter
            onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Log.d(TAG, parent?.getItemAtPosition(position).toString() + "intial")
                    if (!selected) {
                        Log.d(TAG, parent?.getItemAtPosition(position).toString())
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.fragment_replaceable,
                                newInstance(parent?.getItemAtPosition(position).toString())
                            ).commit()
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    Log.d(TAG, defaultCity)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_replaceable, newInstance(defaultCity)).commit()
                }

            }
            setSelection(index)
        }
    }

    private fun getFoodList() {
        if (city.equals(resources.getStringArray(R.array.foodCities)[0], true)) {
            city = defaultCity
        }
        val foodItemLists = ArrayList<FoodItem>()
        val foodUrl = String.format(resources.getString(R.string.url_food), city)
        val foodRequest = JsonObjectRequest(
            Request.Method.GET,
            foodUrl,
            null,
            { response ->
                Log.d(TAG, response.toString())
                val foodList = response.getJSONArray("Food")
                for (i in 0 until foodList.length()) {
                    val foodItem = foodList.getJSONObject(i)
                    val foodId = foodItem.getString("FoodId")
                    val foodName = foodItem.getString("FoodName")
                    val foodRecepiee = foodItem.getString("FoodRecepiee")
                    val foodPrice = foodItem.getString("FoodPrice")
                    val foodCat = foodItem.getString("FoodCategory")
                    val foodImage = foodItem.getString("FoodThumb")
                    val item = FoodItem(
                        foodId,
                        foodName,
                        foodRecepiee,
                        foodPrice,
                        foodCat,
                        foodImage
                    )
                    foodItemLists.add(item)
                }
                appContext.setFoodItemLists(foodItemLists)
                val mTabLayout = homeScreen.food_catatgeory_tab_layout
                val mViewPager = homeScreen.food_catageroy_view_pager
                mTabLayout.apply {
                    addTab(newTab().setText(R.string.food_veg))
                    addTab(newTab().setText(R.string.food_non_veg))
                }
                val foodCatageroyAdapter = FoodCatageroyAdapter(
                    childFm,
                    homeScreen.food_catatgeory_tab_layout.tabCount
                )
                mTabLayout.apply {
                    tabGravity = TabLayout.GRAVITY_FILL
                    setupWithViewPager(mViewPager)
                }
                mViewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(mTabLayout))
                mViewPager.adapter = foodCatageroyAdapter
                selected = false
                homeScreen.loading.visibility = View.GONE
            }, { error ->
                Log.d(TAG, error.toString())
                homeScreen.loading.visibility = View.GONE
                Toast.makeText(requireActivity(), error.message, Toast.LENGTH_SHORT).show()
            })
        appContext.addToRequestQueue(foodRequest)
    }
}