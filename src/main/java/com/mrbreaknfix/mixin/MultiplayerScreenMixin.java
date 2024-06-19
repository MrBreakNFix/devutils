package com.mrbreaknfix.mixin;

import com.mrbreaknfix.gui.DevWindow;
import com.mrbreaknfix.gui.Drawer;
import imgui.ImGui;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin  {
    @Inject(at = @At("RETURN"), method = "render") // It matters weather you inject this at the start or end of the method, as it will draw under the rest of the screen.
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {


        Drawer.draw(DearImGuiYouSuck -> {
            ImGui.pushFont(Drawer.defaultFont);

            DevWindow.forceRenderSpecificWindow("Account & API");

            ImGui.popFont();
        });
    }
}