package com.mrbreaknfix.utils;

import com.mrbreaknfix.Dev;
import com.mrbreaknfix.gui.windows.custom.MultiplayerScreenWindow;

import java.util.UUID;

public class AuthUtils {
    public static UUID cleanUUID(String uuid) {
        try {
            if (uuid.length() == 32) {
                return UUID.fromString(uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32));
            } else if (uuid.length() == 36) {
                return UUID.fromString(uuid);
            }
        } catch (Exception e) {
            Dev.LOGGER.error("Failed to clean UUID: " + uuid);
        }
        MultiplayerScreenWindow.reqResult = "Failed to clean UUID: " + uuid;
        return UUID.fromString("00000000-0000-0000-0000-000000000000");
    }
}
