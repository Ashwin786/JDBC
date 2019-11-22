package com.rk.jdbc.postman.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.rk.jdbc.postman.data.model.ApiDbDto;

import java.util.List;

@Dao
public interface PostmanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ApiDbDto apiDbDto);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    int update(ApiDbDto apiDbDto);

    @Query("Select * from api_details")
    LiveData<List<ApiDbDto>> allApi_Details();
}
