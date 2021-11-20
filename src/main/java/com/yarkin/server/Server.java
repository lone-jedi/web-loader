package com.yarkin.server;

import com.yarkin.server.request.RequestHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int DEFAULT_PORT = 3000;
    private int port;
    private String webAppPath;

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader reader = new BufferedReader(
                             new InputStreamReader(socket.getInputStream())
                     );
                     BufferedWriter writer = new BufferedWriter(
                             new OutputStreamWriter(socket.getOutputStream())
                     );) {
                    // server code here...
                    RequestHandler requestHandler = new RequestHandler();
                    requestHandler.setReader(reader);
                    requestHandler.setWriter(writer);
                    requestHandler.setWebAppPath(webAppPath);

                    requestHandler.handle();
                }
            }
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("The port \"" + port + "\" out of range [0, 65535]");
        }

        this.port = port;
    }

    public void setDefaultPort() {
        setPort(DEFAULT_PORT);
    }

    public void setWebAppPath(String webAppPath) {
        this.webAppPath = webAppPath;
    }
}
