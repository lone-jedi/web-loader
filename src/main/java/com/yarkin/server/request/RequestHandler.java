package com.yarkin.server.request;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public class RequestHandler {
    private BufferedReader reader;
    private BufferedWriter writer;
    private String webAppPath;

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public void setWebAppPath(String webAppPath) {
        this.webAppPath = webAppPath;
    }

    public void handle() {
        // processing...
    }
}
