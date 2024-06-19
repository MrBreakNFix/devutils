package com.mrbreaknfix.gui.windows.custom.api.username;

import com.mrbreaknfix.Dev;
import com.mrbreaknfix.gui.windows.custom.MultiplayerScreenWindow;
import com.mrbreaknfix.gui.windows.elements.TextInput;
import com.mrbreaknfix.utils.ApiUtils;
import imgui.ImGui;

import static com.mrbreaknfix.gui.windows.custom.MultiplayerScreenWindow.username;

public class UsernameToUUID {
    public static void render() {
        ImGui.text("Technical details:");
        ImGui.sameLine();
        MultiplayerScreenWindow.documentation("https://wiki.vg/Mojang_API#Username_to_UUID");

        ImGui.text("GET https://api.mojang.com/users/profiles/minecraft/<username>");
        ImGui.text("Authorization: none");

        ImGui.separator();

        // Input text box to set username
        ImGui.pushItemWidth(ImGui.getWindowWidth() / MultiplayerScreenWindow.scaleFactor);
//        BlockType.blockTextInputFromScreen("##username");
        new TextInput("##username", username);

        // Todo: Have a list of blockableInputText's , called by new blockTextInputFromScreen("##username,", username);
        //  and there will be a method that implements that class which is isBlocked, which will return true if any of the blockableInputText's are focused

        ImGui.popItemWidth();

        ImGui.sameLine();
        if (ImGui.button("Get UUID")) {
            Dev.LOGGER.info("Getting UUID for: " + username.get());
            ApiUtils.GET("https://api.mojang.com/users/profiles/minecraft/" + username.get(), null, (response) -> {
                MultiplayerScreenWindow.reqResult = response;
                MultiplayerScreenWindow.partOfInterest = "id";
            });

        }
    }
}
