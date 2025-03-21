package com.myproject.http.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

public class RequestAnalyzerTest  {

    private Socket mockSocket;


    @Test
    public void testRequestAnalyzerGetRequest() throws Exception {
        mockSocket = mock(Socket.class);
        RequestAnalyzer requestAnalyzer = new RequestAnalyzer(mockSocket);
        String httpRequest =
            "GET /test/path?param1=value1&param2=value2 HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "Content-Length: 0\r\n" +
                "\r\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(httpRequest.getBytes(StandardCharsets.UTF_8));
        when(mockSocket.getInputStream()).thenReturn(inputStream);

        HttpRequest request = requestAnalyzer.httpParser();

        assertNotNull("HttpRequest should not be null", request);
        assertEquals("GET", request.getMethod());
        assertEquals("/test/path", request.getPath());
    }

    @Test
    public void testHttpParserPostRequestWithBody() throws IOException {
        String body = "Hello World";
        String httpRequest =
            "POST /submit HTTP/1.1\r\n" +
                "Host: example.com\r\n" +
                "Content-Length: " + body.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                "\r\n" +
                body;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(httpRequest.getBytes(StandardCharsets.UTF_8));
        Socket socket = mock(Socket.class);
        when(socket.getInputStream()).thenReturn(inputStream);

        RequestAnalyzer analyzer = new RequestAnalyzer(socket);
        HttpRequest request = analyzer.httpParser();

        assertNotNull("HttpRequest should not be null", request);
        assertEquals("POST", request.getMethod());
        assertEquals("/submit", request.getPath());


        assertEquals("example.com", request.getHeader("Host"));

        assertEquals(body, request.getBody());
    }

    @Test
    public void testHttpStreamNullCheck() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));
        Socket socket = mock(Socket.class);
        when(socket.getInputStream()).thenReturn(inputStream);
        RequestAnalyzer analyzer = new RequestAnalyzer(socket);
        HttpRequest request = analyzer.httpParser();
        assertNull(request);
    }

    @Test
    public void testParserNumberFormatError() throws Exception {
        String httpRequest =
            "GET /test/path?param1=value1&param2=value2 HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "Content-Length: Number\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(httpRequest.getBytes(StandardCharsets.UTF_8));
        Socket socket = mock(Socket.class);
        when(socket.getInputStream()).thenReturn(inputStream);
        RequestAnalyzer analyzer = new RequestAnalyzer(socket);
        HttpRequest request = analyzer.httpParser();
        assertNotNull(request);
        assertEquals(request.getHeader("Content-Length"), "0");
    }
}