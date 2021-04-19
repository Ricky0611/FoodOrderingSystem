package com.example.rikki.foodorderingsystem

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.example.rikki.foodorderingsystem.LoginActivity.Companion.SUCCESS
import com.example.rikki.foodorderingsystem.utils.AppController
import kotlinx.android.synthetic.main.fragment_login.view.*
import org.json.JSONArray

class LoginFragment : Fragment() {

    private val TAG = this.javaClass.simpleName
    private lateinit var loginView: View
    private lateinit var phoneText: EditText
    private lateinit var pswdText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginView = inflater.inflate(R.layout.fragment_login, container, false)
        phoneText = loginView.username
        pswdText = loginView.password
        // login
        loginView.loginBtn.setOnClickListener {
            loginView.loading.visibility = View.VISIBLE
            val phone = phoneText.text.toString()
            val password = pswdText.text.toString()
            if (checkInputs(phone, password)) {
                login(phone, password)
            } else {
                loginView.loading.visibility = View.GONE
            }
        }
        // register
        loginView.regBtn.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                setReorderingAllowed(true)
                replace(R.id.container, RegisterFragment())
                addToBackStack(null)
                commit()
            }
        }
        // reset
        loginView.resetBtn.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                setReorderingAllowed(true)
                replace(R.id.container, ResetFragment())
                addToBackStack(null)
                commit()
            }
        }
        return loginView
    }

    private fun login(phone: String, password: String) {
        val loginUrl = String.format(resources.getString(R.string.url_login), phone, password)
        val loginRequest = StringRequest(
            Request.Method.GET,
            loginUrl,
            { response ->
                Log.d(TAG, response)
                loginView.loading.visibility = View.GONE
                if (response.contains(SUCCESS)) {
                    val res = JSONArray(response).getJSONObject(0)
                    AppController.getInstance(requireActivity().applicationContext).saveUserInfo(
                        res.getString("UserName"),
                        password,
                        res.getString("UserMobile"),
                        res.getString("UserEmail"),
                        res.getString("UserAddress")
                    )
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireActivity(), resources.getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
                }
            }, { error ->
                Log.d(TAG, error.toString())
                loginView.loading.visibility = View.GONE
                Toast.makeText(requireActivity(), error.message, Toast.LENGTH_SHORT).show()
            })
        AppController.getInstance(requireActivity().applicationContext).addToRequestQueue(loginRequest)
    }

    private fun checkInputs(mobile: String, password: String) : Boolean {
        var result = true
        if (mobile.isBlank()) {
            result = false
            phoneText.error = resources.getString(R.string.invalid_phone)
        }
        if (password.isBlank() || password.length < 4) {
            result = false
            pswdText.error = resources.getString(R.string.invalid_password)
        }
        return result
    }
}