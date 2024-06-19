package com.mrbreaknfix.scripting.ws;

public class MessageSender {
    public void send(String message) {
        WSServer.getInstance().sendMessageToAllClients(message);
    }
}
