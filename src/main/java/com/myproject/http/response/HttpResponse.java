package com.myproject.http.response;

import com.myproject.http.handler.HttpStatus;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public interface HttpResponse {

    HttpStatus getStatus() throws IOException;
    Map<String, String> getHeaders() throws IOException;
    String getBody() throws IOException;

    void setStatus(HttpStatus status);
    void setHeader(String name, String value);
    void setBody(String body);

}
