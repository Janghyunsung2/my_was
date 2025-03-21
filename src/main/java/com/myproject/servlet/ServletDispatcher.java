package com.myproject.servlet;

import com.myproject.http.handler.HttpStatus;
import com.myproject.http.request.HttpRequest;
import com.myproject.http.response.HttpResponse;
import com.myproject.http.response.ResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(ServletDispatcher.class.getName());

    private ServletDispatcher(){}

    public static void dispatch(HttpRequest request, HttpResponse response, ResponseWriter responseWriter) {
        String path = request.getPath().replaceFirst("^/", "");
        path = path.split("\\?")[0];
        String className;
        if(path.contains(".")){
            className = path.replace("/", ".");
        }else {
            className = "com.myproject.servlet." + path;
        }
        try {
            Class<?> servletClass = Class.forName(className);
            SimpleServlet servlet = (SimpleServlet) servletClass.getDeclaredConstructor().newInstance();
            servlet.service(request, response, responseWriter);
        } catch (ClassNotFoundException e) {
            response.setStatus(HttpStatus.NOT_FOUND);
            logger.warn(e.getMessage());
        }catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            logger.warn(e.getMessage());
        }
    }
}
