package com.yarkin.server.request;

import com.yarkin.server.HttpMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class RequestParserTest {
    private RequestParser requestParser;
    private final String requestText = "GET /wiki/page HTTP/1.1\n" +
            "Host: ru.wikipedia.org\n" +
            "User-Agent: Mozilla/5.0 (X11; U; Linux i686; ru; rv:1.9b5) Gecko/2008050509 Firefox/3.0b5\n" +
            "Accept: text/html\n" +
            "Connection: close\n";

    @BeforeEach
    public void before() {
        requestParser = new RequestParser();
    }

    @Test
    public void whenRequestIsHttp() throws IOException {
        assertTrue(requestParser.isHttp(requestText));
        assertTrue(requestParser.isHttp("GET / HTTP/1.1\n"));
        assertTrue(requestParser.isHttp("GET / HTTP/1.1\n\nHello there"));
        assertTrue(requestParser.isHttp("GET /wiki/page/hello.html HTTP/1.1\n\nHello there"));
        assertTrue(requestParser.isHttp("GET /wiki/page/hello.html HTTP/2\n"));
        assertTrue(requestParser.isHttp("POST /wiki/page/hello.html HTTP/2\n\nPost content"));
        assertTrue(requestParser.isHttp("PUT /wiki/page/hello.html?id=3 HTTP/2\n\nContent"));
        assertTrue(requestParser.isHttp("DELETE /wiki/page/hello.html?id=2&uid=hello HTTP/2\n"));
    }

    @Test
    public void whenRequestIsNotHttp() throws IOException {
        assertFalse(requestParser.isHttp("Hello there!\n Some text\n"));
        assertFalse(requestParser.isHttp("/wiki/page HTTP/1.1\n"));
        assertFalse(requestParser.isHttp("HTTP/1.1\n"));
        assertFalse(requestParser.isHttp("GET HTTP/1.1\n"));
        assertFalse(requestParser.isHttp("GET /wiki/page HTTP/1.1"));
        assertFalse(requestParser.isHttp(" GET /wiki/page HTTP/1.1\n"));
    }

    @Test
    public void parseUrlFromHttpRequest() {
        String actual = requestParser.parseUrl(requestText);
        assertEquals("/wiki/page", actual);
    }

    @Test
    public void parseUrlFromFirstHttpRequestLine() {
        assertEquals("/", requestParser.parseUrl("GET / HTTP/1.1\n"));
        assertEquals("/home", requestParser.parseUrl("GET /home HTTP/1.1\n"));
        assertEquals("/home/hello-world.js", requestParser.parseUrl("GET /home/hello-world.js HTTP/1.1\n"));
        assertEquals("/home/res/a/b/page-a-b.css", requestParser.parseUrl("GET /home/res/a/b/page-a-b.css HTTP/1.1\n"));
        assertEquals("/home/index.html", requestParser.parseUrl("GET /home/index.html HTTP/1.1\n"));
        assertEquals("/home/index.html?id=3&name=Alex", requestParser.parseUrl("GET /home/index.html?id=3&name=Alex HTTP/1.1\n"));
    }

    @Test
    public void parseUriFromHttpRequest() {
        String actual = requestParser.parseUri(requestText);
        assertEquals("/wiki/page", actual);
    }

    @Test
    public void parseUriFromFirstHttpRequestLine() {
        assertEquals("/", requestParser.parseUri("GET / HTTP/1.1\n"));
        assertEquals("/home", requestParser.parseUri("GET /home HTTP/1.1\n"));
        assertEquals("/home/hello-world.js", requestParser.parseUri("GET /home/hello-world.js HTTP/1.1\n"));
        assertEquals("/home/res/a/b/page-a-b.css", requestParser.parseUri("GET /home/res/a/b/page-a-b.css HTTP/1.1\n\nHello there"));
        assertEquals("/home/index.html", requestParser.parseUri("GET /home/index.html HTTP/1.1\n"));
        assertEquals("/home/index.html", requestParser.parseUri("GET /home/index.html?id=3&name=Alex HTTP/1.1\n"));
    }

    @Test
    public void tryToParseIncorrectUrlAndUri() {
        assertThrows(IllegalStateException.class, () -> {
            requestParser.parseUrl("Hello there");
        }, "Url not found");
        assertThrows(IllegalStateException.class, () -> {
            requestParser.parseUri("Hello there");
        }, "Url not found");
        assertThrows(IllegalStateException.class, () -> {
            requestParser.parseUri("word");
        }, "Url not found");
    }

    @Test
    public void parseHttpRequestText() throws IOException {
        Request expected = new Request();
        expected.setHttpMethod(HttpMethod.GET);
        expected.setUrl("/wiki/page");
        expected.setHttpVersion("HTTP/1.1");

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Host", "ru.wikipedia.org");
        headers.put("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; ru; rv:1.9b5) Gecko/2008050509 Firefox/3.0b5");
        headers.put("Accept", "text/html");
        headers.put("Connection", "close");
        expected.setHeaders(headers);

        expected.setBody(null);

        Request actual = requestParser.parse(requestText);

        assertTrue(expected.getHttpMethod().equals(actual.getHttpMethod()));
        assertTrue(expected.getUrl().equals(actual.getUrl()));
        assertTrue(expected.getHttpVersion().equals(actual.getHttpVersion()));
        assertTrue(actual.getBody() == null);
        assertTrue(expected.getHeaders().equals(actual.getHeaders()));
    }
}
