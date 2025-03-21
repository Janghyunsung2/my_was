package com.myproject.servlet;

import com.myproject.http.handler.HttpStatus;
import com.myproject.http.request.HttpRequest;
import com.myproject.http.response.HttpResponse;
import com.myproject.http.response.ResponseWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeServlet implements SimpleServlet {

    @Override
    public void service(HttpRequest request, HttpResponse response, ResponseWriter responseWriter) throws IOException {
        response.setStatus(HttpStatus.OK);
        response.setBody("<h1> Current time is " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "</h1>");
        responseWriter.writerResponse(response);
    }
}
