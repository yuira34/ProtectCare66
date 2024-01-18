package com.ubaya.protectcare66

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import org.json.JSONArray
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USER = "user"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var user = User()

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
        return inflater.inflate(R.layout.fragment_history, container, false).apply {
            val q = Volley.newRequestQueue(context)
            val url = "https://ubaya.fun/native/160419112/history.php"
            val strReq = object : StringRequest(
                Method.POST, url, {
                    Log.d("checkparams", it)
                    val arrayOfLocations = JSONArray(it)
                    var histories:ArrayList<History> = ArrayList()
                    for (i in 0 until arrayOfLocations.length()) {
                        val histStr = arrayOfLocations.getJSONObject(i).toString()
                        val history = GsonBuilder().create().fromJson(histStr, History::class.java)
                        histories.add(history)
                    }
                    val layouting = LinearLayoutManager(context)
                    historyRecycler.apply {
                        layoutManager = layouting
                        setHasFixedSize(true)
                        adapter = HistoryAdapter(histories)
                    }
                },
                {
                    Log.e("paramserror", it.message.toString())
                }) { override fun getParams() = hashMapOf("user_id" to user.id.toString()) }

            q.add(strReq)
        }
    }

    override fun onResume() {
        super.onResume()

        val q = Volley.newRequestQueue(context)
        val url = "https://ubaya.fun/native/160419112/history.php"
        val strReq = object : StringRequest(
            Method.POST, url, {
                Log.d("checkparams", it)
                val arrayOfLocations = JSONArray(it)
                var histories:ArrayList<History> = ArrayList()
                for (i in 0 until arrayOfLocations.length()) {
                    val histStr = arrayOfLocations.getJSONObject(i).toString()
                    val history = GsonBuilder().create().fromJson(histStr, History::class.java)
                    histories.add(history)
                }
                val layouting = LinearLayoutManager(context)
                historyRecycler.apply {
                    layoutManager = layouting
                    setHasFixedSize(true)
                    adapter = HistoryAdapter(histories)
                }
            },
            {
                Log.e("paramserror", it.message.toString())
            }) { override fun getParams() = hashMapOf("user_id" to user.id.toString()) }

        q.add(strReq)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(user: User) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_USER, user)
                }
            }
    }
}