package com.yarkin.server.resource;

import java.io.*;
import java.util.regex.Pattern;

public class ResourceReader {
    private static final Pattern hasExtensionPattern = Pattern.compile("\\.[\\w\\d]{2,4}\\s?$");
    private String webAppPath;
    private String uri;

    public ResourceReader(String webAppPath, String uri) {
        this.webAppPath = webAppPath;
        this.uri = uri;
    }

    public boolean resourceExists() {
        return new File(webAppPath, uri).exists();
    }

    public boolean hasUriExtension() {
        return hasExtensionPattern.matcher(uri).find();
    }

    public String readResource() throws IOException {
        if(!hasUriExtension()) {
            uri += "/index.html";
        }

        File resource = new File(webAppPath, uri);

        if(!resourceExists()) {
            throw new FileNotFoundException("File " + resource.getPath() + " not found");
        }

        String output = "";
        String current = null;
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(resource)));
        while((current = reader.readLine()) != null) {
            output += current;
        }

        return output;
    }
}
