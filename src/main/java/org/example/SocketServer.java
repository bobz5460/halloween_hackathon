package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import com.google.gson.Gson;

public class SocketServer extends WebSocketServer {
    public Game game;

    public SocketServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));

    }

    public SocketServer(InetSocketAddress address, Game game) {
        super(address);
        this.game = game;
    }

    public SocketServer(int port, Draft_6455 draft) {
        super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
    }
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("connected");
        game.entityList.put(conn, new Entity(null,0,0,0,0,10,10, "player"));
    }
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        conn.send("closed");
    }
    @Override
    public void onMessage(WebSocket conn, String message) {
        //takes direction player wants to travel and other user input maybe
        //message should be json
        System.out.println("Received message");
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(message, Map.class);
        String msg_type = (String) map.get("msg_type");
        switch (msg_type) {
            case "name":
                game.entityList.get(conn).name = (String) map.get("name");
            case "direction":
                System.out.println("direction");
                String direction = (String) map.get("direction");
                if(direction.equals("up")){game.entityList.get(conn).setVel(new Integer[]{0,1});}
                else if(direction.equals("down")){game.entityList.get(conn).setVel(new Integer[]{0,-1});}
                else if(direction.equals("left")){game.entityList.get(conn).setVel(new Integer[]{-1,0});}
                else if(direction.equals("right")){game.entityList.get(conn).setVel(new Integer[]{1,0});}
            case "depress":
                game.entityList.get(conn).setVel(new Integer[]{0,0});
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
        System.out.println("Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }
    public void StepTick(){
        for (Entity entity : game.entityList.values()) {
            entity.applyVel();
            System.out.println(entity.getVel()[1]);
        }
        Gson gson = new Gson();
        String json = gson.toJson(game.entityList.values());
        broadcast(json);
    }
}
