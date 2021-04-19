package com.example.rikki.foodorderingsystem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.example.rikki.foodorderingsystem.MainActivity.Companion.appContext
import com.example.rikki.foodorderingsystem.model.OrderItem
import kotlinx.android.synthetic.main.fragment_orders.view.*
import org.json.JSONObject
import java.util.*

class OrdersFragment : Fragment() {

    private val TAG = this.javaClass.simpleName
    private val list = ArrayList<OrderItem>()
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var orderView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderAdapter = OrderAdapter(requireActivity(), list)
        val mobile = appContext.getUserMobile()
        getOrders(mobile)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        orderView = inflater.inflate(R.layout.fragment_orders, container, false)
        // search order
        orderView.searchText.setOnClickListener{
            orderView.searchText.text.clear()
            orderView.trackingCard.visibility = View.GONE
        }
        orderView.searchButton.setOnClickListener{
            val orderId = orderView.searchText.text.toString()
            if (orderId.isNotBlank()) {
                trackStatus(orderId)
            }
        }
        // show orders
        val linearLayoutManager = LinearLayoutManager(requireActivity()).apply {
            stackFromEnd = true
            reverseLayout = true
        }
        orderView.orderRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = orderAdapter
        }
        return orderView
    }

    private fun trackStatus(id: String) {
        val statusUrl = String.format(resources.getString(R.string.url_order_status), id)
        val statusRequest = StringRequest(
            Request.Method.GET, statusUrl,
            { response ->
                Log.d(TAG, response)
                if (response.contains("Incorrect", true)) {
                    Toast.makeText(requireActivity(), response, Toast.LENGTH_SHORT).show()
                } else {
                    val result = JSONObject(response).getJSONArray("Order Detail").getJSONObject(0)
                    orderView.orderIdText.text = result.getString("OrderId")
                    orderView.totalOrderText.text = result.getString("TotalOrder")
                    orderView.orderStatusText.text = getStatus(result.getString("OrderStatus"))
                    orderView.orderDateText.text = result.getString("OrderDate")
                    orderView.trackingCard.visibility = View.VISIBLE
                }
            },{ error ->
                Log.d(TAG, error.toString())
                Toast.makeText(requireActivity(), error.message, Toast.LENGTH_SHORT).show()
            }
        )
        appContext.addToRequestQueue(statusRequest)
    }

    private fun getOrders(mobile: String) {
        val orderUrl = String.format(resources.getString(R.string.url_order), mobile)
        val orderRequest = StringRequest(
            Request.Method.GET, orderUrl,
            { response ->
                Log.d(TAG, response.toString())
                if (response.contains("No order", true)) {
                    Toast.makeText(requireActivity(), response, Toast.LENGTH_SHORT).show()
                } else {
                    parseResponse(JSONObject(response))
                }
            }, { error ->
                Log.d(TAG, error.toString())
                Toast.makeText(requireActivity(), error.message, Toast.LENGTH_SHORT).show()
            })
        appContext.addToRequestQueue(orderRequest)
    }

    private fun parseResponse(response: JSONObject) {
        val result = response.getJSONArray("Order Detail")
        for (i in 0 until result.length()) {
            val obj = result.getJSONObject(i)
            val item = OrderItem(
                obj.getString("OrderId"),
                obj.getString("OrderName"),
                obj.getString("OrderQuantity"),
                obj.getString("TotalOrder"),
                getStatus(obj.getString("OrderStatus")),
                obj.getString("OrderDeliverAdd"),
                obj.getString("OrderDate")
            )
            list.add(i, item)
        }
        orderAdapter.notifyDataSetChanged()
    }

    private fun getStatus(status: String) : String {
        return when (status) {
            "1" -> "Packing"
            "2" -> "On the way"
            "3" -> "Delivered"
            else -> "Pending"
        }
    }
}