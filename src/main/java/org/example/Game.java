package org.example;

import org.java_websocket.WebSocket;

import java.util.concurrent.ConcurrentHashMap;

public class Game {
    public ConcurrentHashMap<WebSocket, Entity> playerList = new ConcurrentHashMap<>();
    public Game() {
        this.playerList = playerList;

    }
}
