package com.rk.jdbc.postman.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "api_details",indices = {@Index(value = {"url", "request"},
        unique = true)})
public class ApiDbDto  implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int _id;

    public ApiDbDto() {
    }

    public static final Creator<ApiDbDto> CREATOR = new Creator<ApiDbDto>() {
        @Override
        public ApiDbDto createFromParcel(Parcel in) {
            return new ApiDbDto(in);
        }

        @Override
        public ApiDbDto[] newArray(int size) {
            return new ApiDbDto[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;
    private long created_date;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public long getCreated_date() {
        return created_date;
    }

    public void setCreated_date(long created_date) {
        this.created_date = created_date;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    private String request;

    private String response;

    private String name;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeString(this.request);
        dest.writeLong(this.created_date);
    }

    protected ApiDbDto(Parcel in) {
        name = in.readString();
        url = in.readString();
        request = in.readString();
        created_date = in.readLong();
    }
}
