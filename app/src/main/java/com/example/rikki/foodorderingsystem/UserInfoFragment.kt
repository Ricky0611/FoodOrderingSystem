package com.example.rikki.foodorderingsystem

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.example.rikki.foodorderingsystem.MainActivity.Companion.appContext
import kotlinx.android.synthetic.main.fragment_user_info.view.*
import kotlinx.android.synthetic.main.shipping_fill_layout.view.*
import org.json.JSONObject

class UserInfoFragment : Fragment() {

    private lateinit var country : String
    private lateinit var state : String
    private lateinit var addressView : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_info, container, false)
        val shippingView = view.shippingLayout
        addressView = view.user_default_location
        // init
        shippingLayoutDeatils(shippingView)
        view.nameText.text = appContext.getUserName()
        view.emailText.text = appContext.getUserEmail()
        view.mobileText.text = appContext.getUserMobile()
        addressView.text = appContext.getUserAddress()
        if (appContext.getUserUpdatedAddress().isNotBlank()) {
            addressView.text = appContext.getUserUpdatedAddress()
        }
        // reset
        view.personal_info_reset.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_replaceable, ResetFragment()).commit()
                addToBackStack(null)
            }
        }
        // update address
        view.personal_info_update_address.setOnClickListener {
            if (shippingView.isShown) {
                shippingView.visibility = View.GONE
            } else {
                shippingView.visibility = View.VISIBLE
            }
        }
        // logout
        view.personal_info_logout.setOnClickListener {
            appContext.clearUserInfo().let {
                if (it) {
                    Toast.makeText(requireActivity(), "Log out successfully!", Toast.LENGTH_SHORT).show()
                    // close app
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Log out failed. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        return view
    }

    private fun shippingLayoutDeatils(shippingView: View) {
        val stateAdapter = ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.states,
            android.R.layout.simple_spinner_dropdown_item
        )
        val countryAdapter = ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.countries,
            android.R.layout.simple_spinner_dropdown_item
        )
        shippingView.shippingState.apply {
            adapter = stateAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    state = parent?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
        shippingView.shippingCountry.apply {
            adapter = countryAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    country = parent?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
        shippingView.shippingAddressConfirm.setOnClickListener {
            if (checkInputs(shippingView)) {
                val apt = if (shippingView.aptorSuit.text.isNullOrBlank()) "" else shippingView.aptorSuit.text.toString()
                val shipmentAddress: String = (shippingView.streetAddress.text.toString() + " "
                        + apt + ", " + shippingView.shippingCity.text + ", " + state
                        + " " + shippingView.shippingZipcode.text.toString() + ", " + country)
                appContext.setUserUpdatedAddress(shipmentAddress)
                updateAddressRequest(shipmentAddress)
                shippingView.visibility = View.GONE
                addressView.text = shipmentAddress
            }
        }
    }

    private fun updateAddressRequest(address: String) {
        var password = appContext.getUserPassword()
        if (password.isBlank()) {
            password = getPassword()
        }
        val mobile = appContext.getUserMobile()
        val urlUpdate = String.format(
            resources.getString(R.string.url_update_address),
            mobile,
            password,
            address
        )
        val stringRequest = StringRequest(
            Request.Method.POST,
            urlUpdate,
            { response ->
                val msg = JSONObject(response).getJSONArray("msg").getString(0)
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            }, { error ->
                Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
            })
        appContext.addToRequestQueue(stringRequest)
    }

    private fun getPassword(): String {
        // custom dialog
        var password = ""
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_get_password)
        val passwordText = dialog.findViewById<View>(R.id.login_password) as EditText
        val saveBtn = dialog.findViewById<View>(R.id.saveBtn) as Button
        saveBtn.setOnClickListener {
            val pass = passwordText.text.toString()
            password = pass
            dialog.cancel()
        }
        dialog.show()
        return password
    }

    private fun checkInputs(shippingView: View): Boolean {
        var result = true
        if (shippingView.streetAddress.text.isNullOrBlank()) {
            shippingView.streetAddressLayout.error = "Please enter Street address"
            result = false
        }
        if (shippingView.shippingCity.text.isNullOrBlank()) {
            shippingView.cityLayout.error = "Please enter City"
            result = false
        }
        if (shippingView.shippingZipcode.text.isNullOrBlank()) {
            shippingView.zipCodeLayout.error = "Please enter Zipcode"
            result = false
        } else {
            try {
                shippingView.shippingZipcode.text.toString().toInt()
            } catch (e: Exception) {
                shippingView.zipCodeLayout.error = "Please enter a valid Zipcode"
                result = false
            }
        }
        return result
    }
}