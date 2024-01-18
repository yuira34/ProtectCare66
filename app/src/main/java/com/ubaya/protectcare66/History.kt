package com.ubaya.protectcare66

import android.annotation.SuppressLint
import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Parcelize
@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
data class History(val user_id:Int=0, val location_id:Int=0, var name:String="", var check_in:String="", val check_out:String="", val status:String=""): Parcelable {
    override fun toString() = name

    fun dateTimeFormatter(dtString: String): String {
        val inputForm = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val outputForm = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy, HH:mm")
        val dt = LocalDateTime.parse(dtString, inputForm)
        return dt.format(outputForm)
    }
}