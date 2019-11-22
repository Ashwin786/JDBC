package com.rk.jdbc.postman.view.history

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.rk.jdbc.postman.data.local.PostmanDao
import com.rk.jdbc.postman.data.local.PostmanDatabase
import com.rk.jdbc.postman.data.model.ApiDbDto

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private var postmanDao: PostmanDao?

    init {
        postmanDao = PostmanDatabase.getDatabase(application).postmanDao()
    }



    fun getApiDbData(): LiveData<List<ApiDbDto>> {
        return postmanDao!!.allApi_Details()
    }
}