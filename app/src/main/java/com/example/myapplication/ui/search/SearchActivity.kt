package com.example.myapplication.ui.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.AdapterSearch
import com.example.myapplication.model.search.EventItem
import com.example.myapplication.model.search.ResponseSearch
import com.example.myapplication.services.ApiClient
import com.example.myapplication.services.ApiInterface
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setTitle("Pencarian pertandingan")

        rv.layoutManager = LinearLayoutManager(this)

        val apiInterface: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        btnCari.setOnClickListener {
            getQuery(apiInterface, edQuery.text.toString())
        }
    }

    fun getQuery(apiInterface: ApiInterface, query: String) {
        loading.visibility = View.VISIBLE
        val call: Call<ResponseSearch> = apiInterface.getSearch(query)

        call.enqueue(object : Callback<ResponseSearch> {
            override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                Toast.makeText(this@SearchActivity, "error " + t.message, Toast.LENGTH_SHORT)
            }

            override fun onResponse(
                call: Call<ResponseSearch>,
                response: Response<ResponseSearch>
            ) {
                loading.visibility = View.GONE
                try {
                    var items: ArrayList<EventItem>
                    items = response.body()?.event?.filter { it?.strSport == "Soccer" } as ArrayList<EventItem>
                    rv.adapter = AdapterSearch(this@SearchActivity, items)
                } catch (err: Exception) {
                    Log.e("Error", err.printStackTrace().toString())
                    Toast.makeText(this@SearchActivity, "error " + err.message, Toast.LENGTH_SHORT)
                }

            }

        })
    }

}
