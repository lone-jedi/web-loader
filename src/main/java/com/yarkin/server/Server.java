package com.yarkin.server;

public class Server {
    private static final int DEFAULT_PORT = 3000;
    private int port;
    private String webAppPath;

    public void start() {
        // server loop here ...
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        if(port < 0 || port > 65535) {
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
