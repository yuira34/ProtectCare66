package com.ubaya.protectcare66

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainAdapter(activity: AppCompatActivity, val fragment: ArrayList<Fragment>): FragmentStateAdapter(activity) {
    override fun getItemCount() = fragment.size

    override fun createFragment(position: Int): Fragment = fragment[position]
}