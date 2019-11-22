package com.rk.jdbc.postman.main

import android.app.Application
import android.arch.lifecycle.*
import android.content.Context
import com.rk.jdbc.postman.data.apiService.ResultUpdatable
import com.rk.jdbc.postman.data.apiService.VolleyApiService
import com.rk.jdbc.postman.data.local.PostmanDao
import com.rk.jdbc.postman.data.local.PostmanDatabase

class PageViewModel(application: Application) : AndroidViewModel(application) {


    private var postmanDao: PostmanDao?
    private val _index = MutableLiveData<Int>()
    var tabCount = 1
    val text: LiveData<String> = Transformations.map(_index) {
        "Hello world from section: $it"
    }

    fun setIndex(index: Int) {
        _index.value = index
        tabCount = index
    }

    fun sendRequest(url: String, request: String, callBack: ResultUpdatable, mContext: Context?, encryption: Boolean) {
        VolleyApiService.getInstance(mContext, callBack).execute(url, request, postmanDao,encryption)
    }

    init {
        postmanDao = PostmanDatabase.getDatabase(application).postmanDao()
    }
}