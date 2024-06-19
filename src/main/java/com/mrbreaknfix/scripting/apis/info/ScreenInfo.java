package com.mrbreaknfix.scripting.apis.info;

import com.mrbreaknfix.scripting.ApiBase;

import java.util.Map;

import static com.mrbreaknfix.Dev.mc;

public class ScreenInfo extends ApiBase {

    @Override
    public String getRoute() {
        return "screeninfo";
    }

    @Override
    public String post(Map<String, String> fields) {
        String request = fields.get("wants");
        if (request == null || mc.currentScreen == null || mc.player == null) {
            return null;
        }

        return switch (request) {
            case "totalSlots" -> String.valueOf(mc.player.currentScreenHandler.slots.size());
            case "title" -> mc.currentScreen.getTitle().getString();
            case "getItemInSlot" -> {
                String slot = fields.get("slot");
                if (slot == null) {
                    yield null;
                } else {
                    // returns "minecraft:stone" for example
                    yield mc.player.currentScreenHandler.getSlot(Integer.parseInt(slot)).getStack().getName().getString();
                }
            }
            default -> null;
        };
    }
}
