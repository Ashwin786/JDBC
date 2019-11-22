package com.rk.jdbc.postman.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.rk.jdbc.postman.data.model.ApiDbDto;

@Database(entities = {ApiDbDto.class}, version = 1)
public abstract class PostmanDatabase extends RoomDatabase {
    private static final String DB_NAME = "postman.db";

    public abstract PostmanDao postmanDao();

    private static PostmanDatabase instances;

    public static PostmanDatabase getDatabase(Context context) {
        if (instances == null) {
//            instances =
            Builder<PostmanDatabase> db_builder = Room.databaseBuilder(context.getApplicationContext(), PostmanDatabase.class, DB_NAME);
//            db_builder.allowMainThreadQueries();
            instances = db_builder.build();
        }
        return instances;
    }
}
