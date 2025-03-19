package com.myproject.httpserver;

import com.sun.net.httpserver.HttpHandler;

public interface Server {

    void start();
    void stop();
    void addRoute(String path, HttpHandler handler);

}
