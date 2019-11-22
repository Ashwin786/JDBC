package com.rk.jdbc

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_insert_bill.*
import java.sql.ResultSet
import java.text.SimpleDateFormat
import java.util.*


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
        rg_btn_port.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (R.id.rb_port == checkedId) {
                InsertBill.getInstance(this).with_portable = true
                linearLayout4.visibility = View.VISIBLE
            } else {
                InsertBill.getInstance(this).with_portable = false
                linearLayout4.visibility = View.GONE
            }
        })
        rg_btn_partial.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (R.id.rb_partial == checkedId) {
                InsertBill.getInstance(this).with_partial = true
                ll_partial.visibility = View.VISIBLE
            } else {
                InsertBill.getInstance(this).with_partial = false
                ll_partial.visibility = View.GONE
            }
        })
        btn_login.setOnClickListener {
            InsertBill.getInstance(this).portablity_percentage = 0
            InsertBill.getInstance(this).fromDate = null
            InsertBill.getInstance(this).toDate = null
            InsertBill.getInstance(this).store_partial_ = 0
            InsertBill.getInstance(this).benef_partial_ = 0
            if (ti_fromDate.text.toString().equals("From Date"))
                Toast.makeText(this, "Kindly enter from date", Toast.LENGTH_SHORT).show()
            else if (ti_toDate.text.toString().equals("To Date"))
                Toast.makeText(this, "Kindly enter to date", Toast.LENGTH_SHORT).show()
            else if (from_time > to_time)
                Toast.makeText(this, "Kindly choose from date less than to date", Toast.LENGTH_SHORT).show()
            else if (rg_btn.getCheckedRadioButtonId() == -1) {
                // no radio buttons are checked
                Toast.makeText(this, "Kindly select any one database", Toast.LENGTH_SHORT).show()
            } else if (rg_btn_port.getCheckedRadioButtonId() == -1) {
                // no radio buttons are checked
                Toast.makeText(this, "Kindly select any portability option", Toast.LENGTH_SHORT).show()
            } else if (rg_btn_partial.getCheckedRadioButtonId() == -1) {
                // no radio buttons are checked
                Toast.makeText(this, "Kindly select any partial option", Toast.LENGTH_SHORT).show()
            } else {
                if (rg_btn_port.getCheckedRadioButtonId() == R.id.rb_port) {
                    if (ed_portable_percentage.text.isNullOrEmpty()) {
                        Toast.makeText(this, "Kindly enter portable percentage", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    } else if (ed_portable_percentage.text.toString().toInt() > 100) {
                        Toast.makeText(this, "Kindly enter percentage below 100", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    } else if (ed_portable_percentage.text.toString().toInt() < 10) {
                        Toast.makeText(this, "Kindly enter percentage above 10", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    InsertBill.getInstance(this).portablity_percentage = ed_portable_percentage.text.toString().toInt();
                }
                if (rg_btn_partial.getCheckedRadioButtonId() == R.id.rb_partial) {
                    if (ed_store_partial.text.isNullOrEmpty() || ed_benef_partial.text.isNullOrEmpty()) {
                        Toast.makeText(this, "Kindly enter percentage", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    } else if (ed_store_partial.text.toString().toInt() > 100 || ed_benef_partial.text.toString().toInt() > 100) {
                        Toast.makeText(this, "Kindly enter percentage below 100", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    } else if (ed_store_partial.text.toString().toInt() < 10 || ed_benef_partial.text.toString().toInt() < 10) {
                        Toast.makeText(this, "Kindly enter percentage above 10", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    InsertBill.getInstance(this).store_partial_ = ed_store_partial.text.toString().toInt()
                    InsertBill.getInstance(this).benef_partial_ = ed_benef_partial.text.toString().toInt()

                    Log.e("store_partial_", "" + InsertBill.getInstance(this).store_partial_)
                    Log.e("benef_partial_", "" + InsertBill.getInstance(this).benef_partial_)
                }
                InsertBill.getInstance(this).fromDate = getCustomDate(from_time)
                InsertBill.getInstance(this).toDate = getCustomDate(to_time)
                InsertBill.getInstance(this).insertBill(this)
            }
        }
        ti_fromDate.setOnClickListener {
            from = true;
            datepicker()
        }
        ti_toDate.setOnClickListener {
            from = false;
            datepicker()
        }
    }

    private fun getCustomDate(time: Long): String? {
        val myFormat = "yyyy-MM-dd HH:mm:ss" //In which you need put here
        val sdf = SimpleDateFormat(myFormat)
        return sdf.format(Date(time))
    }

    private var to_time: Long = 0
    private var from_time: Long = 0
    private var from: Boolean = false
    val myCalendar = Calendar.getInstance()

    private fun datepicker() {
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }
        DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()

    }

    private fun updateLabel() {
        val myFormat = "dd/MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        if (from) {
            from_time = myCalendar.timeInMillis
            ti_fromDate.setText(sdf.format(myCalendar.time))

        } else {
            to_time = myCalendar.timeInMillis
            ti_toDate.setText(sdf.format(myCalendar.time))
        }
    }
}
