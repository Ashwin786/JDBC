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

public class InsertBill {

    private static InsertBill jdbc;
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
    protected boolean rural = false;

    public InsertBill() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static InsertBill getInstance(Context context) {
        mcontext = context;
        call_back = (Call_back) context;
        if (jdbc == null)
            jdbc = new InsertBill();
        return jdbc;
    }

    public void insertBill(final Call_back call_back) {
        Log.e("Started", "try");
        if (running)
            return;
        Log.e("Started", "Started");
        pd = new ProgressDialog(mcontext);
        pd.setCancelable(false);
        pd.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    running = true;
                    previous_date = customDate;
                    custom_Date = get_customDate(previous_date);

                    Class.forName("com.mysql.jdbc.Driver");
                    String dateFormat = "yyyy-MM-dd HH:mm:ss";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
                    /*Testing database*/
//                    con = DriverManager.getConnection("jdbc:mysql://115.124.100.241:4001/test_prod_copy_uprural_03_06_19?user=chandraprakash&password=Ch@ndr@PDS4253");
                    rural = false;
                    if (rural)
                        con = DriverManager.getConnection("jdbc:mysql://192.168.1.102:3306/dev_uppds_rural_03_11_18?user=anilalluri&password=an!l@lluri@67");
                    else
                        con = DriverManager.getConnection("jdbc:mysql://192.168.1.103:3306/UPEPDS_003?user=anilalluri&password=an!l@lluri@67");

//                    con = DriverManager.login("jdbc:mysql://192.168.1.102:3306/dev_uppds_rural_03_11_18?user=anilalluri&password=an!l@lluri@67");
//                    con = DriverManager.getConnection("jdbc:mysql://192.168.1.102:3306?user=anilalluri&password=an!l@lluri@67");
//                    con = DriverManager.login("jdbc:mysql://192.168.1.103:3306/UPEPDS_003?user=anilalluri&password=an!l@lluri@67");
//                    con = DriverManager.login("jdbc:mysql://192.168.1.103:3306?user=anilalluri&password=an!l@lluri@67");
                  /*  DatabaseMetaData dmd = con.getMetaData();
                    ResultSet rs = dmd.getCatalogs();
                    Log.e("Database", "" + rs.toString());

                    while (rs.next()) {
//                        System.out.println("TABLE_CAT = " + rs.getString("TABLE_CAT") );
                        Log.e("Database", "" + rs.getString("TABLE_CAT"));
                    }

                    if (1 == 1)
                        return;*/

                    Statement entitle_st = con.createStatement();
                    ResultSet entitle_rs = entitle_st.executeQuery("select card_type_id,p.id,p.group_id,e.quantity,p.product_price from entitlement_master_rule e join product_group pg on pg.id = e.group_id join product p on p.group_id = pg.id where e.is_deleted = 0 and pg.is_deleted = 0 and p.is_deleted = 0");
//                    ResultSetMetaData entitle_rs_data = entitle_rs.getMetaData();

                    String product_id = null, group_id = null, quantity = null, product_price = null;

                    HashMap<String, ArrayList<ProductDto>> map = new HashMap<>();

                    String card_type;
                    ArrayList<ProductDto> productList;
                    while (entitle_rs.next()) {
                        card_type = entitle_rs.getString(1);
                        product_id = entitle_rs.getString(2);
                        group_id = entitle_rs.getString(3);
                        quantity = entitle_rs.getString(4);
                        product_price = entitle_rs.getString(5);
                        productList = map.get(card_type);
                        if (productList == null) {
                            productList = new ArrayList<>();
                            ProductDto dto = new ProductDto(product_id, group_id, quantity, product_price);
                            productList.add(dto);
                            map.put(card_type, productList);
                        } else {
                            ProductDto dto = new ProductDto(product_id, group_id, quantity, product_price);
                            productList.add(dto);
                            map.put(card_type, productList);
                        }
                    }
//                    Log.e("hashmap", map.toString());

                    Statement st = con.createStatement();

//                    ResultSet fps_store_rs = st.executeQuery("select * from fps_store f join beneficiary b on b.fps_id = f.id where f.active = 1 and f.id = 2268 group by f.id");
//                    ResultSet fps_store_rs = st.executeQuery("select * from fps_store f where f.active = 1 limit 100");
                    ResultSet fps_store_rs = st.executeQuery("select f.id,f.generated_code,max(b.transaction_id) as transaction_id from fps_store f left join bill b on f.id = b.fps_id and b.bill_date between '" + customDate + "' and now() where f.active = 1 group by f.id limit 50");
//                    ResultSetMetaData fps_store_data = fps_store_rs.getMetaData();


                    long transaction_id = 000000;


                    while (fps_store_rs.next()) {

                        if (is_date_exist()) {
                            Log.e("is_date_exist", "yes");
                            break;
                        }
                        transaction_id = 000000;
//                        previous_date = customDate;
//                        custom_Date = get_customDate(previous_date);

//                        Log.e("transaction_id", "" + transaction_id);

                        String fpsId = fps_store_rs.getString(1);
                        String fps_gen_code = fps_store_rs.getString(2);
                        String trans_id = fps_store_rs.getString(3);
                        if (trans_id != null)
                            transaction_id = Long.parseLong(trans_id.replace("D", ""));
                        Log.e("fpsId", "" + fpsId);
                        Log.e("transaction_id", "" + transaction_id);


                        Statement bene_st = con.createStatement();
                        String query = "select be.id,old_ration_number,card_type_id,village_id,taluk_id,district_id " +
                                "from beneficiary be " +
                                "left join bill b on be.id = b.beneficiary_id and b.bill_date between '" + customDate + "' and now() " +
                                "where be.active = 1 and be.fps_id = " + fpsId + " and b.beneficiary_id is null";
                        ResultSet bene_rs = bene_st.executeQuery(query);
//                        ResultSet bene_rs = bene_st.executeQuery("select b.id,old_ration_number,card_type_id,village_id,taluk_id,district_id from beneficiary b where b.active = 1 and b.id in(1236152,1236468)");
                        while (bene_rs.next()) {
                            transaction_id++;
//                            String today_date = "'" + simpleDateFormat.format(custom_Date) + "'";
                            previous_date = change_date(previous_date);
                            today_date = "'" + previous_date + "'";
                            custom_Date = get_customDate(previous_date);
//                            Log.e("today_date", today_date);
//                            Log.e("nic_date_format", nic_date_format());
//                            Log.e("transaction_id", "" + transaction_id);
                            String benef_id = bene_rs.getString(1);
                            String benef_ration_number = bene_rs.getString(2);
                            String benef_card_type = bene_rs.getString(3);
                            Log.e("check", fpsId + " : " + benef_id + " : " + benef_ration_number);
                            productList = map.get(benef_card_type);

                            try {
                                if (productList != null && productList.size() > 0)
                                    insert_bill(bene_rs, fpsId, fps_gen_code, transaction_id, productList);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        }
                        bene_rs.last();
                        Log.e("benef count", "" + bene_rs.getRow());


                        running = false;

                   /* try {
                        call_back.response(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
//                    tv.setText(result);
                    }
                    fps_store_rs.last();
                    Log.e("fps_store count", "" + fps_store_rs.getRow());
                } catch (Exception e) {
                    e.printStackTrace();
//                    tv.setText(e.toString());
                }finally {
                    ((Activity)mcontext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                        }
                    });
                }


            }
        }).start();
    }


    SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM-dd");

    private boolean is_date_exist() {
        try {
            Date date2 = sdfo.parse("2019-08-25");
            if (custom_Date.compareTo(date2) > 0) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void insert_bill(ResultSet bene_rs, String fpsId, String fps_gen_code, long transaction_id, ArrayList<ProductDto> productList) throws SQLException {
        String benef_id = bene_rs.getString(1);
        String benef_ration_number = bene_rs.getString(2);
        String benef_card_type = bene_rs.getString(3);
        String benef_village_id = bene_rs.getString(4);
        String benef_taluk_id = bene_rs.getString(5);
        String benef_district_id = bene_rs.getString(6);
//        Log.e("check", "" + benef_id + " : " + benef_ration_number);

//        if (1 == 1)
//            return;
        int bill_id = 0;
        String change = "'" + month + year + formatter.format(transaction_id) + "'" + "," + benef_id + "," + fpsId;
        Log.e("change bill", "" + change);
        String bill_insert_query = "INSERT INTO bill (amount, bill_date, bill_ref_id, channel, created_date, `mode`, sms_ref_num, transaction_id, beneficiary_id, fps_id, created_by, bunk_id, rrc_id, server_instance, proof_type, proof_value, remarks) VALUES( 0.2, " + today_date + ", " + generateReferenceId() + ", 'G', " + today_date + ", 'G', 0, " + change + ", NULL, NULL, NULL, 'android', NULL, NULL, '')";

//        Log.e("query bill", "" + bill_insert_query);


        PreparedStatement bill_st = con.prepareStatement(bill_insert_query);
        bill_st.executeUpdate();
        ResultSet rs = bill_st.getGeneratedKeys();

        if (rs.next()) {
            bill_id = rs.getInt(1);
        }
        Log.e("bill_id", "" + bill_id);


        change = today_date + "," + fpsId + "," + fps_gen_code;
//                            Log.e("change bill_item", "" + change);
//                            String bill_id = "";

        String bill_item_insert_query = "INSERT INTO bill_item (cost, created_date, quantity, bill_id, product_id, bill_date, fps_id, fps_gen_code, village_id, taluk_id, district_id, kerosene_bunk_id, rrc_id) VALUES";
        for (ProductDto dto : productList) {
            bill_item_insert_query += "(" + dto.getProduct_price() + ", " + today_date + ", " + dto.getQuantity() + ", " + bill_id + ", " + dto.getProduct_id() + ", " + change + ", " + benef_village_id + ", " + benef_taluk_id + ", " + benef_district_id + ", NULL, NULL),";
        }
        bill_item_insert_query = bill_item_insert_query.substring(0, bill_item_insert_query.length() - 1);

//        Log.e("query bill_item", "" + bill_item_insert_query);
        PreparedStatement bill_item_st = con.prepareStatement(bill_item_insert_query);
        bill_item_st.executeUpdate();


        String nic_transaction_id = "'" + fps_gen_code + benef_ration_number + "01" + nic_date_format() + "'";
        benef_ration_number = "'" + benef_ration_number + "01" + "'";
        String bill_auth_insert_query;
        if (rural)
            bill_auth_insert_query = " INSERT INTO bill_auth_detail(bill_id, uid, mobile_number, nic_transaction_id, sms_pin, auth_member_id, auth_response_code, auth_status, i_consent, local_member_name, serial_no, auth_type) VALUES(" + bill_id + ", NULL, NULL, " + nic_transaction_id + ", NULL," + benef_ration_number + ", '1', '1', '1', 'Ramesh', '0', 'FMR')";
        else {
            /*Urban query*/
            bill_auth_insert_query = " INSERT INTO bill_auth_detail(bill_id, uid, mobile_number, nic_transaction_id, sms_pin, auth_member_id, auth_response_code, auth_status, i_consent, local_member_name, serial_no) VALUES(" + bill_id + ", NULL, NULL, " + nic_transaction_id + ", NULL," + benef_ration_number + ", '1', '1', '1', 'Ramesh', '0')";
        }

        PreparedStatement bill_auth_st = con.prepareStatement(bill_auth_insert_query);
        bill_auth_st.executeUpdate();

//        Log.e("query bill_auth", "" + bill_auth_insert_query);

    }

    private String change_date(String previous_date) {
        try {
            c.setTime(sdf.parse(previous_date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.MINUTE, 1);
        return sdf.format(c.getTime());
    }

    private Date get_customDate(String previous_date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(previous_date);
//            return new SimpleDateFormat("yyyy-MM-dd").parse(customDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    SimpleDateFormat nicDateFormat = new SimpleDateFormat("ddMMyyyyHHmmss.mmm");

    private String nic_date_format() {
        return nicDateFormat.format(custom_Date);

    }

    SimpleDateFormat ref_dateFormat = new SimpleDateFormat("yyyyMMddHHmmss0SSS");

    public String generateReferenceId() {
        return ref_dateFormat.format(custom_Date) + getRandomNumber();
    }

    int start = 1000000;
    int end = 9999999;
    Random random = new Random();

    public String getRandomNumber() {
        long range = (long) end - (long) start + 1;
        long fraction = (long) (range * random.nextDouble());
        Integer randomNumber = (int) (fraction + start);
        return randomNumber.toString();
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
