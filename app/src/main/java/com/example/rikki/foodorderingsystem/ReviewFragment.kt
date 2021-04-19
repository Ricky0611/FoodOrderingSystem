package com.example.rikki.foodorderingsystem

import android.app.AlertDialog
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rikki.foodorderingsystem.MainActivity.Companion.appContext
import com.example.rikki.foodorderingsystem.model.ReviewItem
import com.example.rikki.foodorderingsystem.utils.DbHelper
import com.google.android.material.textfield.TextInputEditText
import java.text.DateFormat
import java.util.*

class ReviewFragment : Fragment() {

    companion object {
        const val FoodName = "foodname"
        @JvmStatic
        fun newInstance(name: String) =
            ReviewFragment().apply {
                arguments = Bundle().apply {
                    putString(FoodName, name)
                }
            }
    }

    private lateinit var foodName: String
    private lateinit var userName: String
    private lateinit var rate: String
    private lateinit var date: String
    private lateinit var review: String
    private lateinit var star1: ImageView
    private lateinit var star2: ImageView
    private lateinit var star3: ImageView
    private lateinit var star4: ImageView
    private lateinit var star5: ImageView
    private lateinit var reviewText: TextInputEditText
    private lateinit var cancelBtn: Button
    private lateinit var submitBtn: Button
    private lateinit var emptyReviewCard: CardView
    private lateinit var preReviewCard: CardView
    private lateinit var reviewRecyclerView: RecyclerView

    private lateinit var list : ArrayList<ReviewItem>
    private var numOfReview = 0
    private lateinit var dbHelper: DbHelper
    private lateinit var db: SQLiteDatabase
    private lateinit var cursor: Cursor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            foodName = it.getString(FoodName).toString()
            userName = appContext.getUserName()
            rate = "0"
            review = ""
            dbHelper = DbHelper(requireActivity().applicationContext)
            db = dbHelper.writableDatabase
            list = ArrayList()
            cursor = dbHelper.searchRecord(foodName)
            numOfReview = cursor.count
            if (numOfReview > 0) {
                var position = 0
                while (cursor.moveToNext()) {
                    val item = ReviewItem(
                        cursor.getString(cursor.getColumnIndex(DbHelper.USERNAME)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.FOODNAME)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.RATE)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.DATE)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.REVIEW))
                    )
                    list.add(position, item)
                    position++
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_review, container, false)
        // get View components
        star1 = v.findViewById<View>(R.id.star1) as ImageView
        star2 = v.findViewById<View>(R.id.star2) as ImageView
        star3 = v.findViewById<View>(R.id.star3) as ImageView
        star4 = v.findViewById<View>(R.id.star4) as ImageView
        star5 = v.findViewById<View>(R.id.star5) as ImageView
        reviewText = v.findViewById<View>(R.id.reviewText) as TextInputEditText
        cancelBtn = v.findViewById<View>(R.id.button3) as Button
        submitBtn = v.findViewById<View>(R.id.button2) as Button
        emptyReviewCard = v.findViewById<View>(R.id.emptyReviewCard) as CardView
        preReviewCard = v.findViewById<View>(R.id.preReviewCard) as CardView
        reviewRecyclerView = v.findViewById<View>(R.id.reviewRecyclerView) as RecyclerView
        // get reviews from DB for this item
        if (numOfReview == 0) {
            // no previous reviews
            emptyReviewCard.visibility = View.VISIBLE
            preReviewCard.visibility = View.GONE
        } else {
            // show previews reviews
            emptyReviewCard.visibility = View.GONE
            preReviewCard.visibility = View.VISIBLE
            val linearLayoutManager = LinearLayoutManager(activity)
            linearLayoutManager.stackFromEnd = true
            linearLayoutManager.reverseLayout = true
            reviewRecyclerView.layoutManager = linearLayoutManager
            val adapter = ReviewAdapter(requireActivity(), list)
            reviewRecyclerView.adapter = adapter
        }
        // set view actions
        star1.setOnClickListener(View.OnClickListener {
            star1.setImageResource(R.drawable.star_select)
            star2.setImageResource(R.drawable.star_blank)
            star3.setImageResource(R.drawable.star_blank)
            star4.setImageResource(R.drawable.star_blank)
            star5.setImageResource(R.drawable.star_blank)
            rate = "1"
        })
        star2.setOnClickListener(View.OnClickListener {
            star1.setImageResource(R.drawable.star_select)
            star2.setImageResource(R.drawable.star_select)
            star3.setImageResource(R.drawable.star_blank)
            star4.setImageResource(R.drawable.star_blank)
            star5.setImageResource(R.drawable.star_blank)
            rate = "2"
        })
        star3.setOnClickListener(View.OnClickListener {
            star1.setImageResource(R.drawable.star_select)
            star2.setImageResource(R.drawable.star_select)
            star3.setImageResource(R.drawable.star_select)
            star4.setImageResource(R.drawable.star_blank)
            star5.setImageResource(R.drawable.star_blank)
            rate = "3"
        })
        star4.setOnClickListener(View.OnClickListener {
            star1.setImageResource(R.drawable.star_select)
            star2.setImageResource(R.drawable.star_select)
            star3.setImageResource(R.drawable.star_select)
            star4.setImageResource(R.drawable.star_select)
            star5.setImageResource(R.drawable.star_blank)
            rate = "4"
        })
        star5.setOnClickListener(View.OnClickListener {
            star1.setImageResource(R.drawable.star_select)
            star2.setImageResource(R.drawable.star_select)
            star3.setImageResource(R.drawable.star_select)
            star4.setImageResource(R.drawable.star_select)
            star5.setImageResource(R.drawable.star_select)
            rate = "5"
        })
        cancelBtn.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.fragment_replaceable,
                OrdersFragment()
            ).commit()
        }
        submitBtn.setOnClickListener{
            review = reviewText.getText().toString()
            if (rate == "0" || review == "") {
                // show alert
                val alertDialog = AlertDialog.Builder(requireActivity()).apply {
                    setTitle("Attention")
                    setMessage("Cannot submit an empty review!")
                    setCancelable(true)
                    setPositiveButton("OK") { dialog, which ->
                        dialog.cancel()
                    }
                }.create()
                alertDialog.show()
            } else {
                // add to DB
                date = DateFormat.getDateInstance().format(Date())
                dbHelper.addRecord(null, userName, foodName, rate, date, review)
                // reload fragment
                requireActivity().supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_replaceable, ReviewFragment.newInstance(
                        foodName
                    )
                ).commit()
            }
        }
        return v
    }
}