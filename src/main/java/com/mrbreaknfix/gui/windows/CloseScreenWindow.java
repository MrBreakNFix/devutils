package com.mrbreaknfix.gui.windows;

import com.mrbreaknfix.Dev;
import com.mrbreaknfix.gui.DevWindow;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;

public class CloseScreenWindow extends DevWindow {
    @Override
    public String getTitle() {
        return "Close Screen";
    }

    @Override
    protected void renderContent() {

        MinecraftClient mc = MinecraftClient.getInstance();
        assert mc.player != null;

        ImGui.begin("Close Screen", ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoTitleBar);

        // Super Close Button
        if (ImGui.button("Super Close")) {
            assert MinecraftClient.getInstance().currentScreen != null;
            MinecraftClient.getInstance().currentScreen.close();
        }
        // tooltip
        if (ImGui.isItemHovered()) {
            ImGui.setTooltip("Calls the super.close() method of the current screen, may be usefully if locked in a screen");
        }
        // Client Only
        if (ImGui.button("Client Close")) {
            MinecraftClient.getInstance().setScreen(null);
        }
        // tooltip
        if (ImGui.isItemHovered()) {
            ImGui.setTooltip("AKA: Soft Close");
        }

        // Server Only
        if (ImGui.button("Server Close")) {
            // send close screen packet
            if (mc.getNetworkHandler() != null && mc.player != null) {
                mc.getNetworkHandler().sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
            } else {
                Dev.LOGGER.warn("Minecraft network handler or player was null");
            }
        }
        ImGui.end();
    }
}
