package com.myproject.http.server;

import java.io.IOException;
import java.net.Socket;

public interface Server {

    void start() throws IOException;
    void stop();
    boolean isRunning();
}
