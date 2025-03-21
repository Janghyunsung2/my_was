package com.myproject.http.server;

import static java.util.concurrent.Executors.newFixedThreadPool;

import com.myproject.config.ConfigLoader.ServerConfig;
import com.myproject.http.handler.RequestProcessor;
import com.myproject.http.request.HttpRequest;
import com.myproject.http.request.RequestAnalyzer;
import com.myproject.http.response.HttpResponse;
import com.myproject.http.response.HttpResponseImpl;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServerImpl implements Server{

    private final ServerConfig serverConfig;
    private static final int POOL_SIZE = 50;
    private volatile boolean running = true;
    private final Logger logger = LoggerFactory.getLogger(HttpServerImpl.class);
    private final ExecutorService threadPool;
    private final ServerSocket serverSocket;

    public HttpServerImpl(ServerConfig serverConfig) throws IOException {
        this.serverConfig = serverConfig;
        this.threadPool = newFixedThreadPool(POOL_SIZE);
        serverSocket = new ServerSocket(serverConfig.port, 50, InetAddress.getByName("0.0.0.0"));
    }


    @Override
    public void start() {
            try{
                while (running && !serverSocket.isClosed()) {
                    logger.info("Starting server...");
                    Socket socket = serverSocket.accept();
                    RequestProcessor processor = getRequestProcessor(socket);
                    threadPool.execute(processor);
                }
            } catch (IOException e) {
                logger.error("Failed to start server", e);
            } finally {
                running = false;
                threadPool.shutdown();
            }
    }

    private RequestProcessor getRequestProcessor(Socket socket) throws IOException {
        RequestAnalyzer requestAnalyzer = new RequestAnalyzer(socket);
        HttpRequest httpRequest = requestAnalyzer.httpParser();
        HttpResponse response = new HttpResponseImpl();

        String host = httpRequest.getHeader("Host");

        if(host == null || host.isEmpty() || host.contains("localhost")) {
            host = "*";
        }

        return new RequestProcessor(
            serverConfig, host , socket, httpRequest, response);
    }

    @Override
    public void stop(){
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
