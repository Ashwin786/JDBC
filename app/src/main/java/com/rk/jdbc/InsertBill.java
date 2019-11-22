package com.rk.jdbc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
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
    private static final String TAG = "InsertBill";
    private static InsertBill jdbc;
    private static Call_back call_back;
    private static Context mcontext;
    private static Calendar cal;
    private ProgressDialog pd;
    protected String fromDate = null;
    private Date custom_Date;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar c = Calendar.getInstance();
    private String previous_date;
    String today_date;
    DecimalFormat formatter = new DecimalFormat("000000");
    String month = "09", transaction_year = "19";
    private String current_year = "2019";
    private Connection con;
    private boolean running = false;
    protected boolean rural = false;
    protected boolean with_portable = false;
    protected boolean with_partial = false;
    private String previous_fpsId = "0";
    public int store_partial_ = 0;
    public int benef_partial_ = 0;
    protected String toDate = null;
    public int portablity_percentage;
    private String bene_query;
    private String benef_limit;
    private String fpsId;
    private String trans_id;
    private String totalbeneficiary_count;
    private int beneficiary_count;
    private String fps_gen_code;
    private Statement bene_st;
    private ResultSet bene_rs;
    private int portability_benef_count = -1;
    private String benef_card_type;
    private String benef_district_id;
    private String benef_id;
    private String benef_ration_number;
    private String benef_village_id;
    private String benef_taluk_id;
    private String bill_item_insert_query;
    private String bill_insert_query;
    private String change;
    private PreparedStatement bill_st, update_stock, bill_item_st;
    private ResultSet rs;
    private String updateStock_query;
    private String nic_transaction_id;
    private String bill_auth_insert_query;
    private PreparedStatement bill_auth_st;
    private String benef_fpsId;
    private int bill_durationInMin = 15;


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
        if (running) {
            Toast.makeText((Context)call_back, "Process is running", Toast.LENGTH_SHORT).show();
            Log.e("Process", "Already running");
            return;
        }
        Log.e("Process", "Started");
        if (pd == null) {
            pd = new ProgressDialog(mcontext);
            pd.setCancelable(false);
        }
        pd.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    running = true;
                    previous_date = fromDate;
                    c.setTime(sdf.parse(previous_date));
                    custom_Date = get_customDate(previous_date);

                    Class.forName("com.mysql.jdbc.Driver");
//                    String dateFormat = "yyyy-MM-dd HH:mm:ss";
                    /*Testing database*/
