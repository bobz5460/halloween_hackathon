package org.example;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import com.google.gson.Gson;

public class SocketServer extends WebSocketServer {
    public Game game;

    public SocketServer(InetSocketAddress address, Game game) {
        super(address);
        this.game = game;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("connected");

    }
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        conn.send("closed");
        game.playerList.remove(conn);
    }
    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Received message");
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(message, Map.class);
        String msg_type = (String) map.get("msg_type");
        switch (msg_type) {
            case "connect":
                game.playerList.put(conn, new Player((String) map.get("name"),0,0,0,0,100,100, "player", 0));
            case "direction":
                System.out.println("direction");
                String direction = (String) map.get("direction");
                switch (direction) {
                    case "up" -> game.playerList.get(conn).setVel(new Integer[]{0, -10});
                    case "down" -> game.playerList.get(conn).setVel(new Integer[]{0, 10});
                    case "left" -> game.playerList.get(conn).setVel(new Integer[]{-10, 0});
                    case "right" -> game.playerList.get(conn).setVel(new Integer[]{10, 0});
                    case "none" -> game.playerList.get(conn).setVel(new Integer[]{0, 0});
                }
        }
    }
    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        // what does this do? well never know
    }
    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    @Override
    public void onStart() {
        final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        final long tickRate = 60; // ticks per second
        long period = 1_000_000_000L / tickRate; // nanoseconds
        scheduler.scheduleAtFixedRate(() -> {
            try {
                StepTick();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }, 0, period, TimeUnit.NANOSECONDS);
        System.out.println("Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }
    public void StepTick(){
        long startTime = System.currentTimeMillis();
        // because who cares about time complexity anyway
        for (Entity entity : game.playerList.values()) {
            entity.collision = false;
        }
        for (Entity entity : game.playerList.values()) {
            for (Entity entity2 : game.playerList.values()) {
                if (entity.collidingWithEntity(entity2) && entity != entity2) {
                    System.out.println("collision");
                    entity.collision = true;
                    entity2.collision = true;
                }
            }
            entity.applyVel();
        }
        Gson gson = new Gson();
        String json = gson.toJson(game.playerList.values());
        broadcast(json);
        long endTime = System.currentTimeMillis();
        System.out.println("Tick took "+ (endTime - startTime) +" ms");
    }
}
