package org.example;
import java.util.concurrent.*;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        SocketServer server = new SocketServer(new InetSocketAddress(6767), game);
        server.start();
        }
}