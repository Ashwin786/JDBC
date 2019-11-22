package com.rk.jdbc.postman.data.apiService;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.rk.jdbc.Global;
import com.rk.jdbc.postman.data.local.InsertAsyncTask;
import com.rk.jdbc.postman.data.local.PostmanDao;
import com.rk.jdbc.postman.data.model.ApiDbDto;
import com.rk.jdbc.postman.data.model.InterMediateDto;
import com.rk.jdbc.postman.util.Sha;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyApiService {
    private static ResultUpdatable callBack;
    private static VolleyApiService apiService;
    String url;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog ld;
    private static Context context;
    private RequestQueue req_queue;
    private String actions;
    ApiDbDto apiDbDto = new ApiDbDto();
    private String result;

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    private Map<String, String> headerMap = new HashMap<>();


    public static VolleyApiService getInstance(Context mContext, ResultUpdatable mCallBack) {
        context = mContext;
        callBack = mCallBack;

        if (apiService == null)
            apiService = new VolleyApiService();
        return apiService;
    }

    public VolleyApiService() {
        req_queue = Global.getInstance().get_requestQueue();
    }

    public void cancelrequest() {
        req_queue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }

   /* private void printurl() {
        Set<String> key_action = params.keySet();
        for (int j = 0; j < key_action.size(); j++) {
            actions = actions + "&" + key_action.toArray()[j].toString() + "=" + params.get(key_action.toArray()[j]);
        }
        Log.e("weburls", url + "?" + actions);
    }*/

    public void execute(String tempurl, String jsonString, final PostmanDao postmanDao, final boolean encryption) {
        callProgressBar();
        url = tempurl;

        apiDbDto.setCreated_date(System.currentTimeMillis());
        apiDbDto.setUrl(url);
        String[] name = url.split(".com");
        if (name.length > 1)
            apiDbDto.setName(name[1]);
        else
            apiDbDto.setName(url);
        apiDbDto.setRequest(jsonString);

        JSONObject jsonObject = null;
        try {
            if (encryption) {
                InterMediateDto interMediateDto = new InterMediateDto();
                interMediateDto.setMessage(Base64.encodeToString(Sha.shaEncrypt(jsonString), Base64.DEFAULT));
                jsonString = new Gson().toJson(interMediateDto);
            }
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("jsonObject", jsonObject.toString());
//        printurl();
        jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("encryption response ", response.toString());
                try {
                    result = response.toString();
                    if (encryption) {
                        InterMediateDto mediateDto = new Gson().fromJson(response.toString(), InterMediateDto.class);
                        new InsertAsyncTask(postmanDao).execute(apiDbDto);
                        result = Sha.shaDecrypt(mediateDto.getMessage());
                    }
                    callBack.setResult(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ld.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley error", error.toString());
                callBack.setErrorResponse(error.toString());
                ld.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if (!Global.getInstance().getCookie(context).equals("")) {
                    String cookie = Global.getInstance().getCookie(context);
                    Log.e("cookie", "cookie" + cookie);
                    params.put("Cookie", cookie);
                }
                return params;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually

                Map headers = response.headers;
                String cookie = (String) headers.get("Set-Cookie");
                Log.e("cookie", "cookie" + cookie);
                Global.getInstance().saveCookie(context, cookie);
                return super.parseNetworkResponse(response);
            }
        };
        jsonObjectRequest.setShouldCache(false);
        req_queue.add(jsonObjectRequest);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    private void callProgressBar() {
        ld = new ProgressDialog(context);
        ld.setCancelable(false);
        ld.setMessage("Loading... Please wait...");
        ld.show();
    }
}
