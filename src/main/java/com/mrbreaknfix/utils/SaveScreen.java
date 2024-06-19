package com.mrbreaknfix.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.screen.ScreenHandler;

public class SaveScreen {
    private static final int SLOT_COUNT = 3;
    private static final Screen[] screens = new Screen[SLOT_COUNT];
    private static final ScreenHandler[] screenHandlers = new ScreenHandler[SLOT_COUNT];
    private static final int[] syncIds = new int[SLOT_COUNT];

    public static void saveSlot(int slot) {
        assert MinecraftClient.getInstance().player != null;

        if (slot >= 1 && slot <= SLOT_COUNT) {
            int index = slot - 1;
            screens[index] = MinecraftClient.getInstance().currentScreen;
            screenHandlers[index] = MinecraftClient.getInstance().player.currentScreenHandler;
            syncIds[index] = screenHandlers[index].syncId;
        }
    }

    public static void loadScreen(int slot) {
        assert MinecraftClient.getInstance().player != null;

        if (slot >= 1 && slot <= SLOT_COUNT) {
            int index = slot - 1;
            MinecraftClient.getInstance().setScreen(screens[index]);
            MinecraftClient.getInstance().player.currentScreenHandler = screenHandlers[index];
        }
    }

    public static String getScreenNameFromSlot(int slot) {
        if (slot >= 1 && slot <= SLOT_COUNT && screens[slot - 1] != null) {
            return screens[slot - 1].getTitle().getString();
        }
        return "";
    }

    public static int getSyncIdFromSlot(int slot) {
        if (slot >= 1 && slot <= SLOT_COUNT) {
            return syncIds[slot - 1];
        }
        return -1;
    }
}
