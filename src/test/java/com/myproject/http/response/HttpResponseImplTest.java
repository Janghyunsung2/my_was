package com.myproject.http.response;

import static org.mockito.Mockito.when;

import com.myproject.http.handler.HttpStatus;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class HttpResponseImplTest {
    private Socket mockSocket;
    private ByteArrayOutputStream mockOutputStream;
    private HttpResponseImpl httpResponse;

    @Before
    public void setUp() throws IOException {
        // 🔹 Mock Socket 및 OutputStream 생성
        mockSocket = Mockito.mock(Socket.class);
        mockOutputStream = new ByteArrayOutputStream();
        when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);

        // 🔹 HttpResponseImpl 객체 생성
        httpResponse = new HttpResponseImpl();
    }


}