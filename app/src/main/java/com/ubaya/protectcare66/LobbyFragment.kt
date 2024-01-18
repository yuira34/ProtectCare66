package com.ubaya.protectcare66

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USER = "user"

/**
 * A simple [Fragment] subclass.
 * Use the [LobbyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LobbyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getParcelable(ARG_USER)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lobby, container, false).apply {
            if (user.checkedIn == "YES") {
                val qOut= Volley.newRequestQueue(context)
                val urlOut = "https://ubaya.fun/native/160419112/currently.php"
                val strReqOut = object : StringRequest(
                    Method.POST, urlOut, {
                        Log.d("checkparams", it)
                        val histStr = JSONObject(it).getJSONObject("checkedIn").toString()
                        val history = GsonBuilder().create().fromJson(histStr, History::class.java)
                        activity?.supportFragmentManager?.beginTransaction()?.let {
                            it.replace(R.id.LobbyLayout, CheckOutFragment.newInstance(history))
                            it.addToBackStack(null)
                            it.commit()
                        }
                    },
                    {
                        Log.e("paramserror", it.message.toString())
                    }) { override fun getParams() = hashMapOf( "user_id" to user.id.toString()) }
                qOut.add(strReqOut)
            } else {
                activity?.supportFragmentManager?.beginTransaction()?.let {
                    it.replace(R.id.LobbyLayout, CheckInFragment.newInstance(user))
                    it.addToBackStack(null)
                    it.commit()
                }
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
         * @return A new instance of fragment LobbyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(user: User) =
            LobbyFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_USER, user)
                }
            }
    }
}