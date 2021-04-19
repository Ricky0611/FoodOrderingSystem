package com.example.rikki.foodorderingsystem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rikki.foodorderingsystem.MainActivity.Companion.appContext
import com.example.rikki.foodorderingsystem.model.FoodItem
import com.example.rikki.foodorderingsystem.utils.SwipeHelper
import kotlinx.android.synthetic.main.fragment_check_out.view.*
import java.util.*

class CheckOutFragment : Fragment(), NotifyFragment, SwipeHelper.OnSwipeDataChange {

    private lateinit var cartView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cartView = inflater.inflate(R.layout.fragment_check_out, container, false)
        // init tool bar
        if ((requireActivity() as AppCompatActivity?)!!.supportActionBar != null) {
            (requireActivity() as AppCompatActivity?)!!.supportActionBar!!.title = "Cart"
        }
        // init shopping cart
        if(appContext.getCartItems().getCartItemsList().isNullOrEmpty()) {
            cartView.emptyCartDisplay.visibility = View.VISIBLE
            cartView.cart_card_view.visibility = View.GONE
        } else {
            cartView.emptyCartDisplay.visibility = View.GONE
            cartView.cart_card_view.visibility = View.VISIBLE
            val cartAdapter = CartAdapter(requireActivity(), this)
            cartView.cartRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = cartAdapter
            }
            // swipe to delete
            val callback: ItemTouchHelper.Callback = SwipeHelper(
                0,
                ItemTouchHelper.LEFT,
                cartAdapter,
                this
            )
            val helper = ItemTouchHelper(callback)
            helper.attachToRecyclerView(cartView.cartRecyclerView)
            cartView.cartTotal.text = getTotalValueOfCart()
            // checkout
            cartView.checkoutCart.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_replaceable, PaymentFragment())
                    .commit()
            }
        }
        return cartView
    }

    private fun getTotalValueOfCart(): String {
        var count = 0.0
        val cartItems: ArrayList<FoodItem> = appContext.getCartItems().getCartItemsList()
        for (selectedItem in cartItems) {
            val quantity = appContext.getCartItems().getItemQuantity()[selectedItem] ?: 1
            count += quantity * selectedItem.foodPrice.toDouble()
        }
        if (count == 0.0) {
            cartView.emptyCartDisplay.visibility = View.VISIBLE
            cartView.cart_card_view.visibility = View.GONE
        }
        return "$ $count"
    }

    override fun onItemTotalChanged() {
        cartView.cartTotal.text = getTotalValueOfCart()
    }

    override fun onSwipeCountChange() {
        cartView.cartTotal.text = getTotalValueOfCart()
    }
}