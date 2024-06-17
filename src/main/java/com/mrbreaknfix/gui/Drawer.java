package com.mrbreaknfix.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mrbreaknfix.Dev;
import imgui.*;
import imgui.extension.implot.ImPlot;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.apache.commons.io.IOUtils;
import org.lwjgl.glfw.GLFW;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Drawer {
    private final static ImGuiImplGlfw imGuiImplGlfw = new ImGuiImplGlfw();
    private final static ImGuiImplGl3 imGuiImplGl3 = new ImGuiImplGl3();
    public static ImFont defaultFont;
    private static final float ALPHA_DEFAULT = 0.85f;
    private static final float[] windowBgColorDefault = {0.0f, 0.0f, 0.29411766f, 0.78431374f};
    private static final float[] textColorDefault = {1.0f, 1.0f, 1.0f, 1.0f};
    public static float ALPHA = ALPHA_DEFAULT;
    public static float[] windowBgColor = windowBgColorDefault.clone();
    public static float[] textColor = textColorDefault.clone();
    private static final String styleFile = "devutils-styles.json";

    public static void create(final long handle) throws IOException {
        ImGui.createContext();
        ImPlot.createContext();

        final ImGuiIO io = ImGui.getIO();
        io.setIniFilename("devutils.ini");

        // Font configuration
        final ImFontAtlas fonts = io.getFonts();
        final ImFontGlyphRangesBuilder rangesBuilder = new ImFontGlyphRangesBuilder();

        rangesBuilder.addRanges(io.getFonts().getGlyphRangesDefault());

        final short[] glyphRanges = rangesBuilder.buildRanges();

        final ImFontConfig fontConfig = new ImFontConfig();
        fontConfig.setGlyphRanges(glyphRanges);

        final String fontPath = "/fonts/JetBrainsMonoNerdFont-Regular.ttf";
        InputStream inputStream = Drawer.class.getResourceAsStream(fontPath);
        assert inputStream != null;
        byte[] fontData = IOUtils.toByteArray(inputStream);

        List<ImFont> generatedFonts = IntStream.rangeClosed(5, 50)
                .mapToObj(i -> {
                    fontConfig.setName("JetBrains Mono " + i + "px");
                    return fonts.addFontFromMemoryTTF(fontData, i, fontConfig, glyphRanges);
                }).toList();

        defaultFont = generatedFonts.get(11); // Font scale is 11
        fonts.build();

        // Style settings
        ImGuiStyle style = ImGui.getStyle();
        style.setWindowRounding(0);
        style.setTabRounding(0);
        style.setTabBorderSize(0);
        style.setFrameRounding(0);
        style.setScrollbarRounding(0);
        style.setScrollbarSize(10);

        // Set colors
        setStyleColor(ImGuiCol.Text, textColor);
        setStyleColor(ImGuiCol.TextDisabled, 0.0f, 0.6f, 1.0f, 0.5f);
        setStyleColor(ImGuiCol.WindowBg, windowBgColor);
        setStyleColor(ImGuiCol.FrameBg, 0.0f, 0.0f, 0.0f, 0.8f);
        setStyleColor(ImGuiCol.ResizeGrip, 0.0f, 0.0f, 0.0f, 0.0f);
        setStyleColor(ImGuiCol.ResizeGripHovered, 0.0f, 0.0f, 1f, 0.8f);

        // Set alpha
        style.setAlpha(ALPHA);

        io.getFonts().addFontDefault();
        io.getFonts().build();


        io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
        imGuiImplGlfw.init(handle, true);
        imGuiImplGl3.init();
    }

    public static void draw(final Renderer runnable) {
        imGuiImplGlfw.newFrame();
        ImGui.newFrame();

        // Push the default font before rendering
        ImGui.pushFont(defaultFont);

        runnable.render(ImGui.getIO());

        // Pop the font after rendering
        ImGui.popFont();

        ImGui.render();
        imGuiImplGl3.renderDrawData(ImGui.getDrawData());
        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupContext = GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupContext);
        }
    }


    public static void saveStyles() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Map<String, float[]> styles = new HashMap<>();
            styles.put("windowBgColor", windowBgColor);
            styles.put("textColor", textColor);

            try (Writer writer = new FileWriter(styleFile)) {
                gson.toJson(styles, writer);
            }
        } catch (IOException e) {
            Dev.LOGGER.error("Error saving style: " + e);
        }
    }

    public static void loadStyles() {
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, float[]>>() {}.getType();

            try (Reader reader = new FileReader(styleFile)) {
                Map<String, float[]> styles = gson.fromJson(reader, type);
                if (styles != null) {
                    windowBgColor = styles.getOrDefault("windowBgColor", windowBgColorDefault.clone());
                    textColor = styles.getOrDefault("textColor", textColorDefault.clone());
                }
            }
        } catch (IOException e) {
            Dev.LOGGER.error("Error loading style: " + e);
        }
    }

    public static void applyStyle() {
        ImGuiStyle style = ImGui.getStyle();
        setStyleColor(ImGuiCol.WindowBg, windowBgColor);
        setStyleColor(ImGuiCol.Text, textColor);
        style.setAlpha(ALPHA);
    }

    public static void defaultStyle() {
        ALPHA = ALPHA_DEFAULT;
        windowBgColor = windowBgColorDefault.clone();
        textColor = textColorDefault.clone();
    }

    private static void setStyleColor(int col, float[] color) {
        ImGui.getStyle().setColor(col, color[0], color[1], color[2], color[3]);
    }

    private static void setStyleColor(int col, float r, float g, float b, float a) {
        ImGui.getStyle().setColor(col, r, g, b, a);
    }
}
