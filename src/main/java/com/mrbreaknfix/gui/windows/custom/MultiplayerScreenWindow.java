package com.mrbreaknfix.gui.windows.custom;

import com.google.gson.*;
import com.mrbreaknfix.Dev;
import com.mrbreaknfix.gui.DevWindow;
import com.mrbreaknfix.gui.windows.custom.api.username.UsernameToUUID;
import com.mrbreaknfix.mixin.SessionAccessor;
import com.mrbreaknfix.utils.ApiUtils;
import com.mrbreaknfix.utils.AuthUtils;
import com.mrbreaknfix.utils.SkinPayload;
import com.mrbreaknfix.utils.UrlUtils;
import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiMouseCursor;
import imgui.type.ImInt;
import imgui.type.ImString;
import net.minecraft.client.session.Session;
import java.util.UUID;

import static com.mrbreaknfix.Dev.mc;

public class MultiplayerScreenWindow extends DevWindow {
    public static ImString username = new ImString(mc.getSession().getUsername(), 1024);
    public static ImString uuid = new ImString(String.valueOf(mc.getSession().getUuidOrNull()), 1024);
    public static ImString accessToken = new ImString(mc.getSession().getAccessToken(), 1024);
    public static String[] AccountTypes = {"MSA", "MOJANG", "LEGACY"};
    public static String[] skinTypes = {"Classic", "Slim"};
    // selected account type
    public static ImInt selectedAccountType = mc.getSession().getAccountType() == Session.AccountType.MSA ? new ImInt(0) : mc.getSession().getAccountType() == Session.AccountType.MOJANG ? new ImInt(1) : new ImInt(2);
    public static ImInt selectedSkinType = new ImInt(0);
    public static Session.AccountType accountType = mc.getSession().getAccountType();
    public static final float scaleFactor = 1.5f;
    public static String reqResult = "Information will be displayed here.";
    public static String partOfInterest = "";
    public static String docSt = "Documentation";
    public static ImString usernames = new ImString("", 4098);
    public static String skinType = "classic";
    public static ImString skinUrl = new ImString("", 4098);

    @Override
    public String getTitle() {
        return "Account & API";
    }

