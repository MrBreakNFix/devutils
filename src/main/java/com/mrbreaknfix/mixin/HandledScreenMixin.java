package com.mrbreaknfix.mixin;

import com.mrbreaknfix.Dev;
import com.mrbreaknfix.event.events.ScreenEvent;
import com.mrbreaknfix.gui.DevWindow;
import com.mrbreaknfix.gui.windows.custom.InfoWindow;
import com.mrbreaknfix.gui.Drawer;
import com.mrbreaknfix.gui.windows.elements.TextInput;
import com.mrbreaknfix.utils.DrawUtils;
import imgui.ImGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.mrbreaknfix.Dev.EventBus;
import static com.mrbreaknfix.Dev.mc;


@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin extends Screen {

    @Shadow @Nullable protected Slot focusedSlot;
    @Unique
    protected boolean isFocused;
    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();

        Drawer.draw(DearImGuiYouSuck -> {
            ImGui.pushFont(Drawer.defaultFont);
            assert mc.player != null;

            // Render normal windows
            DevWindow.renderVisibleWindows();

            // Custom dev window
            InfoWindow.render();

            ImGui.popFont();
            Dev.currentSlot = focusedSlot;
        });

    }

    @Inject(method = "keyPressed", at=@At("HEAD"), cancellable = true)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {


        if (keyCode == mc.options.inventoryKey.getDefaultKey().getCode()) {
            isFocused = TextInput.isAnyTextInputFocused();
            if (isFocused) {
                TextInput.unFocus();
                cir.setReturnValue(false);
            }
        }
        TextInput.unFocus();
    }

    @Inject(method = "onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V", at = @At("RETURN"))
    private void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo ci) {
        if (slot != null) {
            Dev.lastClickedSlot = slot;
        }
        TextInput.unFocus();
    }

    // Slot rendering
    @Inject(method = "drawSlot", at = @At("RETURN"))
    private void drawSlot(DrawContext context, Slot slot, CallbackInfo ci) {
        int slotSize = 32;
        int darkblue = 0xFF0000FF;
        int red = 0x50FF0000;
        int green = 0x5000FF00;
        int yellow = 0x50FFFF00;
        int arrowColor = 0x06FFFF00;

        Text text = Text.of(String.valueOf(slot.id));
        int textWidth = textRenderer.getWidth(text);

        // take the slot size and divide it by 2, then subtract the text width divided by 2, this will center the text
        int textX = slot.x + slotSize / 4 - textWidth / 2;
        int textY = slot.y + slotSize / 4 - 4;

        // Add the slot to the slotPositions map
        if (!Dev.slotPositions.containsKey(slot.id)) {
            int centerSlotX = slot.x + slotSize / 4;
            int centerSlotY = slot.y + slotSize / 4;

            Dev.slotPositions.put(slot.id, new int[]{centerSlotX, centerSlotY});
//            dev.slotPositions.put(slot.id, new int[]{slot.x + slotSize / 2, slot.y + slotSize / 2});
        }



        if (Dev.showSelectedSlot) {
            if (Dev.selectedSlot == slot.id) {
                context.fill(slot.x, slot.y, slot.x + slotSize / 2, slot.y + slotSize / 2, green);
                context.drawText(textRenderer, text, textX, textY, darkblue, false);
            }
        }

        if (Dev.showSlotIDs) {
            context.drawText(textRenderer, text, textX, textY, darkblue, false);
        }

        if (Dev.highlightLastClickedSlotID) {
            if (slot == Dev.lastClickedSlot && slot.id != Dev.selectedSlot) {
                context.fill(slot.x, slot.y, slot.x + slotSize / 2, slot.y + slotSize / 2, yellow);
                context.drawText(textRenderer, text, textX, textY, darkblue, false);
            }
        }



        if (slot == Dev.currentSlot) {
            //todo: visualize send packet
            // todo: create a isSelecting boolean, allowing the user to have a dedicated selection gui

            if (Dev.highlightSlotIDsOnHover) {
                if (Dev.highlightLastClickedSlotID && slot == Dev.lastClickedSlot && slot.id != Dev.selectedSlot) {
                    context.fill(slot.x, slot.y, slot.x + slotSize / 2, slot.y + slotSize / 2, yellow);
                    context.drawText(textRenderer, text, textX, textY, darkblue, false);
                } else if (slot.id != Dev.selectedSlot) {
                    context.fill(slot.x, slot.y, slot.x + slotSize / 2, slot.y + slotSize / 2, red);
                    context.drawText(textRenderer, text, textX, textY, darkblue, false);
                }
            }
        }

        // Draw an arrow from the selectedSlot to the last clicked slot
        if (Dev.slotPositions != null) {
            if (Dev.lastClickedSlot != null) {
                int[] selectedSlotPos = Dev.slotPositions.get(Dev.selectedSlot);
                int[] lastClickedSlotPos = Dev.slotPositions.get(Dev.lastClickedSlot.id);
                if (selectedSlotPos != null && lastClickedSlotPos != null) {
//                    DrawUtils.drawArrowWithThickness(context.getMatrices(), selectedSlotPos[0], selectedSlotPos[1], lastClickedSlotPos[0], lastClickedSlotPos[1], 0, green, arrowColor,4, 10);
//                    DrawUtils.lineWithThickness(context.getMatrices(), selectedSlotPos[0], selectedSlotPos[1], lastClickedSlotPos[0], lastClickedSlotPos[1], 0, arrowColor, 4);
                    DrawUtils.drawArrowWithThickness(context.getMatrices(), selectedSlotPos[0], selectedSlotPos[1], lastClickedSlotPos[0], lastClickedSlotPos[1], 0, green, arrowColor, 4, 10);

                }
            }
        }
    }

    @Inject(method = "close", at = @At("HEAD"))
    private void close(CallbackInfo ci) {
        Dev.slotPositions.clear();
        EventBus.trigger(new ScreenEvent.Close());
    }
}
