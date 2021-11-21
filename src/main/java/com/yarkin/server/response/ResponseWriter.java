package com.yarkin.server.response;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

public class ResponseWriter {
    private BufferedWriter writer;
    private HashMap<Integer, String> statusCodes = new HashMap<>();
    {
        statusCodes.put(200, "OK");
        statusCodes.put(404, "Not Found");
        statusCodes.put(400, "Bad Request");
    }

    public ResponseWriter() {
    }

    public ResponseWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public String getResponse(String content, int statusCode) {
        StringBuilder response = new StringBuilder("HTTP/1.1 ");
        response.append(statusCode + " ");
        response.append(statusCodes.get(statusCode));
        response.append("\n");
        if(content != null && !content.equals("")) {
            response.append("\n");
            response.append(content);
        }
        return response.toString();
    }

    public void write(String response) throws IOException {
        writer.write(response);
        writer.flush();
    }
}
