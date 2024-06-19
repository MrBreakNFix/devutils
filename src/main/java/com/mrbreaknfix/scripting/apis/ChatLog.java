package com.mrbreaknfix.scripting.apis;

import com.mrbreaknfix.scripting.ApiBase;
import net.minecraft.text.Text;
import org.apache.http.HttpException;

import java.util.Map;

import static com.mrbreaknfix.Dev.mc;

public class ChatLog extends ApiBase {

    @Override
    public String getRoute() {
        return "chatlog";
    }

    @Override
    public String post(Map<String, String> fields)  {
        String message = fields.get("message");
        if (mc.player != null) {
            mc.player.sendMessage(Text.of(message));
        }
        return null;
    }
}