package org.example;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Map;
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
        game.entityList.put(conn, new Entity(null,0,0,0,0,100,100, "player"));
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
                switch (direction) {
                    case "up" -> game.entityList.get(conn).setVel(new Integer[]{0, 4});
                    case "down" -> game.entityList.get(conn).setVel(new Integer[]{0, -4});
                    case "left" -> game.entityList.get(conn).setVel(new Integer[]{-4, 0});
                    case "right" -> game.entityList.get(conn).setVel(new Integer[]{4, 0});
                    case "none" -> game.entityList.get(conn).setVel(new Integer[]{0, 0});
                }
            //case "depress":
                //game.entityList.get(conn).setVel(new Integer[]{0,0});
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
        // because who cares about time complexity anyway
        for (Entity entity : game.entityList.values()) {
            entity.collision = false;
        }
        for (Entity entity : game.entityList.values()) {
            for (Entity entity2 : game.entityList.values()) {
                if (entity.collidingWithEntity(entity2) && entity != entity2) {
                    System.out.println("collision");
                    entity.collision = true;
                    entity2.collision = true;
                }
            }
            entity.applyVel();
        }
        Gson gson = new Gson();
        String json = gson.toJson(game.entityList.values());
        broadcast(json);

    }
}
