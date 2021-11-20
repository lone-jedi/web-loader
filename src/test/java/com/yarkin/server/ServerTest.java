package com.yarkin.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServerTest {
    private Server server;

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
        assertThrows(IllegalArgumentException.class, () -> server.setPort(-1));
    }
    
    @Test
    public void setServerPortWhichOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> server.setPort(65535 + 1));
    }

    @Test
    public void setDefaultPort() {
        server.setDefaultPort();
        assertEquals(3000, server.getPort());
    }
}
