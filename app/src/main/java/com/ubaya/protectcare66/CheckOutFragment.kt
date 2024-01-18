package com.ubaya.protectcare66

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_check_in.view.*
import kotlinx.android.synthetic.main.fragment_check_out.view.*
import kotlinx.android.synthetic.main.history_card.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_HIST = "history"

/**
 * A simple [Fragment] subclass.
 * Use the [CheckOutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckOutFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var history: History

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            history = it.getParcelable(ARG_HIST)!!
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_out, container, false).apply {
            if (history.status == "YELLOW")  CardViewOut.setCardBackgroundColor(Color.parseColor("#ffc107"))
            else CardViewOut.setCardBackgroundColor(Color.parseColor("#71c29a"))

            textViewCurLoc.text = history.name
            textViewCurrChk.text = "Check in time: ${history.dateTimeFormatter(history.check_in)}"

            val sharedName = context.packageName
            val shared: SharedPreferences? = activity?.getSharedPreferences(sharedName, Context.MODE_PRIVATE)

            buttonCheckOut.setOnClickListener {
                val checkOut: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()).toString()

                val qCheckOut = Volley.newRequestQueue(context)
                val urlCheckOut = "https://ubaya.fun/native/160419112/checkout.php"
                val strReqCheckOut = object : StringRequest(
                    Method.POST, urlCheckOut, {
                        Log.d("checkparams", it)
                        val obj = JSONObject(it)
                        if (obj.getString("status") == "success") {
                            val userStr = obj.getJSONObject("user").toString()
                            val user = GsonBuilder().create().fromJson(userStr, User::class.java)
                            shared?.edit()?.putString(LoginActivity.SIGNED_USER, userStr)?.apply()
                            activity?.supportFragmentManager?.beginTransaction()?.let {
                                it.replace(R.id.LobbyLayout, CheckInFragment.newInstance(user))
                                it.addToBackStack(null)
                                it.commit()
                            }
                        } else {
                            Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show()
                        }
                    },
                    {
                        Log.e("paramserror", it.message.toString())
                    }) { override fun getParams() = hashMapOf(
                    "user_id" to history.user_id.toString(),
                    "location_id" to history.location_id.toString(),
                    "check_out" to checkOut
                )
                }
                qCheckOut.add(strReqCheckOut)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CheckOutFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(history: History) =
            CheckOutFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_HIST, history)
                }
            }
    }
}