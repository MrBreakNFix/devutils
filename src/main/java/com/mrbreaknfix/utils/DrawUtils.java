package com.mrbreaknfix.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.ColorHelper;
import org.joml.Matrix4f;

public class DrawUtils {

    public static void lineWithThickness(MatrixStack matrices, float startX, float startY, float endX, float endY, int z, int startColor, int endColor, float thickness) {
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Matrix4f matrix = matrices.peek().getPositionMatrix();

        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        float sR = (float) ColorHelper.Argb.getRed(startColor) / 255.0F;
        float sG = (float) ColorHelper.Argb.getGreen(startColor) / 255.0F;
        float sB = (float) ColorHelper.Argb.getBlue(startColor) / 255.0F;

        float eR = (float) ColorHelper.Argb.getRed(endColor) / 255.0F;
        float eG = (float) ColorHelper.Argb.getGreen(endColor) / 255.0F;
        float eB = (float) ColorHelper.Argb.getBlue(endColor) / 255.0F;

        float alpha = 0.0105F;

        float dx = endX - startX;
        float dy = endY - startY;
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        dx /= length;
        dy /= length;

        float px = -dy * thickness / 2;
        float py = dx * thickness / 2;

        bufferBuilder.vertex(matrix, startX + px, startY + py, z).color(sR, sG, sB, alpha);
        bufferBuilder.vertex(matrix, endX + px, endY + py, z).color(eR, eG, eB, alpha);
        bufferBuilder.vertex(matrix, endX - px, endY - py, z).color(eR, eG, eB, alpha);
        bufferBuilder.vertex(matrix, startX - px, startY - py, z).color(sR, sG, sB, alpha);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

        RenderSystem.disableBlend();
    }

    public static void drawArrowWithThickness(MatrixStack matrices, float startX, float startY, float endX, float endY, int z, int startColor, int endColor, float thickness, float arrowHeadSize) {
        float dx = endX - startX;
        float dy = endY - startY;
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        dx /= length;
        dy /= length;

        float arrowStartX = endX - dx * arrowHeadSize;
        float arrowStartY = endY - dy * arrowHeadSize;

        lineWithThickness(matrices, startX, startY, arrowStartX, arrowStartY, z, startColor, endColor, thickness);

        float arrowHeadX1 = endX - dx * arrowHeadSize + dy * (arrowHeadSize / 2);
        float arrowHeadY1 = endY - dy * arrowHeadSize - dx * (arrowHeadSize / 2);
        float arrowHeadX2 = endX - dx * arrowHeadSize - dy * (arrowHeadSize / 2);
        float arrowHeadY2 = endY - dy * arrowHeadSize + dx * (arrowHeadSize / 2);

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Matrix4f matrix = matrices.peek().getPositionMatrix();

        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);

        float red = (float) ColorHelper.Argb.getRed(endColor) / 255.0F;
        float green = (float) ColorHelper.Argb.getGreen(endColor) / 255.0F;
        float blue = (float) ColorHelper.Argb.getBlue(endColor) / 255.0F;
        float alpha = 0.0105F;

        bufferBuilder.vertex(matrix, endX, endY, z).color(red, green, blue, alpha);
        bufferBuilder.vertex(matrix, arrowHeadX1, arrowHeadY1, z).color(red, green, blue, alpha);
        bufferBuilder.vertex(matrix, arrowHeadX2, arrowHeadY2, z).color(red, green, blue, alpha);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

        RenderSystem.disableBlend();
    }
}
