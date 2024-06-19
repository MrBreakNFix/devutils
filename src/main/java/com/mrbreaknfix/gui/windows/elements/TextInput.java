package com.mrbreaknfix.gui.windows.elements;

import imgui.ImGui;
import imgui.type.ImString;

import java.util.ArrayList;
import java.util.List;

public class TextInput {
    private static final List<TextInput> activeTextInputs = new ArrayList<>();

    private final String label;
    private final ImString text;
    private boolean isActive = false;

    public TextInput(String label, ImString text) {
        this.label = label;
        this.text = text;
        activeTextInputs.add(this);
        this.create();
    }

    public void create() {
        ImGui.inputText(label, text);
        isActive = ImGui.isItemActive();
    }


    public boolean isActive() {
        return isActive;
    }

    public static boolean isAnyTextInputFocused() {

        for (TextInput input : activeTextInputs) {
            if (input.isActive()) {
                return true;
            }
        }
        return false;
    }

    public static void unFocus() {
        for (TextInput input : activeTextInputs) {
            input.isActive = false;
        }
    }
}