package com.yarkin.server;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ServerTest {
    private Server server;

    @BeforeAll
    static void initialize() {
        new File("src/test/resources/web-app").mkdirs();
    }

    @BeforeEach
    public void before() {
        server = new Server();
    }

    @Test
    public void setServerPort() {
        assertDoesNotThrow(() -> server.setPort(8080));
        assertDoesNotThrow(() -> server.setPort(0));
        assertDoesNotThrow(() -> server.setPort(65535));
    }

    @Test
    public void setServerPortLessThanZero() {
        assertThrows(IllegalArgumentException.class, () -> server.setPort(-1),
                "The port \"-1\" out of range [0, 65535]");
    }
    
    @Test
    public void setServerPortWhichOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> server.setPort(65535 + 1),
                "The port \"65536\" out of range [0, 65535]");
    }

    @Test
    public void setDefaultPort() {
        server.setDefaultPort();
        assertEquals(3000, server.getPort());
    }

    @Test
    public void setExistsWebAppPath() {
        assertDoesNotThrow(() -> server.setWebAppPath("src/test/resources/web-app"));
        assertDoesNotThrow(() -> server.setWebAppPath("src/test/resources/web-app/"));
        assertDoesNotThrow(() -> server.setWebAppPath("src/test/resources///web-app//"));
    }

    @Test
    public void setNotExistsWebAppPath() {
        assertThrows(IllegalArgumentException.class, () -> {
            server.setWebAppPath("src/test/resources/some-folder");
        }, "Directory \"src/test/resources/some-folder\" does not exist");

        assertThrows(IllegalArgumentException.class, () -> {
            server.setWebAppPath("src/test/resources/web-app/some-folder");
        }, "Directory \"src/test/resources/web-app/some-folder\" does not exist");

    }
}
