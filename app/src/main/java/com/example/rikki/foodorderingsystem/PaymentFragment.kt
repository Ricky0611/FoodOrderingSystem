package com.example.rikki.foodorderingsystem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.example.rikki.foodorderingsystem.MainActivity.Companion.appContext
import kotlinx.android.synthetic.main.fragment_payment.view.*
import kotlinx.android.synthetic.main.order_conformation_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class PaymentFragment : Fragment() {

    private val TAG = this.javaClass.simpleName
    private lateinit var payView: View
    private var items = 0
    private var count = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        payView = inflater.inflate(R.layout.fragment_payment, container, false)
        payView.user_delivery_location.text = appContext.getUserAddress()
        val cartItems = appContext.getCartItems().getCartItemsList()
        for (selectedItem in cartItems) {
            val quantity = appContext.getCartItems().getItemQuantity()[selectedItem] ?: 1
            items += quantity
            count += quantity * selectedItem.foodPrice.toDouble()
        }
        payView.payment_total_items.text = "Items: $items"
        payView.payment_total_price.text = "Total: $$count"
        // back to cart
        payView.back_to_cart.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.fragment_replaceable,
                CheckOutFragment()
            ).commit()
        }
        // pay the order
        payView.final_check_out.setOnClickListener {
            placeOrder()
        }
        return payView
    }

    private fun placeOrder() {
        val cartItemList = appContext.getCartItems().getCartItemsList()

        var itemCat = ""
        var itemNames = ""

        for (selectedItem in cartItemList) {
            itemCat += selectedItem.foodCat + ","
            itemNames += selectedItem.foodName.replace("\\s+", "/") + ","
        }
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = simpleDateFormat.format(Date())

        val orderUrl = String.format(
            resources.getString(R.string.url_order_place),
            itemCat,
            itemNames,
            items,
            count,
            appContext.getUserAddress(),
            date,
            appContext.getUserMobile()
        )
        val placeRequest = StringRequest(
            Request.Method.GET, orderUrl,
            { response ->
                Log.d(TAG, response)
                if (response.contains("order confirmed", true)) {
                    val orderId = response.replace("order confirmed. order id is : ", "")
                    setConformation(orderId, date)
                    appContext.getCartItems().clearCart()
                } else {
                    Toast.makeText(requireActivity(), response, Toast.LENGTH_SHORT).show()
                }
            }, { error ->
                Log.d(TAG, error.toString())
                Toast.makeText(requireActivity(), error.message, Toast.LENGTH_SHORT).show()
            }
        )
        appContext.addToRequestQueue(placeRequest)
    }

    private fun setConformation(orderId: String, date: String) {
        payView.orderPlaceLayoutInclude.visibility = View.VISIBLE
        payView.orderLayout.visibility = View.GONE
        // init view
        payView.confirm_orderid.text = orderId
        payView.confirm_order_date.text = date
        payView.confirm_order_total.text = count.toString()
        payView.trackOrder.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.fragment_replaceable,
                OrdersFragment()
            ).commit()
        }
    }
}