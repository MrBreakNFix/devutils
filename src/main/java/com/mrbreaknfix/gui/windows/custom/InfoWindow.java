package com.mrbreaknfix.gui.windows.custom;

import com.mrbreaknfix.gui.DevWindow;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import net.minecraft.client.MinecraftClient;

import java.util.List;

public class InfoWindow {

    public static void render() {
        MinecraftClient mc = MinecraftClient.getInstance();

        ImGui.begin("Info", ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoTitleBar);

        if (mc.player != null && mc.player.currentScreenHandler != null) {
            ImGui.text("Sync ID: " + mc.player.currentScreenHandler.syncId);
            ImGui.text("Revision: " + mc.player.currentScreenHandler.getRevision());
        }
        if (mc.currentScreen != null) {
            ImGui.text("Screen: " + mc.currentScreen.getTitle().getString());
        }
        ImGui.separator();

        // centered label text, with text "hidden windows", and on hover says "click to show"
        ImGui.text("Hidden Windows:");
        if (ImGui.isItemHovered()) {
            ImGui.setTooltip("Click to show");
        }

        // whitespace
        ImGui.spacing();

        List<DevWindow> hiddenWindows = DevWindow.getHiddenWindows();
        for (DevWindow window : hiddenWindows) {
            if (ImGui.button(window.getTitle())) {
                window.makeVisible();
            }
        }

        ImGui.end();
    }
}
