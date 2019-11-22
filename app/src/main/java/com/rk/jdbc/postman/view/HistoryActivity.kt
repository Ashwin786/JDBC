package com.rk.jdbc.postman.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.rk.jdbc.R
import com.rk.jdbc.postman.data.local.PostmanDatabase
import com.rk.jdbc.postman.data.model.ApiDbDto
import com.rk.jdbc.postman.view.history.HistoryAdapter
import com.rk.jdbc.postman.view.history.HistoryViewModel
import java.util.ArrayList

class HistoryActivity : AppCompatActivity() {
    private var adapter: HistoryAdapter? = null
    private lateinit var model: HistoryViewModel
    private var postmanDb: PostmanDatabase? = null
    private var recyclerview: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        /*Intializing recyclerview*/
        recyclerview = findViewById<View>(R.id.recyclerview) as RecyclerView
        val llm = LinearLayoutManager(applicationContext)
        recyclerview!!.setLayoutManager(llm)

        /*Intializing room database*/
        postmanDb = PostmanDatabase.getDatabase(this)

        /*Intializing view model*/
        model = ViewModelProviders.of(this).get(HistoryViewModel::class.java!!)

        /*Intializing live data observer*/
        val dbObserver = object : Observer<List<ApiDbDto>> {
            override fun onChanged(list: List<ApiDbDto>?) {
                // Update the UI, in this case, a TextView.

                /*Checking is adapter is null or intialized*/
                if (adapter != null) {
                    /*Refresing the recyclerview*/
                    adapter!!.notifyDataSetChanged()
                } else {
                    /*Intializing the adapter and setting to recyclerview*/
                    adapter = HistoryAdapter(this@HistoryActivity, list)
                    recyclerview!!.adapter = adapter
                }
            }
        }
        model.getApiDbData().observe(this, dbObserver)
    }
}
