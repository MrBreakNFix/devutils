package com.mrbreaknfix.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.Optional;

import static com.mrbreaknfix.Dev.LOGGER;
import static com.mrbreaknfix.Dev.mc;

public class ImageUtils {
    public static int[] imageFromPath(String path) {
        Identifier textureIdentifier = new Identifier("devutils", path);
        Optional<Resource> resource;

        resource = mc.getResourceManager().getResource(textureIdentifier);

        try (NativeImage nativeImage = NativeImage.read(resource.get().getInputStream())) {
            int width = nativeImage.getWidth();
            int height = nativeImage.getHeight();

            NativeImageBackedTexture texture = new NativeImageBackedTexture(nativeImage);
            int textureId = mc.getTextureManager().getTexture(new Identifier("devutils", path)).getGlId();

            RenderSystem.bindTexture(textureId);


            return new int[]{textureId, width, height};
        } catch (IOException e) {
            LOGGER.error("Failed to read image from resource: " + path, e);
            return new int[]{0, 0, 0};
        }
    }
    public static boolean fileExists(String path) {
        Identifier textureIdentifier = new Identifier("devutils", path);
        Optional<Resource> resource;

        resource = mc.getResourceManager().getResource(textureIdentifier);
        return resource.isPresent();
    }
}