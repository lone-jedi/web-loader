package com.yarkin.server.request;

import com.yarkin.server.resource.ResourceReader;
import com.yarkin.server.response.ResponseWritter;

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
        ResponseWritter responseWritter = new ResponseWritter(writer);

        String requestText = requestParser.getRequestText();
        if(!requestParser.isHttp(requestText)) {
            response = responseWritter.getBadRequestResponse("Bad HTTP Request");
            responseWritter.write(response);
            return;
        }

        String uri = requestParser.parseUrl(requestText);

        ResourceReader resourceReader = new ResourceReader(webAppPath, uri);
        if(!resourceReader.resourceExists()) {
            response = responseWritter.getNotFoundResponse("404 Not Found");
            responseWritter.write(response);
            return;
        }

        String content = resourceReader.readResource(uri);
        response = responseWritter.getSuccessResponse(content);
        responseWritter.write(response);
    }
}
