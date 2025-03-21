package com.myproject.http.server;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.myproject.config.ConfigLoader;
import com.myproject.config.ConfigLoader.ServerConfig;

import com.myproject.http.handler.RequestProcessor;
import com.myproject.http.request.HttpRequest;
import com.myproject.http.request.impl.HttpRequestImpl;
import com.myproject.http.response.HttpResponse;
import com.myproject.http.response.HttpResponseImpl;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class HttpServerImplTest {


    private ConfigLoader.ServerConfig serverConfig;
    private HttpServerImpl httpServer;
    private ServerSocket serverSocket;
    private Socket socket;
    private Map<String, String> headers = new HashMap<>();


    @Before
    public void setUp() throws Exception {
        serverConfig = ConfigLoader.loadConfig("config.json");
        httpServer = new HttpServerImpl(serverConfig);
        serverSocket = new ServerSocket(serverConfig.port);
        socket = mock(Socket.class);
        headers.put("Host", "a.com");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.put("Accept-Language", "en-US,en;q=0.5");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Connection", "keep-alive");
    }
    @Test
    public void testServerStartAndStop() throws Exception {
        Thread thread = new Thread(httpServer::start);
        thread.start();
        boolean running = httpServer.isRunning();
        Assert.assertTrue(running);

        httpServer.stop();
        boolean stoping = httpServer.isRunning();
        Assert.assertFalse(stoping);
    }

//    @Test
//    public void testServerProcessorVerify() throws Exception {
//        ExecutorService executorService = mock(ExecutorService.class);
//        HttpRequest httpRequest = HttpRequestImpl.builder()
//            .headers(headers)
//            .path("/")
//            .method("GET")
//            .build();
//        HttpResponse httpResponse = new HttpResponseImpl();
//
//        RequestProcessor processor = new RequestProcessor(serverConfig, "a.com", socket,
//            httpRequest, httpResponse);
//
//        executorService.execute(processor);
//
//        verify(executorService, times(1)).execute(Mockito.any(Runnable.class));
//    }



}