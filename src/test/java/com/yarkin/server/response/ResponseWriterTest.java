package com.yarkin.server.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseWriterTest {
    private ResponseWriter writer;

    @BeforeEach
    public void before() {
        writer = new ResponseWriter();
    }

    @Test
    public void getSuccessHttpResponse() {
        String expected = "HTTP/1.1 200 OK\n\nHello there";
        String actual = writer.getResponse("Hello there", 200);
        assertEquals(expected, actual);
    }

    @Test
    public void getNotFoundResponse() {
        String expected = "HTTP/1.1 404 Not Found\n\n404 Not Found";
        String actual = writer.getResponse("404 Not Found", 404);
        assertEquals(expected, actual);
    }

    @Test
    public void getBadRequestResponse() {
        String expected = "HTTP/1.1 400 Bad Request\n\n400 Bad Request";
        String actual = writer.getResponse("400 Bad Request", 400);
        assertEquals(expected, actual);
    }
}
