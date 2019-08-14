package com.rk.jdbc

import org.junit.Test

import org.junit.Assert.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private val customDate = "2019-07-01 10:00:00"
    @Test
    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)
//        change_date()
        check_date()
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
