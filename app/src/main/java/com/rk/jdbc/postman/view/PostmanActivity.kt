package com.rk.jdbc.postman.view

import android.content.Intent
import android.content.res.AssetManager
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import com.rk.jdbc.R
import com.rk.jdbc.postman.data.model.ApiDbDto
import com.rk.jdbc.postman.main.PlaceholderFragment
import com.rk.jdbc.postman.main.SectionsPagerAdapter
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.TextView
import com.rk.jdbc.postman.util.Sha
import java.io.*


class PostmanActivity : AppCompatActivity() {
    private lateinit var tabs: TabLayout
    lateinit var viewPager: ViewPager
    lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postman)
        viewPager = findViewById(R.id.view_pager)
        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.addFragment(PlaceholderFragment(), "Tab " + (sectionsPagerAdapter.count+1))
        viewPager.adapter = sectionsPagerAdapter
        tabs = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        setCustomTab()
        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { view ->
            sectionsPagerAdapter.addFragment(PlaceholderFragment(), "Tab " + (sectionsPagerAdapter.count+1))
            sectionsPagerAdapter.notifyDataSetChanged()
            setCustomTab()
//            tabs.newTab()

        }

        val historyView: ImageView = findViewById(R.id.iv_history)
        historyView.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }

    private fun setCustomTab() {
        Log.e("tabs.tabCount", "" + tabs.tabCount)
        for (i in 0 until tabs.tabCount) {
            val relative = LayoutInflater.from(this).inflate(R.layout.custom_tab, null) as RelativeLayout
            val iv_close = relative.findViewById<ImageView>(R.id.iv_close)
            val tv_tabName = relative.findViewById<TextView>(R.id.tv_tabName)
            tv_tabName.setText("Tab "+(i+1))
            iv_close.setOnClickListener {
                if (sectionsPagerAdapter.count > 1) {
                    sectionsPagerAdapter.removeFragment(tabs.selectedTabPosition);
                    sectionsPagerAdapter.notifyDataSetChanged()
                    setCustomTab()
                }
//                tabs.removeTabAt(tabs.selectedTabPosition)
            }
//        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_dash26, 0, 0)
            tabs.getTabAt(i)!!.setCustomView(relative)
        }


    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.e("PostmanActivity", "onNewIntent");
        var apiDbDto = intent!!.getParcelableExtra<Parcelable>("postman")
        if (apiDbDto != null) {
            var fragment = sectionsPagerAdapter.getItem(viewPager.currentItem) as PlaceholderFragment
            fragment.updateView(apiDbDto as ApiDbDto)

//            apiDbDto.url
            Log.e("dto", "" + fragment.what);
        }
    }



}