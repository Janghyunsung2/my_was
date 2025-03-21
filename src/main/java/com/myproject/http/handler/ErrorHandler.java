package com.myproject.http.handler;

import com.myproject.config.ConfigLoader;
import com.myproject.http.response.HttpResponse;
import com.myproject.http.response.ResponseWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


public class ErrorHandler {

    private ErrorHandler() {}

    public static void sendErrorResponse(HttpResponse response, String host, HttpStatus httpStatus, ResponseWriter responseWriter)
        throws IOException {
        ConfigLoader.ServerConfig config = ConfigLoader.loadConfig("config.json");
        ConfigLoader.VirtualHost virtualHost = config.hosts.getOrDefault(host, null);

        String http_root = "";
        String pagePath = "";
        Map<String, String> errors = config.errors;
        if(host == null || virtualHost.http_root == null) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            String forbiddenPage = errors.getOrDefault("403", null);
            pagePath = "var/www/" + forbiddenPage;
            sendErrorPage(pagePath, response, responseWriter);
            return;
        }else {
            http_root = virtualHost.http_root;
        }

        String errorFile = virtualHost.errors.getOrDefault(String.valueOf(httpStatus.getCode()), null);
        response.setStatus(httpStatus);
        if(errorFile == null){
            response.setStatus(HttpStatus.NOT_FOUND);
            pagePath = "var/www/" + errors.getOrDefault("404", null);
        }else {
            pagePath = http_root + "/" + errorFile;
        }

        sendErrorPage(pagePath, response, responseWriter);
    }

    private static void sendErrorPage(String errorPagePath, HttpResponse response, ResponseWriter responseWriter) throws IOException {
        if(errorPagePath.startsWith("/")){
            errorPagePath = errorPagePath.substring(1);
        }
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try(InputStream inputStream = classLoader.getResourceAsStream(errorPagePath);
        ) {
            if(inputStream != null){
                String content = new String(inputStream.readAllBytes());
                response.setBody(content);
                responseWriter.writerResponse(response);
            }else {
                response.setBody("<h1>" + "Not Found.." + "</h1>");
                responseWriter.writerResponse(response);
            }
        }
    }
}
