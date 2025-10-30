package org.example;
import java.util.concurrent.*;
import java.net.InetSocketAddress;

public class Main {
    Game game = new Game();
    SocketServer server = new SocketServer(new InetSocketAddress(6767), game);

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    public void start() {
        server.start();
        final long tickRate = 60; // ticks per second
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
        long startTime = System.currentTimeMillis();
        server.StepTick();
        long endTime = System.currentTimeMillis();
        System.out.println("Tick took "+ (endTime - startTime) +" ms");
    }
    public void stop() {
        scheduler.shutdownNow();

    }
    public static void main(String[] args) {

        Main s = new Main();
        s.start();
        }
    }