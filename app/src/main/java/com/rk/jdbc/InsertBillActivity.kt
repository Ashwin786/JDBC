package com.rk.jdbc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_insert_bill.*
import kotlinx.android.synthetic.main.activity_insert_bill.btn_login
import java.sql.ResultSet

class InsertBillActivity : AppCompatActivity(), Call_back {
    override fun response(result: ResultSet, type: ServiceType) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_bill)
        rg_btn.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (R.id.rb_rural == checkedId) {
                InsertBill.getInstance(this).rural = true
            } else
                InsertBill.getInstance(this).rural = false
        })
        btn_login.setOnClickListener {
            if (rg_btn.getCheckedRadioButtonId() == -1) {
                // no radio buttons are checked
                Toast.makeText(this, "Kindly select any one database", Toast.LENGTH_SHORT).show()
            } else
                InsertBill.getInstance(this).insertBill(this)
        }
    }
}
