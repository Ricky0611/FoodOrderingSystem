package com.example.rikki.foodorderingsystem

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.rikki.foodorderingsystem.model.OrderItem
import java.util.*

class OrderAdapter(val context: Context, private val list: ArrayList<OrderItem>) : RecyclerView.Adapter<OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.order_view, parent, false)
        return OrderViewHolder(v)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val item = list[position]
        holder.id.text = item.id
        holder.name.text = item.name
        holder.quantity.text = item.quantity
        holder.totalCost.text = item.totalCost
        holder.address.text = item.address
        holder.date.text = item.date
        holder.trackBtn.setOnClickListener {
            // track order
            (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_replaceable, OrderTrackFragment.newInstance(item.id))
                .commit()
        }
        holder.reviewBtn.setOnClickListener {
            // write a review
            (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_replaceable, ReviewFragment.newInstance(item.name))
                .commit()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class OrderViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    var id: TextView = v.findViewById<View>(R.id.orderItemIdText) as TextView
    var name: TextView = v.findViewById<View>(R.id.orderItemNameText) as TextView
    var quantity: TextView = v.findViewById<View>(R.id.orderItemQuantityText) as TextView
    var totalCost: TextView = v.findViewById<View>(R.id.totalOrderItemText) as TextView
    var address: TextView = v.findViewById<View>(R.id.orderDeliverAddText) as TextView
    var date: TextView = v.findViewById<View>(R.id.orderItemDateText) as TextView
    var trackBtn: Button = v.findViewById<View>(R.id.trackBtn) as Button
    var reviewBtn: Button = v.findViewById<View>(R.id.reviewBtn) as Button
}