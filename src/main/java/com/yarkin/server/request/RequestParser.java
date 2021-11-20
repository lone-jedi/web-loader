package com.yarkin.server.request;

import com.yarkin.server.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestParser {
    private static final Pattern httpFirstLinePattern =
            Pattern.compile("^\\w{3,6}\\s/[a-zA-Z0-9-?=&\\./]*\\sHTTP/((1\\.1)|2)\\n");
    private static final Pattern httpUrlPattern = Pattern.compile("/[\\w\\d\\./\\-?&=]*");

    private BufferedReader reader;

    public RequestParser() {
    }

    public RequestParser(BufferedReader reader) {
        this.reader = reader;
    }

    public Request parse(String requestText) throws IOException {
        Request request = new Request();
        String[] headersAndBody = requestText.split("\n\n", 2);
        String[] headers = headersAndBody[0].split("\n");
        String[] firstLineValues = headers[0].split("\s", 3);

        request.setHttpMethod(HttpMethod.valueOf(firstLineValues[0]));
        request.setUrl(firstLineValues[1]);
        request.setHttpVersion(firstLineValues[2]);

        request.setHeaders(new HashMap<>());
        for (int i = 1; i < headers.length; i++) {
            String[] keyAndValue = headers[i].split(": ", 2);
            request.getHeaders().put(keyAndValue[0], keyAndValue[1]);
        }

        request.setBody(headersAndBody.length == 2 ? headersAndBody[1] : null);

        return request;
    }

    public boolean isHttp(String requestText) throws IOException {
        return httpFirstLinePattern.matcher(requestText).find();
    }

    public String parseUrl(String requestText) {
        Matcher matcher = httpUrlPattern.matcher(requestText);
        if(!matcher.find()) {
            throw new IllegalStateException("Url not found");
        }
        return requestText.substring(matcher.start(), matcher.end());
    }

    public String parseUri(String requestText) {
        return parseUrl(requestText).split("\\?")[0];
    }

    public String getRequestText() {
        return null;
    }
}
