package com.mrbreaknfix.gui.windows;

import com.mrbreaknfix.gui.DevWindow;
import imgui.ImGui;
import net.minecraft.util.Identifier;

import static com.mrbreaknfix.Dev.mc;

public class TestWindow extends DevWindow {
    @Override
    public String getTitle() {
        return "Test";
    }

    @Override
    protected void renderContent() {
        int textureId = mc.getTextureManager().getTexture(Identifier.of("devutils", "docs/container_chestdouble.png")).getGlId();

        // render the texture with ImGui image
        ImGui.image(textureId, 32, 32);

    }
}