    @Override
    protected void renderContent() {
        ImGui.textColored(1.0f, 0.0f, 0.0f, 1.0f, "WARNING:");
        ImGui.sameLine();
        // wrap colored text
        ImGui.textWrapped("This window contains possibly sensitive information, which could allow somebody with it to potentially access your account. If somebody requests this information, it's a scam.");
        ImGui.spacing();

        ImGui.separator();

        ImGui.spacing();

        // tabs for 1. Account 2. API 3. Account Manager
        ImGui.beginTabBar("Account & API");
                if (ImGui.beginTabItem("Account")) {
                    Account.render();
                    ImGui.endTabItem();
                }

                if (ImGui.beginTabItem("Username APIs")) {
//                    if (ImGui.beginTabItem("Name")) {
                        // tabs for 1. Username to UUID
                        ImGui.text("Mojang API Name related endpoints:");
                        if (ImGui.beginTabBar("Name APIs")) {
                            // username to uuid
                            if (ImGui.beginTabItem("Name to UUID")) {
                                UsernameToUUID.render();
                                ImGui.endTabItem();
                            }
                            // Bulk usernames to uuid
                            if (ImGui.beginTabItem("Bulk Name to UUID")) {
                                ImGui.text("Technical details:");
                                ImGui.sameLine();
                                documentation("https://wiki.vg/Mojang_API#Usernames_to_UUIDs");

                                ImGui.text("POST https://api.minecraftservices.com/minecraft/profile/lookup/bulk/byname");
                                ImGui.text("Authorization: none");
                                ImGui.separator();

                                ImGui.text("Input usernames separated by commas");
                                ImGui.inputTextMultiline("##usernames", usernames, ImGui.getWindowWidth() / scaleFactor, 100, ImGuiInputTextFlags.None);

                                if (ImGui.button("Get UUIDs")) {
                                    Dev.LOGGER.info("Getting UUIDs for: " + usernames.get());

                                    // Process the input usernames
                                    String[] usernameArray = usernames.get().split(",");
                                    JsonArray jsonArray = new JsonArray();
                                    for (String username : usernameArray) {
                                        jsonArray.add(username.trim());
                                    }

                                    // Convert JsonArray to JSON string
                                    Gson gson = new Gson();
                                    String jsonPayload = gson.toJson(jsonArray);

                                    ApiUtils.POST("https://api.minecraftservices.com/minecraft/profile/lookup/bulk/byname", jsonPayload, null, (response) -> {
                                        reqResult = response;
                                        partOfInterest = null;
                                    });
                                }
                                ImGui.endTabItem();
                            }

                            // username availability
                            if (ImGui.beginTabItem("Name availability")) {
                                ImGui.text("Technical details: ");
                                ImGui.sameLine();
                                documentation("https://wiki.vg/Mojang_API#Name_Availability");

                                ImGui.text("GET https://api.minecraftservices.com/minecraft/profile/name/<name>/available");
                                ImGui.text("Authorization: Bearer <access token>");
                                ImGui.separator();

                                // Input text box to check username availability
                                ImGui.pushItemWidth(ImGui.getWindowWidth() / scaleFactor);
                                ImGui.inputText("##username", username);
                                ImGui.sameLine();
                                ImGui.text("Desired Username");
                                ImGui.popItemWidth();

                                // Input text box for Access Token
                                ImGui.pushItemWidth(ImGui.getWindowWidth() / scaleFactor);
                                ImGui.inputText("##accessToken", accessToken);
                                ImGui.sameLine();
                                ImGui.text("Access Token");
                                ImGui.popItemWidth();

                                String cleanedToken = accessToken.get().replaceAll("\\s+", "");


                                if (ImGui.button("Check Username Availability")) {
                                    Dev.LOGGER.info("Checking availability for: " + username.get());
                                    ApiUtils.GET("https://api.minecraftservices.com/minecraft/profile/name/" + username.get() + "/available", cleanedToken, (response) -> {
                                        reqResult = response;
                                        partOfInterest = "status";
                                    });
                                }
                                ImGui.endTabItem();
                            }
                            //Profile Name Change Information
                            if (ImGui.beginTabItem("Name Change Information")) {
                                ImGui.text("Technical details: ");
                                ImGui.sameLine();
                                documentation("https://wiki.vg/Mojang_API#Name_Change_Information");

                                ImGui.text("GET https://api.minecraftservices.com/minecraft/profile/namechange");
                                ImGui.text("Authorization: Bearer <access token>");
                                ImGui.separator();

                                // Input text box for Access Token
                                ImGui.pushItemWidth(ImGui.getWindowWidth() / scaleFactor);
                                ImGui.inputText("##accessToken", accessToken);
                                ImGui.sameLine();
                                ImGui.text("Access Token");
                                ImGui.popItemWidth();

                                String cleanedToken = accessToken.get().replaceAll("\\s+", "");

                                if (ImGui.button("Get Name Change Information")) {
                                    Dev.LOGGER.info("Getting name change information");
                                    ApiUtils.GET("https://api.minecraftservices.com/minecraft/profile/namechange", cleanedToken, (response) -> {
                                        reqResult = response;
                                        partOfInterest = null;
                                    });
                                }
                                ImGui.endTabItem();
                            }
                            // Change name
                            if (ImGui.beginTabItem("Change Name")) {
                                ImGui.text("Technical details: ");
                                ImGui.sameLine();
                                documentation("https://wiki.vg/Mojang_API#Change_Name");

                                ImGui.text("PUT https://api.minecraftservices.com/minecraft/profile/name/<name>");
                                ImGui.text("Authorization: Bearer <access token>");

                                ImGui.spacing();
                                ImGui.spacing();

                                // Warning
                                ImGui.textColored(1.0f, 0.0f, 0.0f, 1.0f, "WARNING:");
                                ImGui.sameLine();
                                ImGui.textWrapped("You can only change your name every 30 days, first, get name change information to make sure you are eligible to change your name.");
                                ImGui.separator();

                                // Input text box to set username
                                ImGui.pushItemWidth(ImGui.getWindowWidth() / scaleFactor);
                                ImGui.inputText("##username", username);
                                ImGui.popItemWidth();
                                ImGui.sameLine();
                                ImGui.text("Desired Username");

                                // Input text box for Access Token
                                ImGui.pushItemWidth(ImGui.getWindowWidth() / scaleFactor);
                                ImGui.inputText("##accessToken", accessToken);
                                ImGui.sameLine();
                                ImGui.text("Access Token");
                                ImGui.popItemWidth();

                                String cleanedToken = accessToken.get().replaceAll("\\s+", "");

                                if (ImGui.button("Change Name")) {
                                    Dev.LOGGER.info("Changing name to: " + username.get());
                                    ApiUtils.PUT("https://api.minecraftservices.com/minecraft/profile/name/" + username.get(), cleanedToken, (response) -> {
                                        reqResult = response;
                                        partOfInterest = null;
                                    });
                                }
                                ImGui.endTabItem();
                            }
                            ImGui.endTabBar();
                        }


                    // Output text in text area
                    showResult(partOfInterest);
                    ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("Skin API")) {
                if (ImGui.beginTabBar("Skin APIs")) {
                    // Change skin
                    if (ImGui.beginTabItem("Change Skin")) {
                        ImGui.text("Technical details: ");
                        ImGui.sameLine();
                        documentation("https://wiki.vg/Mojang_API#Change_Skin");

                        ImGui.text("POST https://api.minecraftservices.com/minecraft/profile/skins");
                        ImGui.text("Authorization: Bearer <access token>");
                        ImGui.textWrapped("Payload: JSON object, containing variant, which is either \"classic\" or \"slim\", and url, the URL of the skin.");
                        ImGui.spacing();

                        ImGui.separator();

                        // Input text box for Access Token
                        ImGui.pushItemWidth(ImGui.getWindowWidth() / scaleFactor);
                        ImGui.inputText("##accessToken", accessToken);
                        ImGui.sameLine();
                        ImGui.text("Access Token");
                        ImGui.popItemWidth();

                        // Input text box for skin URL
                        ImGui.pushItemWidth(ImGui.getWindowWidth() / scaleFactor);
                        ImGui.inputText("##skinUrl", skinUrl);
                        ImGui.sameLine();
                        ImGui.text("Skin URL");
                        ImGui.popItemWidth();

                        // Dropdown menu for skin type
                        ImGui.pushItemWidth(ImGui.getWindowWidth() / scaleFactor);
                        if (ImGui.combo("##hiddenLabel", selectedSkinType, skinTypes)) {
                            switch (selectedSkinType.get()) {
                                case 0:
                                    // Classic
                                    skinType = "classic";
                                    break;
                                case 1:
                                    // Slim
                                    skinType = "slim";
                                    break;
                            }
                        }
                        ImGui.popItemWidth();

                        String cleanedToken = accessToken.get().replaceAll("\\s+", "");

                        if (ImGui.button("Change Skin")) {
                            Dev.LOGGER.info("Changing skin to: " + skinUrl.get());
                            Dev.LOGGER.info("Skin type: " + skinType);

                            // Constructing the payload using Gson
                            Gson gson = new Gson();
                            String jsonPayload = gson.toJson(new SkinPayload(skinType, skinUrl.get()));

                            Dev.LOGGER.info("Payload: " + jsonPayload);

                            ApiUtils.POST("https://api.minecraftservices.com/minecraft/profile/skins", jsonPayload, cleanedToken, (response) -> {
                                reqResult = response;
                                partOfInterest = null;
                            });
                        }
                        ImGui.endTabItem();
                    }
                    showResult(partOfInterest);
                    ImGui.endTabItem();
                }
                ImGui.endTabBar();
            }
            if (ImGui.beginTabItem("Cape API")) {
                ImGui.text("Cape API placeholder");
                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("OAuth 2")) {
                ImGui.text("Microsoft Authentication Scheme Debugger");
                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("Account Manager")) {
                ImGui.text("Account Manager placeholder");
                ImGui.endTabItem();
            }
        ImGui.endTabBar();



    }

