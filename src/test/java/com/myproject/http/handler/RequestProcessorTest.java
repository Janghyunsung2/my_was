package com.myproject.http.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.myproject.config.ConfigLoader;
import com.myproject.config.ConfigLoader.ServerConfig;
import com.myproject.http.request.HttpRequest;
import com.myproject.http.request.impl.HttpRequestImpl;
import com.myproject.http.response.HttpResponse;
import com.myproject.http.response.HttpResponseImpl;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class RequestProcessorTest {

    private RequestProcessor requestProcessor;
    private HttpResponse response;
    ServerConfig configLoader;
    Map<String, String> headers = new HashMap<>();

    @Before
    public void setUp() throws Exception {
        configLoader = ConfigLoader.loadConfig("config.json");
        headers.put("Host", "a.com");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.put("Accept-Language", "en-US,en;q=0.5");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Connection", "keep-alive");

        response = new HttpResponseImpl();
    }

//    @Test
//    public void testAHostRequestProcessorSuccess() throws IOException {
//        Map<String, String> headers = new HashMap<>();
//
//        HttpRequest request = HttpRequestImpl.builder()
//            .headers(headers)
//            .path("/")
//            .method("GET")
//            .build();
//
//        Socket mockSocket = mock(Socket.class);
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        when(mockSocket.getOutputStream()).thenReturn(outputStream);
//
//        String host = "a.com";
//        requestProcessor = new RequestProcessor(configLoader, host, mockSocket, request, response);
//        requestProcessor.run();
//
//        Mockito.verify(mockSocket, Mockito.times(1)).close();
//    }

//    @Test
//    public void testRequestProcessorForbidden() throws IOException {
//        HttpRequest request = HttpRequestImpl.builder()
//            .headers(headers)
//            .path("/../file.exe")
//            .method("GET")
//            .build();
//
//        Socket mockSocket = mock(Socket.class);
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        when(mockSocket.getOutputStream()).thenReturn(outputStream);
//
//        String host = "a.com";
//        requestProcessor = new RequestProcessor(configLoader, host, mockSocket, request, response);
//        requestProcessor.run();
//
//        Assert.assertEquals(response.getStatus(), HttpStatus.FORBIDDEN);
//    }
}