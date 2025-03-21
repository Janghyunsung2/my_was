package com.myproject.servlet;

import com.myproject.http.handler.HttpStatus;
import com.myproject.http.request.HttpRequest;
import com.myproject.http.response.HttpResponse;
import com.myproject.http.response.ResponseWriter;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class Hello implements SimpleServlet{

    @Override
    public void service(HttpRequest request, HttpResponse response, ResponseWriter responseWriter) throws IOException {
        response.setStatus(HttpStatus.OK);
        response.setBody("<h1>Hello, " + request.getParameter("name") + "!</h1>");
        responseWriter.writerResponse(response);
        ReentrantLock reentrantLock = new ReentrantLock(true);
        reentrantLock.lock();

        reentrantLock.unlock();
    }
}
