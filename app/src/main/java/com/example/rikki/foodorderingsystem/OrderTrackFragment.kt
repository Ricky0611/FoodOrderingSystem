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
import kotlinx.android.synthetic.main.fragment_order_track.view.*
import org.json.JSONObject

class OrderTrackFragment : Fragment() {

    companion object {
        const val OrderId = "id"
        @JvmStatic
        fun newInstance(id: String) =
            OrderTrackFragment().apply {
                arguments = Bundle().apply {
                    putString(OrderId, id)
                }
            }
    }

    private val TAG = this.javaClass.simpleName
    private lateinit var orderId: String
    private lateinit var statusView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderId = it.getString(OrderId).toString()
            trackStatus(orderId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        statusView = inflater.inflate(R.layout.fragment_order_track, container, false)
        return statusView
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
                    statusView.tOrderId.text = "Order #".plus(result.getString("OrderId"))
                    statusView.tOrderCost.text = resources.getString(R.string.hint_orderTotal).plus(result.getString("TotalOrder"))
                    statusView.tOrderDate.text = resources.getString(R.string.hint_orderDate).plus(result.getString("OrderDate"))
                    val status = result.getString("OrderStatus")
                    when (status) {
                        "1" -> {
                            statusView.status.text = "Packing"
                            statusView.statusImage.setImageResource(R.drawable.image_packing)
                        }
                        "2" -> {
                            statusView.status.text = "On the way"
                            statusView.statusImage.setImageResource(R.drawable.image_on_the_way)
                        }
                        "3" -> {
                            statusView.status.text = "Delivered"
                            statusView.statusImage.setImageResource(R.drawable.image_delivered)
                        }
                        else -> {
                            statusView.status.text = "Pending"
                            statusView.statusImage.setImageResource(R.drawable.image_packing)
                        }
                    }
                }
            },{ error ->
                Log.d(TAG, error.toString())
                Toast.makeText(requireActivity(), error.message, Toast.LENGTH_SHORT).show()
            }
        )
        MainActivity.appContext.addToRequestQueue(statusRequest)
    }
}