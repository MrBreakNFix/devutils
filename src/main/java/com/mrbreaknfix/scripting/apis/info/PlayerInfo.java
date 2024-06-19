package com.mrbreaknfix.scripting.apis.info;

import com.mrbreaknfix.scripting.ApiBase;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

import static com.mrbreaknfix.Dev.mc;

public class PlayerInfo extends ApiBase {

    @Override
    public String getRoute() {
        return "playerinfo";
    }

    @Override
    public String post(Map<String, String> fields) {
        String request = fields.get("wants");
        if (request == null || mc.player == null) {
            return null;
        }

        return switch (request) {
            case "x" -> String.valueOf(mc.player.getX());
            case "y" -> String.valueOf(mc.player.getY());
            case "z" -> String.valueOf(mc.player.getZ());

            case "health" -> String.valueOf(mc.player.getHealth());
            case "dimension" -> mc.player.getEntityWorld().getRegistryKey().getValue().toString();

            case "targetBlockPosX" -> String.valueOf(mc.crosshairTarget.getPos().getX());
            case "targetBlockPosY" -> String.valueOf(mc.crosshairTarget.getPos().getY());
            case "targetBlockPosZ" -> String.valueOf(mc.crosshairTarget.getPos().getZ());

            case "world" -> mc.player.getWorld().getRegistryKey().getValue().toString();
            case "biome" -> mc.player.getEntityWorld().getBiome(mc.player.getBlockPos()).toString();
            case "getBlockAt" -> {
                // Check for X, Y, and Z fields
                String x = fields.get("x");
                String y = fields.get("y");
                String z = fields.get("z");

                if (x == null || y == null || z == null) {
                    yield null;
                } else {
                    // return the block type, ex minecraft:stone
                    yield mc.world.getBlockState(new BlockPos(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z))).getBlock().getTranslationKey();
                }
            }

            default -> null;
        };
    }
}