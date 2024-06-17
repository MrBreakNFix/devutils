package com.mrbreaknfix.gui.windows;

import com.mrbreaknfix.gui.DevWindow;
import com.mrbreaknfix.utils.SaveScreen;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class SaveLoadScreenWindow extends DevWindow {

    @Override
    public String getTitle() {
        return "Save & Load Screens";
    }

    protected boolean shouldProtectSyncID;

    @Override
    protected void renderMenuItems() {
        if (ImGui.menuItem("SyncID Loss prevention")) {
            // todo: add prevention
            shouldProtectSyncID = !shouldProtectSyncID;
            MinecraftClient.getInstance().player.sendMessage(Text.of(String.valueOf(shouldProtectSyncID)), false);
            // todo: render checkmark
        }
        if (ImGui.menuItem("SyncID change notifications")) {
            // todo: add notifications
            // send a placeholder chat message
            MinecraftClient.getInstance().player.sendMessage(Text.of("SyncID change notifications are not yet implemented"), false);
        }
    }

    @Override
    protected void renderContent() {
        MinecraftClient mc = MinecraftClient.getInstance();
        assert mc.player != null;

        for (int slot = 1; slot <= 3; slot++) {
            String screenName = SaveScreen.getScreenNameFromSlot(slot);

            formatButtons(mc, slot, screenName);

            if (ImGui.button("Save Slot " + slot)) {
                SaveScreen.saveSlot(slot);
            }

            if (!screenName.isEmpty()) {
                ImGui.popStyleColor(2);
            }

            if (ImGui.isItemHovered()) {
                ImGui.setTooltip("Save " + screenName);
            }

            ImGui.sameLine();

            formatButtons(mc, slot, screenName);

            if (ImGui.button("Load Slot " + slot)) {
                SaveScreen.loadScreen(slot);
            }

            if (!screenName.isEmpty()) {
                ImGui.popStyleColor(2);
            }

            if (ImGui.isItemHovered()) {
                ImGui.setTooltip("Load " + screenName);
            }

            ImGui.sameLine();
            ImGui.text("Saved SyncID: " + SaveScreen.getSyncIdFromSlot(slot));

            if (slot < 3) {
                ImGui.newLine();
            }
        }

        ImGui.newLine();
        ImGui.text("Yellow: SyncID matches (good)");
        ImGui.textWrapped("Red: SyncID is probably de-synced from server. Hover for more info");
        if (ImGui.isItemHovered()) {
            ImGui.setTooltip("For most exploits, a mismatched SyncID means it is only client side. Changing the SyncID can be avoided by turning on \"Prevent SyncID Loss\" under the menu bar.");
        }
    }

    private void formatButtons(MinecraftClient mc, int slot, String screenName) {
        if (!screenName.isEmpty()) {
            if (mc.player != null && mc.player.currentScreenHandler.syncId == SaveScreen.getSyncIdFromSlot(slot)) {
                ImGui.pushStyleColor(ImGuiCol.Button, ImGui.getColorU32(1.0f, 1.0f, 0.0f, 1.0f)); // Yellow color
            } else {
                ImGui.pushStyleColor(ImGuiCol.Button, ImGui.getColorU32(1.0f, 0.0f, 0.0f, 1.0f)); // Red color
            }
            ImGui.pushStyleColor(ImGuiCol.Text, ImGui.getColorU32(0.0f, 0.0f, 0.0f, 1.0f)); // Black text color
        }
    }
}
