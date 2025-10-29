package org.example;

import org.java_websocket.WebSocket;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    public ConcurrentHashMap<WebSocket, Entity> entityList = new ConcurrentHashMap<>();

    public Game() {
        this.entityList = entityList;
    }
}
