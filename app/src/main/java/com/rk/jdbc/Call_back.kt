package com.rk.jdbc

import java.sql.ResultSet

/**
 * Created by user1 on 17/7/19.
 */
interface Call_back {
    fun response(result: ResultSet,type : ServiceType);
}