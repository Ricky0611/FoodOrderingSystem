package com.example.rikki.foodorderingsystem

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rikki.foodorderingsystem.MainActivity.Companion.appContext
import com.example.rikki.foodorderingsystem.model.FoodItem
import com.squareup.picasso.Picasso

class CartAdapter(val mContext: Context, val mNotifyFragment: NotifyFragment) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var cartItemsList = appContext.getCartItems().getCartItemsList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_view, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItemsList[position]
        holder.selectedItemTitle.text = item.foodName
        holder.selectedItemDesc.text = item.foodRecipe
        val quantity = appContext.getCartItems().getItemQuantity()[item] ?: 1
        val price = quantity.times(item.foodPrice.toDouble())
        holder.cartItemTotal.text = mContext.resources.getString(R.string.hint_orderTotal).plus(
            price
        )
        holder.cartItemQuantitySelected.text = quantity.toString()
        holder.selectedItemPriceQuantity.text = quantity.toString().plus(" * ").plus(item.foodPrice)
        Picasso.with(mContext).load(item.foodImage).into(holder.selectedImageView)
    }

    override fun getItemCount(): Int {
        return cartItemsList.size
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var selectedImageView = itemView.findViewById<View>(R.id.cartItemImage) as ImageView
        var incrimentQuantity = itemView.findViewById<View>(R.id.incrimentCartItem) as ImageView
        var decrimentQuantity = itemView.findViewById<View>(R.id.decrimentCartItem) as ImageView
        var selectedItemTitle = itemView.findViewById<View>(R.id.cartItemTitle) as TextView
        var selectedItemDesc = itemView.findViewById<View>(R.id.cartItemDesc) as TextView
        var selectedItemPriceQuantity = itemView.findViewById<View>(R.id.cartItemquantityPrice) as TextView
        var cartItemTotal = itemView.findViewById<View>(R.id.cartItemTotal) as TextView
        var cartItemQuantitySelected = itemView.findViewById<View>(R.id.cartItemquantitySelected) as TextView
        var textViewAlert = itemView.findViewById<View>(R.id.textViewAlert) as TextView

        init {
            incrimentQuantity.setOnClickListener {
                itemIncrimented(cartItemsList[adapterPosition], adapterPosition)
                mNotifyFragment.onItemTotalChanged()
            }
            decrimentQuantity.setOnClickListener {
                itemDecrimented(cartItemsList[adapterPosition], adapterPosition)
                mNotifyFragment.onItemTotalChanged()
            }
        }

        private fun itemDecrimented(products: FoodItem, poition: Int) {
            val cartItems = appContext.getCartItems()
            val quantity = appContext.getCartItems().getItemQuantity()[products] ?: 1
            if (quantity > 1) {
                textViewAlert.visibility = View.GONE
                cartItems.putintoHashMap(products, -1)
                notifyItemChanged(poition)
            } else {
                textViewAlert.visibility = View.VISIBLE
                textViewAlert.text = "Item quantity cannot less then 0"
            }
        }

        private fun itemIncrimented(products: FoodItem, position: Int) {
            val cartItems = appContext.getCartItems()
            textViewAlert.visibility = View.INVISIBLE
            cartItems.putintoHashMap(products, 1)
            notifyItemChanged(position)
        }
    }
}

interface NotifyFragment {
    fun onItemTotalChanged()
}