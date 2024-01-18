package com.ubaya.protectcare66

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val id:Int=0, val name:String="", val vaccination:Int=0, var checkedIn:String=""): Parcelable {
    override fun toString() = name
}
