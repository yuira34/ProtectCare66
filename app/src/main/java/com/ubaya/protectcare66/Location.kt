package com.ubaya.protectcare66

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(val id:Int=0, val name:String="", val ucode:String=""): Parcelable {
    override fun toString() = name
}
