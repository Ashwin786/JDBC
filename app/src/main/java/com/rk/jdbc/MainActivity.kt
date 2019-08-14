package com.rk.jdbc

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.ResultSet
import java.util.ArrayList

class MainActivity : AppCompatActivity(), Call_back {


    override fun response(result: ResultSet, type: ServiceType) {

        when (type) {
            ServiceType.LOGIN -> database_List(result)
        }
        dismissProgress()
    }

    private fun database_List(result: ResultSet) {
        val db_list = ArrayList<String>()
        if (result != null) {
            db_list.add("Select Database")
            while (result.next()) {
                if (result.getString("TABLE_CAT") != "information_schema")
                    db_list.add(result.getString("TABLE_CAT"))
                Log.e("Database", "" + result.getString("TABLE_CAT"))
            }
        }
        if (db_list != null && db_list.size > 0) {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putStringArrayListExtra("database_list", db_list)
            startActivity(intent)
        }
    }

    var ed_password: EditText? = null

    private var pd: ProgressDialog ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val jdbc = Jdbc.getInstance(this)
        /*var connection = getSharedPreferences("jdbc", Context.MODE_PRIVATE).getString("connection", null)
        if (connection != null) {
            jdbc.getConnection(connection,ServiceType.LOGIN)
            return
        }*/
        setContentView(R.layout.activity_main)

        ed_password = findViewById<EditText>(R.id.ed_password)
        ed_db_url.setText("192.168.1.103")
        ed_db_port.setText("3306")
        ed_username.setText("anilalluri")
        ed_password!!.setText("an!l@lluri@67")

//        (ed_db_url as EditText?).text
//         button = findViewById<Button>(R.id.btn_login) as Button


        btn_login.setOnClickListener {
            if (TextUtils.isEmpty(ed_db_url?.text)) {
                Toast.makeText(this, "Kindly enter the database url", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(ed_db_port?.text)) {
                Toast.makeText(this, "Kindly enter the port number", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(ed_username?.text)) {
                Toast.makeText(this, "Kindly enter the username", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(ed_password?.text)) {
                Toast.makeText(this, "Kindly enter the password", Toast.LENGTH_SHORT).show();
            } else {

                login(jdbc);

            }
        }



    }

    private fun login(jdbc: Jdbc) {
        show_progress()
        jdbc.login(ed_db_url?.text.toString(), ed_db_port?.text.toString(), ed_username?.text.toString(), ed_password?.text.toString(), ServiceType.LOGIN)

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
