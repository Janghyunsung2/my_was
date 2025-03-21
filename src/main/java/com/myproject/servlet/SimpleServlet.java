package com.myproject.servlet;

import com.myproject.http.request.HttpRequest;
import com.myproject.http.response.HttpResponse;
import com.myproject.http.response.ResponseWriter;
import java.io.IOException;

public interface SimpleServlet {

    void service(HttpRequest request, HttpResponse response, ResponseWriter responseWriter) throws IOException;
}
