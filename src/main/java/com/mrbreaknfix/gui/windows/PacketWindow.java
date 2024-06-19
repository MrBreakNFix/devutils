package com.mrbreaknfix.gui.windows;

import com.mrbreaknfix.Dev;
import com.mrbreaknfix.gui.DevWindow;
import com.mrbreaknfix.utils.ImageUtils;
import imgui.ImGui;
import imgui.type.ImInt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mrbreaknfix.Dev.mc;


public class PacketWindow extends DevWindow {
    @Override
    public String getTitle() {
        return "Packet manipulation";
    }

    private final ImInt selectedChoice = new ImInt(0);
    private final ImInt selectedButton = new ImInt(0);
    private final String[] choices = {"PICKUP", "QUICK_MOVE", "SWAP", "CLONE", "QUICK_CRAFT", "PICKUP_ALL", "DROP"};
    private final String[] buttons = {"Left Click", "Right Click", "Middle Click"};
    ImInt syncId = new ImInt(0);
    ImInt revision = new ImInt(0);
    ImInt slotId = new ImInt(0);
    int[] img = {0, 0, 0};


    @Override
    protected void renderContent() {
        // Create 2 tabs
        if (ImGui.beginTabBar("Click Slot Packet")) {
            if (ImGui.beginTabItem("Click Slot")) {

                ImGui.separator();
                if (Dev.currentSlot != null) {
                    ImGui.textDisabled("Hovered Slot: " + Dev.currentSlot.id);
                } else {
                    ImGui.textDisabled("Hovered Slot: None");
                }

                if (Dev.lastClickedSlot != null) {
                    ImGui.textDisabled("Last Clicked Slot: " + Dev.lastClickedSlot.id);
                } else {
                    ImGui.textDisabled("Last Clicked Slot: None");
                }
                ImGui.separator();

                if (ImGui.button("Detect SyncID")) {
                    if (mc.player != null) {
                        syncId.set(mc.player.currentScreenHandler.syncId);
                        Dev.LOGGER.info("Sync ID: " + syncId.get());
                    }
                }
                ImGui.sameLine();
                if (ImGui.inputInt("Sync ID", syncId)) {
                    Dev.LOGGER.info("Sync ID: " + syncId.get());
                }


                if (ImGui.button("Detect Revision")) {
                    if (mc.player != null) {
                        revision.set(mc.player.currentScreenHandler.getRevision());
                        Dev.LOGGER.info("Revision: " + revision.get());
                    }
                }
                ImGui.sameLine();
                if (ImGui.inputInt("Revision", revision)) {
                    Dev.LOGGER.info("Revision: " + revision.get());
                }


                //Slot ID field as int
                if (ImGui.checkbox("Highlight Selected Slot", Dev.showSelectedSlot)) {
                    Dev.showSelectedSlot = !Dev.showSelectedSlot;
                }

                if (ImGui.button("Set as last clicked")) {
                    // button tooltip
                    ImGui.setTooltip("Sets the slot ID to the last clicked slot.");
                    if (Dev.lastClickedSlot != null) {
                        slotId.set(Dev.lastClickedSlot.id);
                        Dev.selectedSlot = Dev.lastClickedSlot.id;
                        Dev.LOGGER.info("Slot ID set to: " + slotId.get());
                    }
                }

                ImGui.sameLine();

                if (ImGui.inputInt("Slot ID", slotId)) {
                    Dev.LOGGER.info("Slot ID: " + slotId.get());
                    Dev.selectedSlot = slotId.get();
                }


                if (ImGui.combo("Select Choice", selectedChoice, choices)) {
                    // Handle selection change
                    switch (selectedChoice.get()) {
                        case 0:
                            Dev.LOGGER.info("PICKUP");
                            break;
                        case 1:
                            Dev.LOGGER.info("QUICK_MOVE");
                            break;
                        case 2:
                            Dev.LOGGER.info("SWAP");
                            break;
                        case 3:
                            Dev.LOGGER.info("CLONE");
                            break;
                        case 4:
                            Dev.LOGGER.info("QUICK_CRAFT");
                            break;
                        case 5:
                            Dev.LOGGER.info("PICKUP_ALL");
                            break;
                        case 6:
                            Dev.LOGGER.info("DROP");
                            break;
                    }
                }
                // Button dropdown 0, 1, 2 : 0 = left click, 1 = right click, 2 = middle click
                if (ImGui.combo("Select Button", selectedButton, buttons)) {
                    // Handle selection change
                    switch (selectedButton.get()) {
                        case 0:
                            Dev.LOGGER.info("Left Click");
                            break;
                        case 1:
                            Dev.LOGGER.info("Right Click");
                            break;
                        case 2:
                            Dev.LOGGER.info("Middle Click");
                            break;
                    }
                }
                ImGui.endTabItem();
            }
        }
//        if (ImGui.beginTabItem("Button Click")) {
//            ImGui.text("Receive packets");
//            ImGui.endTabItem();
//        }

        if (ImGui.beginTabItem("Screen References")) {
            ImGui.text("Slot ID Screen Reference");
            if(ImGui.checkbox("Show all Slot IDs", Dev.showSlotIDs)) {
                Dev.showSlotIDs = !Dev.showSlotIDs;
            }
            if (ImGui.checkbox("Show Slot IDs on hover", Dev.highlightSlotIDsOnHover)) {
                Dev.highlightSlotIDsOnHover = !Dev.highlightSlotIDsOnHover;
            }
            if (ImGui.checkbox("Highlight List Clicked Slot ID", Dev.highlightLastClickedSlotID)) {
                Dev.highlightLastClickedSlotID = !Dev.highlightLastClickedSlotID;
            }


            if (ImGui.button("Get Screen Reference")) {
                if (mc.player != null && mc.currentScreen != null) {
                    String key = null;

                    String title = mc.currentScreen.getTitle().toString();
                    Dev.LOGGER.info("St title: " + title);

                    Pattern pattern = Pattern.compile("key='(.*?)'");
                    Matcher matcher = pattern.matcher(title);

                    if (matcher.find()) {
                        key = matcher.group(1);
                    }

                    if (key != null) {
                        Dev.LOGGER.info("Screen Reference: " + key);
                        key = key.replace(".", "_");
                        key = key.toLowerCase();

                        // check if file exists
                        String path = "docs/" + key + ".png";
                        if (!ImageUtils.fileExists(path)) {
                            Dev.LOGGER.error("File does not exist: " + path);

                            img = ImageUtils.imageFromPath(path);

                            return;
                        }


                        Dev.LOGGER.info("Key: " + key);
                    }

                }

            }
            if (img[0] != 0) {
                ImGui.image(img[0], img[1], img[2]);
            }


            ImGui.endTabItem();
        }
        ImGui.endTabBar();
    }
}