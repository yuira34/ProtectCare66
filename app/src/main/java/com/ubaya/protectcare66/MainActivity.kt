package com.ubaya.protectcare66

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_check_in.view.*
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    val fragments: ArrayList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var user = User()
        val shared:SharedPreferences
        val sharedName = packageName
        shared = getSharedPreferences(sharedName, Context.MODE_PRIVATE)

        val jsonUser = shared.getString(LoginActivity.SIGNED_USER, null)
        if (jsonUser != null) user = GsonBuilder().create().fromJson(jsonUser, User::class.java)

        fragments.add(LobbyFragment.newInstance(user))
        fragments.add(HistoryFragment.newInstance(user))
        fragments.add(ProfileFragment.newInstance(user))


        viewPager.adapter = MainAdapter(this@MainActivity, fragments)
        viewPager.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNav.selectedItemId = bottomNav.menu[position].itemId
            }
        })

        bottomNav.setOnItemSelectedListener {
            viewPager.currentItem = when(it.itemId) {
                R.id.itemCheckIn -> 0
                R.id.itemHistory -> 1
                R.id.itemProfile -> 2
                else -> 0
            }
            true
        }
    }

    override fun onBackPressed() {
        finish()
    }
}