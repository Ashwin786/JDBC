package com.rk.jdbc.postman.data.model;

import java.util.ArrayList;

public class JsonDto {
    private Request request = new Request();

    private String response;

    private String name;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

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
}
