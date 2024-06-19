package com.mrbreaknfix.scripting.apis.playeractions;

import com.mrbreaknfix.scripting.ApiBase;

import java.util.Map;

import static com.mrbreaknfix.Dev.mc;

public class Chat extends ApiBase {

    @Override
    public String getRoute() {
        return "chat";
    }

    @Override
    public String post(Map<String, String> fields)  {
        String message = fields.get("message");
        if (mc.player != null) {
            mc.inGameHud.getChatHud().addToMessageHistory(message);

            if (message.startsWith("/")) mc.player.networkHandler.sendChatCommand(message.substring(1));
            else mc.player.networkHandler.sendChatMessage(message);

        }
        return null;
    }
}