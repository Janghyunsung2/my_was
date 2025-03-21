package com.myproject.http.handler;

import com.myproject.http.request.HttpRequest;
import com.myproject.http.response.HttpResponse;

public interface HttpHandler {
    void handler(HttpRequest request, HttpResponse response);

}
