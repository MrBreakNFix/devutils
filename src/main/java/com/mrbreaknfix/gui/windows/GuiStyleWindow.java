package com.mrbreaknfix.gui.windows;

import com.mrbreaknfix.gui.DevWindow;
import com.mrbreaknfix.gui.Drawer;
import imgui.ImGui;

public class GuiStyleWindow extends DevWindow {

    @Override
    public String getTitle() {
        return "Customization";
    }

    @Override
    protected void renderContent() {
        // Transparency slider min 0.1f max 1.0f
        float[] alpha = {Drawer.ALPHA};
        ImGui.sliderFloat("Opacity", alpha, 0.1f, 1.0f);
        Drawer.ALPHA = alpha[0];

        // Window background color
        float[] windowBg = Drawer.windowBgColor;
        ImGui.colorEdit4("Window Background", windowBg);

        // Text color
        float[] text = Drawer.textColor;
        ImGui.colorEdit3("Text Color", text);


        // Save button
        if (ImGui.button("Save")) {
            Drawer.saveStyles();
            Drawer.applyStyle();
        }

        ImGui.sameLine();

        if (ImGui.button("Default Style")) {
            Drawer.defaultStyle();
            Drawer.saveStyles();
            Drawer.applyStyle();
        }

    }

}