    public static void documentation(String url) {
        ImGui.textColored(0.0f, 0.5f, 1.0f, 1.0f, docSt);
        if (ImGui.isItemClicked()) {
            UrlUtils.openUrl(url);
        }
        if (ImGui.isItemHovered()) {
            ImGui.setMouseCursor(ImGuiMouseCursor.Hand);
            // underline text on hover
            docSt = url;
        } else {
            docSt = "Documentation";
        }
    }

    public static void showResult(String PartOfInterest) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Object jsonObject = gson.fromJson(reqResult, Object.class);
            String indentedJson = gson.toJson(jsonObject);
            ImGui.separator();
            ImGui.inputTextMultiline("##apiResult", new ImString(indentedJson), ImGui.getWindowWidth() / scaleFactor, 100, ImGuiInputTextFlags.ReadOnly);
            ImGui.spacing();
            if (PartOfInterest != null && !PartOfInterest.isEmpty()) displayPartOfInterest(reqResult, PartOfInterest);
            ImGui.separator();

        } catch (Exception e) {
            ImGui.inputTextMultiline("##apiResult", new ImString(reqResult), ImGui.getWindowWidth() / scaleFactor, 100, ImGuiInputTextFlags.ReadOnly);
        }
    }

    private static void displayPartOfInterest(String response, String fieldName) {
        try {
            JsonElement jsonElement = JsonParser.parseString(response);

            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                if (jsonObject.has(fieldName)) {
                    String fieldValue = jsonObject.get(fieldName).getAsString();

                    ImGui.text(fieldName + ": " + fieldValue);
                    ImGui.sameLine();
                    if (ImGui.button("Copy to Clipboard")) {
                        ImGui.setClipboardText(fieldValue);
                        Dev.LOGGER.info("Copied to clipboard: " + fieldValue);
                    }
                } else {
                    Dev.LOGGER.error("Field not found: " + fieldName);
                }
            } else {
                Dev.LOGGER.error("Invalid JSON format. Expected a JSON object.");
            }
        } catch (Exception e) {
            Dev.LOGGER.error("Failed to parse JSON response: " + e.getMessage());
        }
    }
}
