package com.rk.jdbc

import android.util.Base64
import com.rk.jdbc.postman.util.Sha
import org.junit.Test
import java.text.DecimalFormat

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private var custom_Date: Date? = null
    private var cal: Calendar? = null
    private val customDate = "2019-07-01 10:00:00"
    internal var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    @Test
    fun addition_isCorrect() {

        val jsonString = "rcid=\"299900000002\"|dcode=\"999\"|userid=\"OASIS\"|password=\"Oasis23Nic1\"|dt=\"11/20/2019 10:56:48\"|checksum=\"215e9736aa4be0579fe3871675c9b1a3\"";
        var response = Base64.encodeToString(Sha.shaEncrypt(jsonString), Base64.DEFAULT)
        System.out.println(response)

//        assertEquals(4, 2 + 2)
//        change_date()
//        check_date()
//        check_percentage();
//        while_check();
//        System.out.println(calculate_percentage(50,25))
                /* sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        custom_Date = get_customDate("2019-07-02 00:00:00")

        System.out.println(getFirstDateOfmonth())
        System.out.println(getLastDateOfmonth())*/
//                var a = 17.9
//        var b = 5.9
//        var c = a-b
//        val f = DecimalFormat("##.000")
//        System.out.println(f.format(c))
//        System.out.println(c)

    }
    private fun get_customDate(previous_date: String): Date? {
        try {
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss")
            return sdf.parse(previous_date)
            //            return new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

    fun getFirstDateOfmonth(): String {
        cal = Calendar.getInstance()
        cal?.set(Calendar.DAY_OF_MONTH, 1)
        cal?.set(Calendar.MONTH, getmonth())
        cal?.set(Calendar.YEAR, getYear())

        sdf.applyPattern("yyyy-MM-dd")
        return sdf.format(cal?.getTime())
    }

    private fun getYear(): Int {
        cal = Calendar.getInstance()
        cal?.setTime(custom_Date)
        sdf.applyPattern("yyyy")
        return Integer.parseInt(sdf.format(cal?.getTime()))
    }

    private fun getmonth(): Int {
        cal = Calendar.getInstance()
        cal?.setTime(custom_Date)
        sdf.applyPattern("MM")
        return Integer.parseInt(sdf.format(cal?.getTime()))
    }

    fun getLastDateOfmonth(): String {
        cal = Calendar.getInstance()
        cal?.set(Calendar.DAY_OF_MONTH, cal!!.getActualMaximum(Calendar.DAY_OF_MONTH))
        cal?.set(Calendar.MONTH, getmonth())
        cal?.set(Calendar.YEAR, getYear())
        sdf.applyPattern("yyyy-MM-dd")
        return sdf.format(cal?.getTime())
    }

    private fun getcurrentMonth(time: Long): String {
        sdf.applyPattern("MM")
        return sdf.format(Date(time))
    }

    private fun calculate_percentage(totalbeneficiary_count: Int, percentage: Int): String {
        return (Math.round((totalbeneficiary_count * percentage).toFloat() / 100)).toString()
    }
    private fun sub_string_check() {
        var trans_port_id = "0819000001"
        trans_port_id = trans_port_id.replace("D", "")
        trans_port_id = trans_port_id.substring(4, trans_port_id.length)
        System.out.println(trans_port_id)
    }

    private fun while_check() {
        var i = 0
        do {
            i++;
            if (i == 4) {
                continue
            }
            System.out.println(i)

        } while (i < 5)
    }

    private fun check_percentage() {
        val beneficiary_count = 200
        val percentage = 5
        val portability_count = beneficiary_count * percentage / 100
        System.out.println("beneficiary_count" + beneficiary_count)
        System.out.println("portability_count" + portability_count)
        System.out.println("Math" + Math.round(portability_count.toDouble()))

    }

    private fun check_date() {
//
        val sdfo = SimpleDateFormat("yyyy-MM-dd")

        // Get the two dates to be compared
        val d1 = sdfo.parse("2018-03-31")
        val d2 = sdfo.parse("2012-03-31")

        // Print the dates
        println("Date1 : " + sdfo.format(d1))
        println("Date2 : " + sdfo.format(d2))

        // Compare the dates using compareTo()
        if (d1.compareTo(d2) > 0) {

            // When Date d1 > Date d2
            println("Date1 is after Date2")
        } else if (d1.compareTo(d2) < 0) {

            // When Date d1 < Date d2
            println("Date1 is before Date2")
        } else if (d1.compareTo(d2) === 0) {

            // When Date d1 = Date d2
            println("Date1 is equal to Date2")
        }

    }


    private fun change_date() {
        var incDate: String? = null
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val c = Calendar.getInstance()
        try {
            c.time = sdf.parse(customDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        c.add(Calendar.MINUTE, 10)
        incDate = sdf.format(c.time)
        System.out.println(incDate)

    }
}
