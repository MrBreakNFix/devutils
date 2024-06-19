package com.mrbreaknfix.mixin;

import com.mrbreaknfix.Dev;
import com.mrbreaknfix.gui.Drawer;
import com.mrbreaknfix.gui.DevWindow;
import com.mrbreaknfix.gui.WindowManager;
import com.mrbreaknfix.utils.UrlUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.mrbreaknfix.Dev.mc;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Shadow
	@Final
	private Window window;

	@Inject(method = "<init>", at = @At("RETURN"))
	public void init(RunArgs args, CallbackInfo ci) throws Exception {
		Drawer.create(window.getHandle());
		WindowManager.registerWindows();
		DevWindow.loadWindowState();
		Drawer.loadStyles();
		Drawer.applyStyle();
    }
}