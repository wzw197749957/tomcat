package com.wzw.tomcat;

import com.wzw.tomcat.listener.ConnectorListener;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestProcessor extends Thread {

    private final Socket socket;
    private final ConnectorListener connectorListener;


    public RequestProcessor(Socket socket, ConnectorListener connectorListener) {
        this.socket = socket;
        this.connectorListener = connectorListener;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            Request request = new Request(inputStream);
            Response response = new Response(outputStream);

            connectorListener.service(request, response);

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
