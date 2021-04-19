package com.example.rikki.foodorderingsystem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rikki.foodorderingsystem.MainActivity.Companion.appContext
import com.example.rikki.foodorderingsystem.model.CartItems
import com.example.rikki.foodorderingsystem.model.FoodItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_food_item_view.view.*

class FoodItemViewFragment : Fragment() {

    private val TAG = this.javaClass.simpleName

    companion object {
        const val FOODID = "foodID"
        const val POSITION = "position"

        @JvmStatic
        fun newInstance(productId: String, position: Int) =
            FoodItemViewFragment().apply {
                arguments = Bundle().apply {
                    putString(FOODID, productId)
                    putInt(POSITION, position)
                }
            }
    }

    private lateinit var mFoodID: String
    private var mPosition = 0
    private lateinit var mFoodItem : FoodItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mFoodID = it.getString(FOODID, "")
            mPosition = it.getInt(POSITION)
            mFoodItem = appContext.getFoodItem(
                mFoodID
            )!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_food_item_view, container, false)
        // init view
        view.food_item_view_title.text = mFoodItem.foodName
        view.food_item_view_reciepee.text = mFoodItem.foodRecipe
        view.food_item_view_price.text = mFoodItem.foodPrice
        view.food_item_view_cat.text = mFoodItem.foodCat
        Picasso.with(requireContext()).load(mFoodItem.foodImage).into(view.food_item_view_image)
        view.foodItemAddToCart.setOnClickListener {
            val quantity = view.food_item_view_quantity.text
            if (quantity.isNullOrBlank() || Integer.parseInt(quantity.toString()) <= 0) {
                view.food_item_view_quantity.error = resources.getString(R.string.Quantity_Null)
            } else {
                if (!appContext.getCartItems().getItemQuantity().containsKey(mFoodItem)) {
                    val cartItems = appContext.getCartItems()
                    Log.d(TAG, "FirstTime entered")
                    cartItems.addToCartList(mFoodItem, view.food_item_view_quantity.text.toString().toInt())
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment_replaceable, CheckOutFragment()).commit()
                        addToBackStack(null)
                    }
                } else {
                    val cartItems: CartItems = appContext.getCartItems()
                    cartItems.putintoHashMap(mFoodItem, view.food_item_view_quantity.text.toString().toInt())
                    Log.d(TAG, "secondTime entered")
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment_replaceable, CheckOutFragment()).commit()
                        addToBackStack(null)
                    }
                }
            }
        }
        return view
    }
}