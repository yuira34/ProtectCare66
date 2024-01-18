package com.ubaya.protectcare66

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.history_card.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HistoryAdapter(val histories:ArrayList<History>): RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    class HistoryViewHolder(val v: View): RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var v = inflater.inflate(R.layout.history_card, parent, false)
        return HistoryViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = histories[position]
        with(holder.v) {
            var checkout = "Check out: Not yet Checked Out"
            var dtCheckIn = LocalDateTime.parse(history.check_in, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            if (history.status == "YELLOW")  locationCardView.setCardBackgroundColor(Color.parseColor("#ffc107"))
            else locationCardView.setCardBackgroundColor(Color.parseColor("#71c29a"))

            if (history.check_out != null) checkout = "Check out: ${history.dateTimeFormatter(history.check_out)}"

            locationTextView.text = history.name
            checkinTextView.text = "Check in: ${history.dateTimeFormatter(history.check_in)}"
            checkoutTextView.text = checkout

        }
    }

    override fun getItemCount() = histories.size
}