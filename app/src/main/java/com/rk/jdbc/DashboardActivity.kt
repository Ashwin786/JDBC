package com.rk.jdbc

import android.app.ProgressDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.transition.Visibility
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.sql.ResultSet

class DashboardActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, Call_back {
    override fun response(result: ResultSet, type: ServiceType) {
        Log.e("response", "response");
        when (type) {
            ServiceType.OPEN_DB -> btn_showtable.visibility = View.VISIBLE
            ServiceType.SHOW_TABLE -> table_List(result)
        }
        dismissProgress()
    }

    private fun table_List(result: ResultSet) {
        var list: ArrayList<String> = ArrayList()
        while (result.next()) {
            list.add(result.getString(1))
            Log.e("entitle_rs : ", result.getString(1))
        }
    }

    private var pd: ProgressDialog? = null
    private var jdbc: Jdbc? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        jdbc = Jdbc.getInstance(this)
        var database_list = intent.getStringArrayListExtra("database_list");
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, database_list)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = aa;
        spinner.onItemSelectedListener = this;
        btn_showtable.setOnClickListener {
            jdbc?.show_tables(ServiceType.SHOW_TABLE)
        }
        var database = getSharedPreferences("jdbc", Context.MODE_PRIVATE).getString("database", "")
        if (!database.isNullOrEmpty()) {
            for (i in 0 until database_list.size) {
                if (database_list.get(i).equals(database))
                    spinner.setSelection(i)
//                break
            }


        }
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        Log.e("postiion", "" + position)
        if (position > 0) {
            show_progress()
            val selectedItem = arg0.getItemAtPosition(position).toString()
            getSharedPreferences("jdbc", Context.MODE_PRIVATE).edit().putString("database", selectedItem).commit()
            jdbc?.openDatabase(selectedItem, ServiceType.OPEN_DB);
        }
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }

    private fun dismissProgress() {
        if (pd != null && pd!!.isShowing) {
            pd!!.dismiss()
        }
    }

    private fun show_progress() {
        if (pd == null) {
            pd = ProgressDialog(this)
            pd!!.setMessage("loading")
        }

        if (!pd!!.isShowing)
            pd!!.show()
    }
}
