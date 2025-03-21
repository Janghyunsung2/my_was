package com.myproject.http.request;

import com.myproject.http.request.impl.HttpRequestImpl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestAnalyzer {

    private final Socket connection;
    private final Logger logger = LoggerFactory.getLogger(RequestAnalyzer.class);
    private Map<String, String> queryParams;
    private Map<String, String> headers;
    private String body;
    public RequestAnalyzer(Socket connection) {
        this.connection = connection;
    }

    public HttpRequest httpParser(){

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String requestLine = reader.readLine();
            if(isRequestLineValid(requestLine)){
                logger.debug("Request line: {}", requestLine);
                return null;
            }
            String[] parts = requestLine.split(" ", 3);
            if(parts.length < 3){
                logger.warn("Invalid request line: {}", requestLine);
            }

            String method = parts[0];
            String fullPath = parts[1];

            String[] pathParts = fullPath.split("\\?", 2);
            // 'Hello'
            String path = pathParts[0];

            if(pathParts.length == 2){
                String queryString = pathParts[1];
                queryParsing(queryString);
            }

            //헤더 파싱
            headerParsing(reader);

            //본문 읽기
            int contentLength = 0;
            try {
                contentLength = Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
            }catch (NumberFormatException e){
                logger.warn("Invalid content length: {}", contentLength);
                headers.put("Content-Length", "0");
            }
            if(contentLength > 0){
                bodyParsing(reader, contentLength);
            }

            return new HttpRequestImpl(headers, queryParams, method, path, body);
        }catch (IOException e){
            logger.warn(e.getMessage());
        }
        return null;
    }

    private boolean isRequestLineValid(String line){
        return line == null || line.isEmpty();
    }

    private void queryParsing(String queryString){
        queryParams = new HashMap<>();
        for (String param : queryString.split("&")) {
            String[] keyValue = param.split("=", 2);
            if(keyValue.length == 2){
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                queryParams.put(key, value);
            }
        }
    }

    private void headerParsing(BufferedReader reader) throws IOException{
        headers = new HashMap<>();
        String headerLine;
        while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
            String[] headerParts = headerLine.split(":", 2);
            if(headerParts.length == 2){
                headers.put(headerParts[0].trim(), headerParts[1].trim());
            }
        }
    }

    private void bodyParsing(BufferedReader reader, int bodyLength) throws IOException{
        char[] bodyChars = new char[bodyLength];
        reader.read(bodyChars, 0, bodyLength);
        this.body = new String(bodyChars);
    }
}
