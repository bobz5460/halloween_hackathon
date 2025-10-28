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
    Game game;

    public SocketServer(int port, Game game) throws UnknownHostException {
        super(new InetSocketAddress(port));
        this.game = game;
    }

    public SocketServer(InetSocketAddress address) {
        super(address);
    }

    public SocketServer(int port, Draft_6455 draft) {
        super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
    }
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("connected");
        this.game.entityList.put(conn, new Entity(null,0,0,0,0,10,10, "player"));
    }
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        conn.send("closed");
    }
    @Override
    public void onMessage(WebSocket conn, String message) {
        //takes direction player wants to travel and other user input maybe
        //message should be json
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(message, Map.class);
        String msg_type = (String) map.get("msg_type");
        switch (msg_type) {
            case "name":
                this.game.entityList.get(conn).name = (String) map.get("name");
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

}
