package com.example.rikki.foodorderingsystem

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rikki.foodorderingsystem.model.ReviewItem
import java.util.*

class ReviewAdapter(val context: Context, private val list: ArrayList<ReviewItem>) : RecyclerView.Adapter<ReviewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.review_view, parent, false)
        return ReviewHolder(v)
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
        val item = list[position]
        holder.user.text = item.userName
        holder.date.text = item.date
        holder.review.text = item.review
        when (item.rate) {
            "1" -> holder.star1.setImageResource(R.drawable.star_select)
            "2" -> {
                holder.star1.setImageResource(R.drawable.star_select)
                holder.star2.setImageResource(R.drawable.star_select)
            }
            "3" -> {
                holder.star1.setImageResource(R.drawable.star_select)
                holder.star2.setImageResource(R.drawable.star_select)
                holder.star3.setImageResource(R.drawable.star_select)
            }
            "4" -> {
                holder.star1.setImageResource(R.drawable.star_select)
                holder.star2.setImageResource(R.drawable.star_select)
                holder.star3.setImageResource(R.drawable.star_select)
                holder.star4.setImageResource(R.drawable.star_select)
            }
            "5" -> {
                holder.star1.setImageResource(R.drawable.star_select)
                holder.star2.setImageResource(R.drawable.star_select)
                holder.star3.setImageResource(R.drawable.star_select)
                holder.star4.setImageResource(R.drawable.star_select)
                holder.star5.setImageResource(R.drawable.star_select)
            }
            else -> {
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class ReviewHolder(v: View) : RecyclerView.ViewHolder(v) {
    var user: TextView = v.findViewById<View>(R.id.reviewUser) as TextView
    var date: TextView = v.findViewById<View>(R.id.reviewDate) as TextView
    var review: TextView = v.findViewById<View>(R.id.userReview) as TextView
    var star1: ImageView = v.findViewById<View>(R.id.rate_star1) as ImageView
    var star2: ImageView = v.findViewById<View>(R.id.rate_star2) as ImageView
    var star3: ImageView = v.findViewById<View>(R.id.rate_star3) as ImageView
    var star4: ImageView = v.findViewById<View>(R.id.rate_star4) as ImageView
    var star5: ImageView = v.findViewById<View>(R.id.rate_star5) as ImageView

}