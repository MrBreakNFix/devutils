package com.mrbreaknfix.gui.windows;

import com.mrbreaknfix.gui.DevWindow;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiWindowFlags;

import java.util.Arrays;
import java.util.function.Function;

import static com.mrbreaknfix.Dev.tailLogFile;

public class LogWindow extends DevWindow {
    private boolean trimBoilerplate = false;

    @Override
    public String getTitle() {
        return "Log";
    }

    @Override
    protected void renderMenuItems() {
        if (ImGui.menuItem("Trim Boilerplate", "", trimBoilerplate)) {
            trimBoilerplate = !trimBoilerplate;
        }
    }

    @Override
    protected void renderContent() {
        ImGui.begin("Log", ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoTitleBar);

        String[] lines = tailLogFile(10);

        if (trimBoilerplate) {
            lines = Arrays.stream(lines)
                    .map(trimBeforeFirstParenthesis())
                    .toArray(String[]::new);
        }

        for (String line : lines) {
            if (line.contains("ERROR")) {
                ImGui.pushStyleColor(ImGuiCol.Text, 1.0f, 0.0f, 0.0f, 1.0f);
            } else if (line.contains("WARN")) {
                ImGui.pushStyleColor(ImGuiCol.Text, 1.0f, 1.0f, 0.0f, 1.0f);
            } else if (line.contains("DevUtils")) {
                ImGui.pushStyleColor(ImGuiCol.Text, 0.0f, 0.0f, 1.0f, 1.0f);
            } else {
                ImGui.pushStyleColor(ImGuiCol.Text, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            ImGui.text(line);
            ImGui.popStyleColor();
        }

        ImGui.end();
    }

    private Function<String, String> trimBeforeFirstParenthesis() {
        return line -> {
            int index = line.indexOf(')');
            return index != -1 ? line.substring(index + 1).trim() : line;
        };
    }
}
