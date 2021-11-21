package com.yarkin.server.resource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceReaderTest {
    private static File rootDir;

    @BeforeAll
    public static void initialize() throws IOException {
        rootDir = new File("src/test/resources/web-app");
        rootDir.mkdirs();

        File indexFile = new File(rootDir, "index.html");
        indexFile.createNewFile();
        OutputStream outputHtml = new FileOutputStream(indexFile);
        outputHtml.write("<h1>Hello world</h1>".getBytes());
        outputHtml.close();

        File cssFile = new File(rootDir, "style.css");
        indexFile.createNewFile();
        OutputStream outputCss = new FileOutputStream(cssFile);
        outputCss.write("body { margin: 0; }".getBytes());
        outputCss.close();
    }

    @Test
    public void readFromIndexFile() throws IOException {
        ResourceReader reader = new ResourceReader(rootDir.getPath(), "/");
        String expected = "<h1>Hello world</h1>";
        String actual = reader.readResource();
        assertTrue(reader.resourceExists());
        assertEquals(expected, actual);
    }

    @Test
    public void readFromCssFile() throws IOException {
        ResourceReader reader = new ResourceReader(rootDir.getPath(), "/style.css");
        String actual = reader.readResource();
        assertTrue(reader.resourceExists());
        assertEquals("body { margin: 0; }", actual);
    }

    @Test
    public void readFromNotExistsFile() {
        ResourceReader reader = new ResourceReader(rootDir.getPath(), "/style22.css");
        assertFalse(reader.resourceExists());
        assertThrows(FileNotFoundException.class, () -> reader.readResource(),
                "File src/test/resources/web-app/style22.css not found");
    }
}
