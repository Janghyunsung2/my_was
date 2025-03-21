package com.myproject.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class ConfigLoader {

    private ConfigLoader() {}

    public static ServerConfig loadConfig(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(filePath);

        if(inputStream == null){
            throw new IOException("config.json 파일을 찾을 수 없습니다.");
        }
        return objectMapper.readValue(inputStream, ServerConfig.class);
    }

    public static class ServerConfig {
        public int port;
        public Map<String, String> errors;
        public Map<String, VirtualHost> hosts;
        public ServerConfig() {}


    }

    public static class VirtualHost {
        public String http_root;
        public Map<String, String> errors;
        public VirtualHost(){}

    }

}
