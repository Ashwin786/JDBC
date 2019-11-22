package com.rk.jdbc.postman.data.model;

import java.util.ArrayList;

public class Request {
    private String method;

    private ArrayList<Header> header;

    private String description;

    private Body body = new Body();

    private String url;

    public String getMethod ()
    {
        return method;
    }

    public void setMethod (String method)
    {
        this.method = method;
    }

    public ArrayList<Header> getHeader ()
    {
        return header;
    }

    public void setHeader (ArrayList<Header> header)
    {
        this.header = header;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public Body getBody ()
    {
        return body;
    }

    public void setBody (Body body)
    {
        this.body = body;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }
}
