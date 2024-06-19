package com.mrbreaknfix.scripting;

import com.mrbreaknfix.Dev;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class Server {

    public static Server instance = new Server();
    private HttpServer server;
    private String uri;

    private Server() {

    }

    public void start() {
        int port = 0;
        for (int i = 3000; i < 4000; i++) {
            if (isAvailable(i)) {
                port = i;
                break;
            }
        }

        if (port == 0) {
            Dev.LOGGER.error("HTTP server not started");
            return;
        }

        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        server.createContext("/api/", new ApiHandler());
        server.createContext("/", new RootHandler());

        server.setExecutor(null);
        server.start();

        uri = "http://localhost:" + port + "/";
        Dev.url = uri;
        Dev.serverPort = port;

        Dev.LOGGER.info("Http server started on port " + port);
    }

    public String getUrl() {
        return uri;
    }

    public static boolean isAvailable(int port) {

        ServerSocket socket = null;
        try {
            socket = new ServerSocket(port);
            socket.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {

                }
            }
        }

        return false;
    }
}