package com.ubaya.protectcare66

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    companion object {
        val SIGNED_USER = "signed_user"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val intent = Intent(this, MainActivity::class.java)
        val shared:SharedPreferences
        val sharedName = packageName
        shared = getSharedPreferences(sharedName, Context.MODE_PRIVATE)

        val jsonUser = shared.getString(SIGNED_USER, null)
        if (jsonUser != null) {
            startActivity(intent)
            finish()
        }

        buttonSignIn.setOnClickListener {
            val uname = InputUsername.text.toString()
            val pword = InputPassword.text.toString()
            val q = Volley.newRequestQueue(this)
            val url = "https://ubaya.fun/native/160419112/signin.php"
            val strReq = object : StringRequest(
                Method.POST, url, {
                    Log.d("checkparams", it)
                    val obj = JSONObject(it)
                    if (obj.getString("status") == "success") {
                        val jsonUser = obj.getJSONObject("user").toString()
                        shared.edit().putString(SIGNED_USER, jsonUser).apply()
                        startActivity(intent)
                        finish()
                    } else {
                        val message = obj.getString("message")
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                },
                {
                    Log.e("paramserror", it.message.toString())
                }) { override fun getParams() = hashMapOf("username" to uname, "password" to pword) }
            q.add(strReq)
        }
    }
}