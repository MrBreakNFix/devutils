package com.mrbreaknfix.gui;

import com.mrbreaknfix.gui.windows.*;
import com.mrbreaknfix.gui.windows.custom.MultiplayerScreenWindow;

public class WindowManager {
    public static void registerWindows() {
        DevWindow.registerWindow(new SaveLoadScreenWindow());
        DevWindow.registerWindow(new CloseScreenWindow());
        DevWindow.registerWindow(new LogWindow());
        DevWindow.registerWindow(new GuiStyleWindow());
        DevWindow.registerWindow(new ServerInfoWindow());
        DevWindow.registerWindow(new PacketWindow());

        // todo ramio just hatest testing things
//        DevWindow.registerWindow(new TestWindow());

        // Custom windows
        DevWindow.registerWindow(new MultiplayerScreenWindow());
    }
}
