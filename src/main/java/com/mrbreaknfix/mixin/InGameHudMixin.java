package com.mrbreaknfix.mixin;

import com.mrbreaknfix.gui.DevWindow;
import com.mrbreaknfix.gui.Drawer;
import imgui.ImGui;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.mrbreaknfix.Dev.mc;

@Mixin(InGameHud.class)
public class InGameHudMixin {


    @Inject(at = @At("HEAD"), method = "render")
    private void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (mc.currentScreen == null || mc.currentScreen instanceof ChatScreen || mc.currentScreen instanceof GameMenuScreen) {
            if (mc.player == null) {
                return;
            }

            Drawer.draw(DearImGuiYouSuck -> {
                ImGui.pushFont(Drawer.defaultFont);
                DevWindow.renderPinnedWindows();

//                DevWindow.renderSpecificWindow("Account & API");


                ImGui.popFont();
            });
        }
    }
}