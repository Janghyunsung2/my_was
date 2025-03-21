package com.myproject;

import com.myproject.config.ConfigLoader;
import com.myproject.http.server.HttpServerImpl;
import com.myproject.http.server.Server;
import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws IOException {

        ConfigLoader.ServerConfig serverConfig = ConfigLoader.loadConfig("config.json");

        Server server = new HttpServerImpl(serverConfig);
        server.start();
    }
}