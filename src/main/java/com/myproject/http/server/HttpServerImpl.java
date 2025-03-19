package com.myproject.httpserver;

import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

public class HttpServerImpl implements Server{

    private int port;
    private Map<String, HttpHandler> routes = new HashMap<>();
    private boolean running = false;

    public HttpServerImpl(int port) {
        this.port = port;
    }


    @Override
    public void start() {
        running = true;
        try(ServerSocket serverSocket = new ServerSocket(port)){
            while(running){
                Socket socket = serverSocket.accept();
                new Thread(() -> handleRequest(socket)).start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void addRoute(String path, HttpHandler handler) {
        routes.put(path, handler);
    }
}
