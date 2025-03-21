package com.myproject.http.request;

public interface HttpRequest {

    String getMethod();
    String getPath();
    String getHeader(String name);
    String getBody();
    String getParameter(String name);
}
