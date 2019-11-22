package com.rk.jdbc.postman.data.model;

public class SessionId {
    private static final SessionId ourInstance = new SessionId();

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    private String sessionid;
    public static SessionId getInstance() {
        return ourInstance;
    }
}
