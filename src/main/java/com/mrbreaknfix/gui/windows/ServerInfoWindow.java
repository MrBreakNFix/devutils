package com.mrbreaknfix.gui.windows;

import com.mrbreaknfix.gui.DevWindow;
import imgui.ImGui;

import java.util.Objects;

import static com.mrbreaknfix.Dev.mc;

public class ServerInfoWindow extends DevWindow {
    @Override
    public String getTitle() {
        return "Server Info";
    }
    @Override
    protected void renderContent() {
        ImGui.text("Player Count: " + (mc.getNetworkHandler() != null ? mc.getNetworkHandler().getPlayerList().size() : "N/A"));
        ImGui.text("Server: " + (mc.isIntegratedServerRunning() ? "Singleplayer" : Objects.requireNonNull(mc.getCurrentServerEntry()).address));
        ImGui.text("Server Type: " + (mc.getNetworkHandler().getServerInfo() != null ? mc.getNetworkHandler().getServerInfo().getServerType() : "N/A"));
        ImGui.text("Server protocol: " + (mc.getNetworkHandler().getServerInfo() != null ? mc.getNetworkHandler().getServerInfo().protocolVersion : "N/A"));

        ImGui.separator();

        if (ImGui.button("Get plugins")) {

        }
    }

}
