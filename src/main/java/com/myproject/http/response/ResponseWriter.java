package com.myproject.http.response;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

public class ResponseWriter {

    private final OutputStream outputStream;

    public ResponseWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writerResponse(HttpResponse response) throws IOException {
        Writer writer = new OutputStreamWriter(outputStream);

        writer.write("HTTP/1.1 " + response.getStatus().getCode() + " " + response.getStatus());

        writer.write("\r\n");

        for (Map.Entry<String, String> entry : response.getHeaders().entrySet()) {
            writer.write(entry.getKey() + ": " + entry.getValue() + "\r\n");
        }
        writer.write("\r\n");

        if(response.getBody() != null) {
            writer.write(response.getBody());
        }

        writer.flush();
        writer.close();
    }
}
