package com.ubaya.protectcare66

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_check_in.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USER = "user"

/**
 * A simple [Fragment] subclass.
 * Use the [CheckInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckInFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getParcelable(ARG_USER)!!
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_in, container, false).apply {
            var locations:ArrayList<Location> = ArrayList()

            val sharedName = context.packageName
            val shared: SharedPreferences? = activity?.getSharedPreferences(sharedName, Context.MODE_PRIVATE)

            val q = Volley.newRequestQueue(context)
            val url = "https://ubaya.fun/native/160419112/locations.php"
            val strReq = StringRequest(
                Request.Method.POST, url, {
                    Log.d("locations", it)
                    val arrayOfLocations = JSONArray(it)
                    for (i in 0 until arrayOfLocations.length()) {
                        val locStr = arrayOfLocations.getJSONObject(i).toString()
                        val location = GsonBuilder().create().fromJson(locStr, Location::class.java)
                        locations.add(location)
                    }
                    val spinAdapter = ArrayAdapter(context, R.layout.procare_layout, locations)
                    spinAdapter.setDropDownViewResource(R.layout.procare_item_layout)
                    spinLocation.adapter = spinAdapter
                },
                {
                    Log.e("paramserror", it.message.toString())
                })
            q.add(strReq)

            checkInButton.setOnClickListener {
                val location = spinLocation.selectedItem as Location
                val checkIn: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()).toString()
                var status = "YELLOW"
                if (user.vaccination > 1) status = "GREEN"

                val qCheckIn = Volley.newRequestQueue(context)
                val urlCheckIn = "https://ubaya.fun/native/160419112/checkin.php"
                val strReqCheckIn = object : StringRequest(
                    Method.POST, urlCheckIn, {
                        Log.d("checkin", it)
                        val obj = JSONObject(it)
                        if (obj.getString("status") == "success") {
                            user.checkedIn = "YES"
                            val jsonUser = GsonBuilder().create().toJson(user)
                            shared?.edit()?.putString(LoginActivity.SIGNED_USER, jsonUser)?.apply()

                            val histStr = obj.getJSONObject("checkedIn").toString()
                            val history = GsonBuilder().create().fromJson(histStr, History::class.java)
                            activity?.supportFragmentManager?.beginTransaction()?.let {
                                it.replace(R.id.LobbyLayout, CheckOutFragment.newInstance(history))
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
                            "user_id" to user.id.toString(),
                            "location_id" to location.id.toString(),
                            "check_in" to checkIn,
                            "status" to status
                        )
                    }

                if (UcodeInput.text.toString() == location.ucode && user.vaccination > 0) qCheckIn.add(strReqCheckIn)
                else if (UcodeInput.text.toString() == location.ucode) Toast.makeText(context, "Cannot check in due to lack of vaccination dose.", Toast.LENGTH_SHORT).show()
                else Toast.makeText(context, "Invalid Unique Code.", Toast.LENGTH_SHORT).show()
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
         * @return A new instance of fragment CheckInFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(user:User) =
            CheckInFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_USER, user)
                }
            }
    }
}