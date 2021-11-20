package com.yarkin;

import com.yarkin.server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.setPort(3000);
        } catch(IllegalArgumentException e) {
            server.setDefaultPort();
        }

        server.setWebAppPath("src/main/resources/web-app");
        server.start();
    }
}
