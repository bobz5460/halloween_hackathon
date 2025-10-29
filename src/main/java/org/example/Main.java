package org.example;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.util.concurrent.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Main {
    Game game = new Game();
    SocketServer server = new SocketServer(new InetSocketAddress(6767), game);

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    public void start() {
        server.start();
        final long tickRate = 1; // ticks per second
        long period = 1_000_000_000L / tickRate; // nanoseconds
        scheduler.scheduleAtFixedRate(() -> {
            try {
                tick();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }, 0, period, TimeUnit.NANOSECONDS);
    }

    public void tick() {
        server.StepTick();
        System.out.println("Ticked");
    }
    public void stop() {
        scheduler.shutdownNow();

    }
    public static void main(String[] args) {

        Main s = new Main();
        s.start();
        }
    }