//                    con = DriverManager.getConnection("jdbc:mysql://115.124.100.241:4001/test_prod_copy_uprural_03_06_19?user=chandraprakash&password=Ch@ndr@PDS4253");

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
                    /*Get get previous fps_id */
                    if (with_portable) {
                        Statement fst = con.createStatement();
                        ResultSet fps_store_prev = fst.executeQuery("select id from fps_store where active = 1 order by id desc limit 1");
                        fps_store_prev.first();
                        previous_fpsId = fps_store_prev.getString(1);
                    }
                    String store_limit = "";
                    if (store_partial_ != 0) {
                        /*Calculating store percentage*/
                        String store_count = getTotalStoreCount(con);

                        store_count = calculate_percentage(Integer.parseInt(store_count), store_partial_);
                        store_limit = " limit " + store_count;
                    }


                    Statement st = con.createStatement();

                    String store_query = "select f.id,f.generated_code,max(b.transaction_id),count(distinct(be.id)) as benef_count,count(distinct(b.id)) as bill_count " +
                            "from fps_store f " +
                            "join beneficiary be on f.id = be.fps_id and be.active = 1 " +
                            "left join bill b on b.beneficiary_id = be.id and b.bill_date between '" + getFirstDateOfmonth() + "' and '" + getLastDateOfmonth() + "' " +
                            "where f.active = 1 group by f.id having benef_count <> bill_count " + store_limit;
                    Log.e(TAG, "store_query: " + store_query);
                    ResultSet fps_store_rs = st.executeQuery(store_query);

                    long transaction_id = 000000;
                    if (!fps_store_rs.first()) {
                        Log.e(TAG, "Break: ");
                        return;
                    }
                    do {
                        if (is_date_exist()) {
                            Log.e(TAG, "date_exist: ");
                            return;
                        }
                        transaction_id = 000000;
//                        previous_date = fromDate;
//                        custom_Date = get_customDate(previous_date);

//                        Log.e("transaction_id", "" + transaction_id);

                        fpsId = fps_store_rs.getString(1);
                        fps_gen_code = fps_store_rs.getString(2);
                        trans_id = fps_store_rs.getString(3);
                        totalbeneficiary_count = fps_store_rs.getString(4);
                        Log.e(TAG, "totalbeneficiary_count: " + totalbeneficiary_count);
                        if (trans_id != null) {
                            Log.e("trans_id", "" + trans_id);
                            trans_id = trans_id.replace("D", "");
                            trans_id = trans_id.substring(4, trans_id.length());
                            transaction_id = Long.parseLong(trans_id);
                        }
                        Log.e("fpsId", "" + fpsId);
                        Log.e("transaction_id", "" + transaction_id);

//                        String totalbeneficiary_count = getTotalbeneficiary_count(con, fpsId);
                        if (totalbeneficiary_count.equals("0")) {
                            continue;
                        }
                        beneficiary_count = Integer.parseInt(totalbeneficiary_count);
                        benef_limit = "";
                        if (benef_partial_ != 0) {
                            totalbeneficiary_count = calculate_percentage(Integer.parseInt(totalbeneficiary_count), benef_partial_);
                            benef_limit = " limit " + totalbeneficiary_count;
                        }

                        bene_st = con.createStatement();
                        bene_query = "select be.id,old_ration_number,card_type_id,village_id,taluk_id,district_id,be.fps_id " +
                                "from beneficiary be " +
                                "left join bill b on be.id = b.beneficiary_id and b.bill_date between '" + getFirstDateOfmonth() + "'" +
                                " and '" + getLastDateOfmonth() + "'" +
                                " where be.active = 1 and be.fps_id = " + fpsId + " and b.beneficiary_id is null " + benef_limit;
                        Log.e(TAG, "bene_query: " + bene_query);
                        bene_rs = bene_st.executeQuery(bene_query);

                        if (with_portable) {
                            portability_benef_count = portability_count(portablity_percentage, beneficiary_count);
                        }


                        if (!bene_rs.first()) {
                            continue;
                        }
                        do {
                            transaction_id++;
//                            String today_date = "'" + simpleDateFormat.format(custom_Date) + "'";
                            Log.e("previous_date", previous_date);
                            previous_date = change_date(previous_date);
                            today_date = "'" + previous_date + "'";
                            custom_Date = get_customDate(previous_date);
                            Log.e("today_date", today_date);
                            benef_card_type = bene_rs.getString(3);
                            productList = map.get(benef_card_type);

                            if (productList != null && productList.size() > 0) {

                                if (beneficiary_count <= portability_benef_count) {
                                    Log.e(TAG, "with_portable: " + beneficiary_count);
                                    try {
                                        insert_portability_bill(bene_rs, previous_fpsId, fps_gen_code, transaction_id, productList);
                                    } catch (SQLException e) {
                                        transaction_id = get_portableShop_transaction_id();
                                        e.printStackTrace();
                                    }

                                } else {
                                    Log.e(TAG, "non with_portable: " + beneficiary_count);
                                    insert_bill(bene_rs, fpsId, fps_gen_code, transaction_id, productList);
                                }

                            }
                            beneficiary_count--;
                        } while (bene_rs.next());
//                        bene_rs.last();
//                        Log.e("benef count", "" + bene_rs.getRow());




                   /* try {
                        call_back.response(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
//                    tv.setText(result);
                        previous_fpsId = fpsId;
                        Log.e("previous_fpsId", "" + previous_fpsId);
                    } while (fps_store_rs.next());
                    fps_store_rs.last();

                    Log.e("fps_store count", "" + fps_store_rs.getRow());
                } catch (Exception e) {
                    e.printStackTrace();
//                    tv.setText(e.toString());
                } finally {
                    running = false;
                    ((Activity) mcontext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                        }
                    });
                }


            }
        }).start();
    }

    private String getTotalStoreCount(Connection con) throws SQLException {
        Statement bene_st = con.createStatement();
        String query = "select count(f.id) from fps_store f where f.active = 1;";
        ResultSet bene_rs = bene_st.executeQuery(query);
        bene_rs.first();
        return bene_rs.getString(1);

    }

    private String calculate_percentage(int totalbeneficiary_count, int percentage) {
        return String.valueOf(Math.round((totalbeneficiary_count * percentage) / 100));
    }


    protected long get_portableShop_transaction_id() throws SQLException {
        Statement pst = con.createStatement();
        ResultSet fps_store_port = pst.executeQuery("select max(b.transaction_id) as transaction_id from fps_store f left join bill b on f.id = b.fps_id and b.bill_date between '" + fromDate + "' and now() where f.id = " + previous_fpsId);
        fps_store_port.first();
        String trans_port_id = fps_store_port.getString(1);
        Log.e("trans_port_id", "" + trans_port_id);
        long transaction_id;
        if (trans_port_id != null) {
            trans_port_id = trans_port_id.replace("D", "");
            trans_port_id = trans_port_id.substring(4, trans_port_id.length());
            transaction_id = Long.parseLong(trans_port_id);
        } else
            transaction_id = 000000;
        transaction_id++;
        Log.e("transaction_id", "" + transaction_id);
        return transaction_id;
    }

    private int portability_count(int percentage, int beneficiary_count) throws SQLException {

        int portability_count = (beneficiary_count * percentage) / 100;
        Log.e("beneficiary_count", "" + beneficiary_count);
        Log.e("portability_count", "" + portability_count);
        return Math.round(portability_count);
    }


    private void insert_bill(ResultSet bene_rs, String fpsId, String fps_gen_code, long transaction_id, ArrayList<ProductDto> productList) throws SQLException {
        benef_id = bene_rs.getString(1);
        benef_ration_number = bene_rs.getString(2);
        benef_card_type = bene_rs.getString(3);
        benef_village_id = bene_rs.getString(4);
        benef_taluk_id = bene_rs.getString(5);
        benef_district_id = bene_rs.getString(6);
        int bill_id = 0;
        change = "'" + month + transaction_year + formatter.format(transaction_id) + "'" + "," + benef_id + "," + fpsId;
        Log.e("change bill", "" + change);
        bill_insert_query = "INSERT INTO bill (amount, bill_date, bill_ref_id, channel, created_date, `mode`, sms_ref_num, transaction_id, beneficiary_id, fps_id, created_by, bunk_id, rrc_id, server_instance, proof_type, proof_value, remarks) VALUES( 0.2, " + today_date + ", " + generateReferenceId() + ", 'G', " + today_date + ", 'G', 0, " + change + ", NULL, NULL, NULL, 'android', NULL, NULL, '')";

//        Log.e("bene_query bill", "" + bill_insert_query);


        bill_st = con.prepareStatement(bill_insert_query);
        bill_st.executeUpdate();
        rs = bill_st.getGeneratedKeys();

        if (rs.next()) {
            bill_id = rs.getInt(1);
        }
        Log.e("bill_id", "" + bill_id);


        change = today_date + "," + fpsId + ",NULL";
//                            Log.e("change bill_item", "" + change);
//                            String bill_id = "";

        bill_item_insert_query = "INSERT INTO bill_item (cost, created_date, quantity, bill_id, product_id, bill_date, fps_id, fps_gen_code, village_id, taluk_id, district_id, kerosene_bunk_id, rrc_id) VALUES";
        for (ProductDto dto : productList) {
            bill_item_insert_query += "(" + dto.getProduct_price() + ", " + today_date + ", " + dto.getQuantity() + ", " + bill_id + ", " + dto.getProduct_id() + ", " + change + ", " + benef_village_id + ", " + benef_taluk_id + ", " + benef_district_id + ", NULL, NULL),";

            /*Updating stock*/
            updateStock_query = "update fps_stock set quantity = (quantity - " + dto.getQuantity() + ") where fps_id = " + fpsId + " and product_id = " + dto.getProduct_id() + "";

            update_stock = con.prepareStatement(updateStock_query);
            update_stock.executeUpdate();
        }
        bill_item_insert_query = bill_item_insert_query.substring(0, bill_item_insert_query.length() - 1);

        bill_item_st = con.prepareStatement(bill_item_insert_query);
        bill_item_st.executeUpdate();


        nic_transaction_id = "'" + fps_gen_code + benef_ration_number + "01" + nic_date_format() + "'";
        benef_ration_number = "'" + benef_ration_number + "01" + "'";
        if (rural)
            bill_auth_insert_query = " INSERT INTO bill_auth_detail(bill_id, uid, mobile_number, nic_transaction_id, sms_pin, auth_member_id, auth_response_code, auth_status, i_consent, local_member_name, serial_no, auth_type) VALUES(" + bill_id + ", NULL, NULL, " + nic_transaction_id + ", NULL," + benef_ration_number + ", '1', '1', '1', 'Ramesh', '0', 'FMR')";
        else {
            /*Urban bene_query*/
            bill_auth_insert_query = " INSERT INTO bill_auth_detail(bill_id, uid, mobile_number, nic_transaction_id, sms_pin, auth_member_id, auth_response_code, auth_status, i_consent, local_member_name, serial_no) VALUES(" + bill_id + ", NULL, NULL, " + nic_transaction_id + ", NULL," + benef_ration_number + ", '1', '1', '1', 'Ramesh', '0')";
        }

        bill_auth_st = con.prepareStatement(bill_auth_insert_query);
        bill_auth_st.executeUpdate();


