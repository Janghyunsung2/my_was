package com.myproject.http.response;

import com.myproject.http.handler.HttpStatus;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class HttpResponseImpl implements HttpResponse {
    private HttpStatus httpStatus = HttpStatus.OK;
    private final Map<String,String> headers = new HashMap<>();
    private String body = "";

    public HttpResponseImpl() throws IOException {
    }

    @Override
    public HttpStatus getStatus()  {
        return this.httpStatus;
    }

    @Override
    public Map<String, String> getHeaders()  {
        return this.headers;
    }

    @Override
    public String getBody()  {
        return this.body;
    }

    @Override
    public void setStatus(HttpStatus status) {
        this.httpStatus = status;
    }

    @Override
    public void setHeader(String name, String value) {
        this.headers.put(name, value);
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }


}
