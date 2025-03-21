package com.myproject.http.request.impl;

import com.myproject.http.request.HttpRequest;
import java.util.Map;
import lombok.Builder;


public class HttpRequestImpl implements HttpRequest {

    private String method;
    private String path;
    private final Map<String, String> headers;
    private final Map<String, String> queryParams;
    private String body;

    @Builder
    public HttpRequestImpl(Map<String, String> headers, Map<String, String> queryParams,
        String method,
        String path, String body) {
        this.headers = headers;
        this.queryParams = queryParams;
        this.method = method;
        this.path = path;
        this.body = body;
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public String getBody() {
        return this.body;
    }

    @Override
    public String getParameter(String name) {
        return queryParams.get(name);
    }
}
