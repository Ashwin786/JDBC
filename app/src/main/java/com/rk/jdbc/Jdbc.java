package com.rk.jdbc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;


import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by user1 on 15/7/19.
 */

public class Jdbc {

    private static Jdbc jdbc;
    private static Call_back call_back;
    private static Context mcontext;
    private ProgressDialog pd;
    private String customDate = "2019-08-01 00:00:00";
    //    private String customDate = "2019-07-01";
    private Date custom_Date;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar c = Calendar.getInstance();
    private String previous_date;
    String today_date;
    DecimalFormat formatter = new DecimalFormat("000000");
    String month = "08", year = "19";
    private Connection con;
    private boolean running = false;
    private boolean rural = false;

    public Jdbc() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Jdbc getInstance(Context context) {
        mcontext = context;
        call_back = (Call_back) context;
        if (jdbc == null)
            jdbc = new Jdbc();
        return jdbc;
    }

    protected void login(final String db_url, final String db_port, final String username, final String password, final ServiceType type) {
        final String connection = "jdbc:mysql://" + db_url + ":" + db_port + "?user=" + username + "&password=" + password;
        getConnection(connection, type);


    }

    public void getConnection(final String connection, final ServiceType type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    con = DriverManager.getConnection(connection);
//            con = DriverManager.login("jdbc:mysql://192.168.1.102:3306?user=anilalluri&password=an!l@lluri@67");
                    DatabaseMetaData dmd = con.getMetaData();
                    ResultSet rs = dmd.getCatalogs();
                    mcontext.getSharedPreferences("jdbc", Context.MODE_PRIVATE).edit().putString("connection", connection).commit();
                    call_back.response(rs, type);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    public void openDatabase(@NotNull final String selectedItem, final ServiceType type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    con.setCatalog(selectedItem);
                    Log.e("isClosed", "" + con.isClosed());
//                    show_database();
                    call_back.response(null, type);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    protected void show_tables(final ServiceType type) {
        Statement entitle_st = null;
        try {
            entitle_st = con.createStatement();
            ResultSet entitle_rs = entitle_st.executeQuery("show tables");

            call_back.response(entitle_rs, type);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
