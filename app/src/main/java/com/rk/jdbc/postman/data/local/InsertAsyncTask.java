package com.rk.jdbc.postman.data.local;

import android.os.AsyncTask;
import android.util.Log;

import com.rk.jdbc.postman.data.model.ApiDbDto;

import java.util.ArrayList;

public class InsertAsyncTask extends AsyncTask<ApiDbDto, Void, Void> {

    private final PostmanDao postmanDao;

    public InsertAsyncTask(PostmanDao postmanDao) {
        this.postmanDao = postmanDao;
    }

    @Override
    protected Void doInBackground(ApiDbDto... dto) {
        Log.e("Insertion : ", "" + postmanDao.insert(dto[0]));
        return null;
    }
}
