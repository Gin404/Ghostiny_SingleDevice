package com.example.ghostiny_singledevice.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        byte[] bytes = new byte[1024];
        try {
            InputStream inputStream = clientSocket.getInputStream();
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                String request = new String(bytes, 0, len, "UTF-8");
                if (request.equals("quit")) {
                    break;
                }
                System.out.println(String.format("Request from %s: %s", clientSocket.getRemoteSocketAddress(), request));
                String response = "You click " + request;
                clientSocket.getOutputStream().write(response.getBytes());
            }
        } catch (IOException e){
            System.out.println("Caught exception "+e);
            throw new RuntimeException(e);
        }
    }
}
