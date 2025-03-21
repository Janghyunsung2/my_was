package com.myproject.http.handler;


import com.myproject.config.ConfigLoader.ServerConfig;
import com.myproject.config.ConfigLoader.VirtualHost;
import com.myproject.http.request.HttpRequest;
import com.myproject.http.response.HttpResponse;
import com.myproject.http.response.ResponseWriter;
import com.myproject.servlet.ServletDispatcher;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestProcessor implements Runnable {

    private final ServerConfig serverConfig;
    private final Socket connection;
    private final String host;
    private static final Logger logger = LoggerFactory.getLogger(RequestProcessor.class.getName());
    private final HttpRequest httpRequest;
    private final HttpResponse httpResponse;

    public RequestProcessor(ServerConfig serverConfig, String host, Socket connection, HttpRequest httpRequest, HttpResponse httpResponse) {
        this.serverConfig = serverConfig;
        this.host = host;
        this.connection = connection;
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;
    }

    @Override
    public void run() {
        try {
            ResponseWriter responseWriter = new ResponseWriter(connection.getOutputStream());

            ServletDispatcher.dispatch(httpRequest, httpResponse, responseWriter);

            if(isForbidden(httpRequest.getPath())){
                httpResponse.setStatus(HttpStatus.FORBIDDEN);
                ErrorHandler.sendErrorResponse(httpResponse, host, HttpStatus.FORBIDDEN, responseWriter);
                return;
            }
                // VirtualHost 설정을 가져오기
            VirtualHost virtualHost = serverConfig.hosts.getOrDefault(host, null);
            String content = getString(virtualHost, httpRequest, httpResponse, responseWriter);
            determineHttpStatus(httpRequest, httpResponse);
            httpResponse.setBody(content);
            responseWriter.writerResponse(httpResponse);



        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if(connection != null && !connection.isClosed()) {
                try {
                    connection.close();
                } catch (IOException e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
    }

    private static String getString(VirtualHost virtualHost, HttpRequest request, HttpResponse response, ResponseWriter responseWriter)
        throws IOException {
        if(virtualHost.http_root == null){
            ErrorHandler.sendErrorResponse(response,null, HttpStatus.NOT_FOUND, responseWriter);
            return null;
        }
        String root = virtualHost.http_root.replaceFirst("/", "");
        String indexFileName =
            request.getPath().equals("/") ? "/index.html" : request.getPath();  //

        String filePath = root + indexFileName;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String content;

        try (InputStream inputStream = classLoader.getResourceAsStream(filePath)) {
            if (inputStream != null) {
                content = new String(inputStream.readAllBytes());
                return content;
            }
        }
        return null;
    }



    private static boolean isForbidden(String fileName){
        if(fileName.startsWith("/..")){
            return true;
        }else
            return fileName.matches(".*\\.(exe|sh|bat|cmd|msi|dll)$");
    }

    private static void determineHttpStatus(HttpRequest request, HttpResponse response){
        String method = request.getMethod();
        if(method.equals("POST") || method.equals("PUT")){
            response.setStatus(HttpStatus.CREATED);
        }else if(method.equals("DELETE")){
            response.setStatus(HttpStatus.NO_CONTENT);
        }else if(method.equals("GET")){
            response.setStatus(HttpStatus.OK);
        }
    }

}
