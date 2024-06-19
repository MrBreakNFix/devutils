package com.mrbreaknfix.scripting.ws;

import com.mrbreaknfix.Dev;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static com.mrbreaknfix.scripting.Server.isAvailable;

public class WSServer extends WebSocketServer {

    private static WSServer instance;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final Map<WebSocket, Boolean> connectedClients = new ConcurrentHashMap<>();

    private WSServer(InetSocketAddress address) {
        super(address);
    }

    public static WSServer getInstance() {
        int port = 0;
        for (int i = 3000; i < 4000; i++) {
            if (isAvailable(i)) {
                port = i;
                break;
            }
        }

        if (port == 0) {
            Dev.LOGGER.error("WebSocket server not started");
            return null;
        }

        if (instance == null) {
            Dev.webSocketPort = port;
            InetSocketAddress defaultAddress = new InetSocketAddress("localhost", port);
            instance = new WSServer(defaultAddress);
        }
        return instance;
    }

    public void startServer() {
        this.start();
        Dev.LOGGER.info("WebSocket server started on ws://" + getAddress().getHostName() + ":" + getAddress().getPort() + "/ws");
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Dev.LOGGER.info("New connection from " + conn.getRemoteSocketAddress());
        connectedClients.put(conn, true);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Dev.LOGGER.info("Closed connection to " + conn.getRemoteSocketAddress());
        connectedClients.remove(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Dev.LOGGER.info("Message from " + conn.getRemoteSocketAddress() + ": " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        Dev.LOGGER.info("Server started successfully");
    }

    public void sendMessageToAllClients(String message) {
        try {
            for (WebSocket client : connectedClients.keySet()) {
                client.send(message);
            }
        } catch (Exception e) {
            Dev.LOGGER.error("Error sending message to clients: " + e.getMessage());
        }
    }
}