//        Log.e("bene_query bill_auth", "" + bill_auth_insert_query);

    }

    private void insert_portability_bill(ResultSet bene_rs, String billing_fpsId, String fps_gen_code, long transaction_id, ArrayList<ProductDto> productList) throws SQLException {
        benef_id = bene_rs.getString(1);
        benef_ration_number = bene_rs.getString(2);
        benef_village_id = bene_rs.getString(4);
        benef_taluk_id = bene_rs.getString(5);
        benef_district_id = bene_rs.getString(6);
        benef_fpsId = bene_rs.getString(7);
        int bill_id = 0;
        change = "'" + month + transaction_year + formatter.format(transaction_id) + "'" + "," + benef_id + "," + billing_fpsId;
        Log.e("change bill", "" + change);
        String bill_insert_query = "INSERT INTO bill (amount, bill_date, bill_ref_id, channel, created_date, `mode`, sms_ref_num, transaction_id, beneficiary_id, fps_id, created_by, bunk_id, rrc_id, server_instance, proof_type, proof_value, remarks, parent_fps_id, parent_district_id) VALUES( 0.2, " + today_date + ", " + generateReferenceId() + ", 'G', " + today_date + ", 'P', 0, " + change + ", NULL, NULL, NULL, 'android', NULL, NULL, '','" + benef_fpsId + "','" + benef_district_id + "')";

//        Log.e("bene_query bill", "" + bill_insert_query);


        bill_st = con.prepareStatement(bill_insert_query);
        bill_st.executeUpdate();
        rs = bill_st.getGeneratedKeys();

        if (rs.next()) {
            bill_id = rs.getInt(1);
        }
        Log.e("bill_id", "" + bill_id);


        change = today_date + "," + billing_fpsId + ", NULL";
//                            Log.e("change bill_item", "" + change);
//                            String bill_id = "";

        bill_item_insert_query = "INSERT INTO bill_item (cost, created_date, quantity, bill_id, product_id, bill_date, fps_id, fps_gen_code, village_id, taluk_id, district_id, kerosene_bunk_id, rrc_id) VALUES";
        for (ProductDto dto : productList) {
            bill_item_insert_query += "(" + dto.getProduct_price() + ", " + today_date + ", " + dto.getQuantity() + ", " + bill_id + ", " + dto.getProduct_id() + ", " + change + ", " + benef_village_id + ", " + benef_taluk_id + ", " + benef_district_id + ", NULL, NULL),";

            /*Updating stock*/
            updateStock_query = "update fps_stock set quantity = (quantity - " + dto.getQuantity() + ") where fps_id = " + billing_fpsId + " and product_id = " + dto.getProduct_id() + "";

            update_stock = con.prepareStatement(updateStock_query);
            update_stock.executeUpdate();
        }
        bill_item_insert_query = bill_item_insert_query.substring(0, bill_item_insert_query.length() - 1);

//        Log.e("bene_query bill_item", "" + bill_item_insert_query);
        bill_item_st = con.prepareStatement(bill_item_insert_query);
        bill_item_st.executeUpdate();


        nic_transaction_id = "'" + fps_gen_code + benef_ration_number + "01" + nic_date_format() + "'";
        benef_ration_number = "'" + benef_ration_number + "01" + "'";
        if (rural)
            bill_auth_insert_query = " INSERT INTO bill_auth_detail(bill_id, uid, mobile_number, nic_transaction_id, sms_pin, auth_member_id, auth_response_code, auth_status, i_consent, local_member_name, serial_no, auth_type) VALUES(" + bill_id + ", NULL, NULL, " + nic_transaction_id + ", NULL," + benef_ration_number + ", '1', '1', '1', 'Ramesh', '0', 'FMR')";
        else {
            /*Urban bene_query*/
            bill_auth_insert_query = " INSERT INTO bill_auth_detail(bill_id, uid, mobile_number, nic_transaction_id, sms_pin, auth_member_id, auth_response_code, auth_status, i_consent, local_member_name, serial_no) VALUES(" + bill_id + ", NULL, NULL, " + nic_transaction_id + ", NULL," + benef_ration_number + ", '1', '1', '1', 'Ramesh', '0')";
        }

        bill_auth_st = con.prepareStatement(bill_auth_insert_query);
        bill_auth_st.executeUpdate();

//        Log.e("bene_query bill_auth", "" + bill_auth_insert_query);

    }

    private String change_date(String previous_date) {
        try {
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            c.setTime(sdf.parse(previous_date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.MINUTE, bill_durationInMin);
        return sdf.format(c.getTime());
    }

    private Date get_customDate(String previous_date) {
        try {
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            month = getcurrentMonth(c.getTimeInMillis());
            transaction_year = getcurrentYear(c.getTimeInMillis(), "yy");
            current_year = getcurrentYear(c.getTimeInMillis(), "yyyy");
            return sdf.parse(previous_date);
//            return new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);

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


    private boolean is_date_exist() {
        try {
            sdf.applyPattern("yyyy-MM-dd");
            Date date2 = sdf.parse(toDate);
            if (custom_Date.compareTo(date2) > 0) {
//                previous_date = fromDate;
                return true;
            } else
                return false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getcurrentMonth(long time) {
        sdf.applyPattern("MM");
        return sdf.format(new Date(time));
    }

    private String getcurrentYear(long time, String pattern) {
        sdf.applyPattern(pattern);
        return sdf.format(new Date(time));
    }

    public String getFirstDateOfmonth() {
        cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        cal.set(Calendar.YEAR, Integer.parseInt(current_year));

        sdf.applyPattern("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }


    public String getLastDateOfmonth() {
        cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        cal.set(Calendar.YEAR, Integer.parseInt(current_year));
        sdf.applyPattern("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

}
