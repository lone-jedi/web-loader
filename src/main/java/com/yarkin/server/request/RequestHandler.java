package com.yarkin.server.request;

import com.yarkin.server.resource.ResourceReader;
import com.yarkin.server.response.ResponseWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class RequestHandler {
    private BufferedReader reader;
    private BufferedWriter writer;
    private String webAppPath;

    public RequestHandler(BufferedReader reader, BufferedWriter writer, String webAppPath) {
        this.reader = reader;
        this.writer = writer;
        this.webAppPath = webAppPath;
    }

    public void handle() throws IOException {
        String response = "";
        RequestParser requestParser = new RequestParser(reader);
        ResponseWriter responseWriter = new ResponseWriter(writer);

        String requestText = requestParser.getRequestText();
        if(!requestParser.isHttp(requestText)) {
            response = responseWriter.getResponse("Bad HTTP Request", 400);
            responseWriter.write(response);
            return;
        }

        String uri = requestParser.parseUrl(requestText);
        ResourceReader resourceReader = new ResourceReader(webAppPath, uri);
        if(!resourceReader.resourceExists()) {
            response = responseWriter.getResponse("404 Not Found", 404);
            responseWriter.write(response);
            return;
        }

        String content = resourceReader.readResource();
        response = responseWriter.getResponse(content, 200);
        responseWriter.write(response);
    }
}
