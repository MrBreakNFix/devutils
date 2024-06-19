package com.mrbreaknfix.gui;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mrbreaknfix.Dev;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class DevWindow {
    private static final HashMap<String, WindowState> windowStates = new HashMap<>();
    private static final List<DevWindow> windows = new ArrayList<>();
    private final String title;
    private static final String statesFile = "devutils-guistate.json";

    public DevWindow() {
        this.title = getTitle();
        windowStates.putIfAbsent(title, new WindowState());
//        windows.add(this);
    }

    public static void registerWindow(DevWindow window) {
        windows.add(window);
    }

    public final void render(boolean force) {
        WindowState state = windowStates.get(title);

        if (state.isDisabled() && !force) {
            return;
        }

        int windowFlags = getWindowFlags();
        if (state.isPinned()) {
            windowFlags |= ImGuiWindowFlags.NoMove;
        }

        ImGui.begin(title, windowFlags);

        if (ImGui.beginMenuBar()) {
            if (ImGui.beginMenu(getTitle())) {
                if (ImGui.menuItem("Hide", "", state.isDisabled())) {
                    state.setDisabled(!state.isDisabled());
                }
                if (ImGui.menuItem("Pin", "", state.isPinned())) {
                    state.setPinned(!state.isPinned());
                }
                renderMenuItems();
                ImGui.endMenu();
            }
            ImGui.endMenuBar();
        }

//        ImGui.separator();
        renderContent();
        ImGui.end();
    }

    public abstract String getTitle();

    protected int getWindowFlags() {
        return ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoTitleBar;
    }

    protected void renderMenuItems() {
        // Override this method to add menu items to the window, see SaveLoadScreenWindow.java for an example
    }

    // Override this method to add content to the window
    protected abstract void renderContent();

    public void makeVisible() {
        // todo: rename make visible to enabled/disabled
        windowStates.get(title).setDisabled(false);
    }

    public boolean isVisible() {
        return !windowStates.get(title).isDisabled();
    }

    public boolean isPinned() {
        if (windowStates.get(title) == null) {
            // prevent update crash :)
            // this is messy code :I
            windowStates.put(title, new WindowState());
            saveWindowState();
            return false;
        }
        return windowStates.get(title).isPinned();
    }

    //todo: fix crash on update/add more windows
    // preform null check and notify the user that they either need to remove the plugin, or continue with their previous settings being erased
    // or back them up export to file or clipboard
    public static void renderPinnedWindows() {
        for (DevWindow window : windows) {
            if (window.isPinned() && window.isVisible()) {
                window.render(false);
            }
        }
    }

    public static void renderVisibleWindows() {
        for (DevWindow window : windows) {
            if (window.isVisible()) {
                window.render(false);
            }
        }
    }

    public static void forceRenderSpecificWindow(String title) {
        for (DevWindow window : windows) {
            if (window.getTitle().equals(title)) {
                window.render(true);
            }
        }
    }

    // getHiddenWindows
    public static List<DevWindow> getHiddenWindows() {
        List<DevWindow> hiddenWindows = new ArrayList<>();
        for (DevWindow window : windows) {
            if (!window.isVisible()) {
                hiddenWindows.add(window);
            }
        }
        return hiddenWindows;
    }

    private static class WindowState {
        private boolean disabled = false;
        private boolean pinned = false;

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
            saveWindowState();
        }

        public boolean isPinned() {
            return pinned;
        }

        public void setPinned(boolean pinned) {
            this.pinned = pinned;
            saveWindowState();
        }
    }

    public static void saveWindowState() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (Writer writer = new FileWriter(statesFile)) {
                gson.toJson(windowStates, writer);
            }
        } catch (IOException e) {
            Dev.LOGGER.error("Error saving window states: " + e);
        }
    }

    public static void loadWindowState() {
        try {
            Gson gson = new Gson();
            File file = new File(statesFile);
            if (file.exists()) {
                try (Reader reader = new FileReader(statesFile)) {
                    Type type = new TypeToken<Map<String, WindowState>>(){}.getType();
                    windowStates.clear();
                    windowStates.putAll(gson.fromJson(reader, type));
                }
            }
        } catch (IOException e) {
            Dev.LOGGER.error("Error loading window states: " + e);
        }
    }

}
