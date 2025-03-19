package com.myproject.servlet;

import com.myproject.http.request.HttpRequest;
import com.myproject.http.response.HttpResponse;
import java.io.IOException;
import java.io.Writer;

public class HelloServlet implements SimpleServlet{

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        Writer writer = response.getWriter();
        writer.write("Hello ");
        writer.write(request.getParameter("name"));
        writer.flush();
    }
}
