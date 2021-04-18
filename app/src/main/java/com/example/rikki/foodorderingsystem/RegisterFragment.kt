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
import com.example.rikki.foodorderingsystem.LoginActivity.Companion.SUCCESS
import kotlinx.android.synthetic.main.fragment_register.view.*

class RegisterFragment : Fragment() {

    private lateinit var regView: View
    private val TAG = this.javaClass.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        regView = inflater.inflate(R.layout.fragment_register, container, false)
        // register
        regView.regBtn.setOnClickListener {
            regView.loading.visibility = View.VISIBLE
            val username = regView.username.text.toString()
            val email = regView.email.text.toString()
            val phone = regView.phone.text.toString()
            val password = regView.password.text.toString()
            val address = regView.address.text.toString()
            if (checkInputs(username, email, phone, password, address)) {
                register(username, email, phone, password, address)
            } else {
                regView.loading.visibility = View.GONE
            }
        }
        return regView
    }

    private fun register(username: String, email: String, phone: String, password: String, address: String) {
        val regUrl = String.format(resources.getString(R.string.url_register), username, email, phone, password, address)
        val regRequest = StringRequest(
            Request.Method.GET,
            regUrl,
            { response ->
                Log.d(TAG, response)
                regView.loading.visibility = View.GONE
                if (response.contains(SUCCESS)) {
                    Toast.makeText(requireActivity(), response.capitalize().plus("!"), Toast.LENGTH_SHORT).show()
                    activity?.supportFragmentManager?.beginTransaction()?.apply {
                        setReorderingAllowed(true)
                        replace(R.id.container, LoginFragment())
                        commit()
                    }
                } else {
                    Toast.makeText(requireActivity(), response.capitalize().plus("!"), Toast.LENGTH_SHORT).show()
                }
            }, { error ->
                Log.d(TAG, error.toString())
                regView.loading.visibility = View.GONE
                Toast.makeText(requireActivity(), error.message, Toast.LENGTH_SHORT).show()
            })
        AppController.getInstance(requireActivity().applicationContext).addToRequestQueue(regRequest)
    }

    private fun checkInputs(username: String, email: String, phone: String, password: String, address: String): Boolean {
        var result = true
        if (username.isBlank()) {
            result = false
            regView.username.error = resources.getString(R.string.invalid_username)
        }
        if (email.isBlank()) {
            result = false
            regView.email.error = resources.getString(R.string.invalid_email)
        }
        if (phone.isBlank()) {
            result = false
            regView.phone.error = resources.getString(R.string.invalid_phone)
        }
        if (password.isBlank() || password.length < 4) {
            result = false
            regView.password.error = resources.getString(R.string.invalid_password)
        }
        if (address.isBlank()) {
            result = false
            regView.address.error = resources.getString(R.string.invalid_address)
        }
        return result
    }
}