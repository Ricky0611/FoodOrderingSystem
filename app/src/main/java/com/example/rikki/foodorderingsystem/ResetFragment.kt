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
import kotlinx.android.synthetic.main.fragment_register.view.*
import kotlinx.android.synthetic.main.fragment_reset.*
import kotlinx.android.synthetic.main.fragment_reset.view.*
import kotlinx.android.synthetic.main.fragment_reset.view.loading

class ResetFragment : Fragment() {

    private lateinit var resetView: View
    private val TAG = this.javaClass.simpleName

    companion object {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        resetView = inflater.inflate(R.layout.fragment_reset, container, false)
        // reset password
        resetView.resetBtn.setOnClickListener {
            resetView.loading.visibility = View.VISIBLE
            val username = resetView.mobile.text.toString()
            val oldPassword = resetView.oldPswd.text.toString()
            val newPassword = resetView.newPswd.text.toString()
            val confirmPassword = confirmPswd.text.toString()
            if (checkInputs(username, oldPassword, newPassword, confirmPassword)) {
                reset(username, oldPassword, newPassword)
            } else {
                resetView.loading.visibility = View.GONE
            }
        }
        return resetView
    }

    private fun reset(username: String, oldPassword: String,newPassword: String) {
        val resetUrl = String.format(resources.getString(R.string.url_reset), username, oldPassword, newPassword)
        val resetRequest = StringRequest(
            Request.Method.GET,
            resetUrl,
            { response ->
                Log.d(TAG, response)
                resetView.loading.visibility = View.GONE
                if (response.contains(LoginActivity.SUCCESS)) {
                    Toast.makeText(requireActivity(), resources.getString(R.string.reset_success), Toast.LENGTH_SHORT).show()
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
                resetView.loading.visibility = View.GONE
                Toast.makeText(requireActivity(), error.message, Toast.LENGTH_SHORT).show()
            }
        )
        AppController.getInstance(requireActivity().applicationContext).addToRequestQueue(resetRequest)
    }

    private fun checkInputs(username: String, oldPassword: String, newPassword: String, confirmPassword: String): Boolean {
        var result = true
        if (username.isBlank()) {
            result = false
            resetView.username.error = resources.getString(R.string.invalid_username)
        }
        if (oldPassword.isBlank()) {
            result = false
            resetView.oldPswd.error = resources.getString(R.string.invalid_password)
        }
        if (newPassword.isBlank() || newPassword.length < 4) {
            result = false
            resetView.newPswd.error = resources.getString(R.string.invalid_password)
        }
        if (confirmPassword.isBlank() || confirmPassword.length < 4) {
            result = false
            resetView.confirmPswd.error = resources.getString(R.string.invalid_password)
        }
        if (newPassword != confirmPassword) {
            result = false
            resetView.confirmPswd.error = resources.getString(R.string.invalid_password_match)
        }
        return result
    }
}