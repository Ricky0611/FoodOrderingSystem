package com.example.rikki.foodorderingsystem

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.rikki.foodorderingsystem.MainActivity.Companion.appContext
import com.example.rikki.foodorderingsystem.model.CartItems
import com.example.rikki.foodorderingsystem.model.FoodItem
import com.squareup.picasso.Picasso
import java.util.*

class FoodCatAdapter(val context: Context, cat: Int) : RecyclerView.Adapter<FoodCatAdapter.MyViewHolder>() {

    companion object{
        const val VEG = 1
        const val NON_VEG = 2
    }

    private var mFoodItemList: ArrayList<FoodItem> = ArrayList<FoodItem>()
    private val TAG = this.javaClass.simpleName

    init {
        mFoodItemList = when (cat) {
            VEG -> appContext.getVegFood()
            else -> appContext.getNonVegFood()
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var foodName: TextView
        var foodReciepee: TextView
        var foodPrice: TextView
        var foodImage: ImageView
        var addToCartOnHome: ImageView
        var foodItemCardView: CardView

        init {
            foodItemCardView = itemView.findViewById<View>(R.id.food_catageroy_layout_cardview) as CardView
            foodName = itemView.findViewById<View>(R.id.food_title) as TextView
            foodReciepee = itemView.findViewById<View>(R.id.food_recieppe) as TextView
            foodPrice = itemView.findViewById<View>(R.id.food_price) as TextView
            foodImage = itemView.findViewById<View>(R.id.food_image_view) as ImageView
            addToCartOnHome = itemView.findViewById<View>(R.id.addtocart_main_view) as ImageView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.food_catagery_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val foodName = holder.foodName
        val foodReciepee = holder.foodReciepee
        val foodPrice = holder.foodPrice
        val foodImage = holder.foodImage
        val addToCartOnMainView = holder.addToCartOnHome

        foodName.text = mFoodItemList[position].foodName
        foodReciepee.text = mFoodItemList[position].foodRecipe
        foodPrice.text = "$ ".plus(mFoodItemList[position].foodPrice)
        val foodItemCardView = holder.foodItemCardView
        Picasso.with(context).load(mFoodItemList[position].foodImage).into(foodImage)

        foodImage.setOnClickListener {
            Log.d(TAG, mFoodItemList[position].foodId + "  " + position)
            val foodItemViewFragment: FoodItemViewFragment = FoodItemViewFragment.newInstance(
                mFoodItemList[position].foodId, position
            )
            val fragmentTransaction =
                (context as FragmentActivity).supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_replaceable, foodItemViewFragment).commit()
            fragmentTransaction.addToBackStack("FoodItemView")
        }

        addToCartOnMainView.setOnClickListener {
            val foodItemList: FoodItem = mFoodItemList[position]
            Log.d(TAG, "add to cart in home i clicked")
            if (!appContext.getCartItems().getItemQuantity()
                    .containsKey(foodItemList)
            ) {
                val cartItems: CartItems = appContext.getCartItems()
                Log.d("CartCheck", "FirstTime entered")
                cartItems.addToCartList(foodItemList, 1)
            } else {
                val cartItems: CartItems = appContext.getCartItems()
                cartItems.putintoHashMap(foodItemList, 1)
                Log.d("CartCheck", "secondTime entered")
            }
            addToCartOnMainView.setImageResource(R.drawable.add_to_basket_after_click)
            addToCartOnMainView.isClickable = false
        }
    }

    override fun getItemCount(): Int {
        return mFoodItemList.size
    }
}