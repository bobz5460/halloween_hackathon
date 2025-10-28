package org.example;

import org.java_websocket.WebSocket;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    ConcurrentHashMap<WebSocket, Entity> entityList;
    public Game(ConcurrentHashMap<WebSocket, Entity> entityList) {
        this.entityList = entityList;
    }
}
