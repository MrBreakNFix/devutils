package com.mrbreaknfix.gui.windows.custom;

import com.mrbreaknfix.Dev;
import com.mrbreaknfix.mixin.SessionAccessor;
import com.mrbreaknfix.utils.AuthUtils;
import imgui.ImGui;
import net.minecraft.client.session.Session;

import java.util.UUID;

import static com.mrbreaknfix.Dev.mc;
import static com.mrbreaknfix.gui.windows.custom.MultiplayerScreenWindow.*;

public class Account {
    public static void render() {
        ImGui.spacing();

        // Input text box to set username
        ImGui.pushItemWidth(ImGui.getWindowWidth() / scaleFactor);
        ImGui.inputText("##username", username);
        // check if the user is typing in the input box

        ImGui.popItemWidth();

        ImGui.sameLine();
        if (ImGui.button("Set Username")) {
            Dev.LOGGER.info("Setting Username: " + username.get());
            reqResult = "Setting Username: " + username.get();
            ((SessionAccessor) mc.getSession()).setUsername(username.get());
        }
        // Text input box to set UUID
        ImGui.pushItemWidth(ImGui.getWindowWidth() / scaleFactor);
        ImGui.inputText("##uuid", uuid);
        ImGui.popItemWidth();

        ImGui.sameLine();
        if (ImGui.button("Set UUID")) {
            String preCleanedUUID = uuid.get().replaceAll("\\s+", "");
            UUID cleanUUID = AuthUtils.cleanUUID(preCleanedUUID);
            Dev.LOGGER.info("Setting UUID: " + cleanUUID);
            reqResult = "Setting UUID: " + cleanUUID;
            ((SessionAccessor) mc.getSession()).setUuid(cleanUUID);
        }
        // Text input field to set access token
        ImGui.pushItemWidth(ImGui.getWindowWidth() / scaleFactor);
        ImGui.inputText("##accessToken", accessToken);
        ImGui.popItemWidth();
        ImGui.sameLine();
        if (ImGui.button("Set Access Token")) {
            // remove spaces and newlines
            String cleanedToken = accessToken.get().replaceAll("\\s+", "");
            Dev.LOGGER.info("Setting Access Token: " + cleanedToken);
            ((SessionAccessor) mc.getSession()).setAccessToken(cleanedToken);
            reqResult = "Access token set.";
        }
        // Dropdown menu for account type
        ImGui.pushItemWidth(ImGui.getWindowWidth() / scaleFactor);
        if (ImGui.combo("##hiddenLabel", selectedAccountType, AccountTypes)) {
            // Handle selection change
            switch (selectedAccountType.get()) {
                case 0:
                    accountType = Session.AccountType.MSA;
                    break;
                case 1:
                    accountType = Session.AccountType.MOJANG;
                    break;
                case 2:
                    accountType = Session.AccountType.LEGACY;
                    break;
            }
        }
        ImGui.popItemWidth();
        ImGui.sameLine();
        if (ImGui.button("Set Account Type")) {
            Dev.LOGGER.info("Setting Account Type: " + accountType.toString());
            ((SessionAccessor) mc.getSession()).setAccountType(accountType);
            reqResult = "Account type set to: " + accountType.toString();
        }

        // Output text in text area
//                ImGui.inputTextMultiline("##apiResult", new ImString(reqResult, 4098), ImGui.getWindowWidth() / scaleFactor, 100, ImGuiInputTextFlags.ReadOnly, null);
        showResult(null);
    }
}
