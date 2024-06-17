package com.mrbreaknfix.utils;

import com.mrbreaknfix.Dev;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class UrlUtils {
    public static void openUrl(String url) {
        Dev.LOGGER.info("Opening URL: " + url);
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI(url));
                }
            } catch (IOException | URISyntaxException e) {
                Dev.LOGGER.error("Failed to open URL: " + url);
            }
        } else {
            try {
                String os = System.getProperty("os.name").toLowerCase();
                Runtime runtime = Runtime.getRuntime();

                if (os.contains("win")) {
                    runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
                } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                    String[] browsers = {"xdg-open", "google-chrome", "firefox", "opera", "mozilla"};
                    String browser = null;

                    for (String b : browsers) {
                        if (runtime.exec(new String[] {"which", b}).waitFor() == 0) {
                            browser = b;
                            break;
                        }
                    }

                    if (browser != null) {
                        runtime.exec(new String[] {browser, url});
                    } else {
                        throw new Exception("No web browser found");
                    }
                } else {
                    throw new Exception("Unknown operating system: " + os);
                }
            } catch (Exception e) {
                Dev.LOGGER.error("Failed to open URL: " + url);
            }
        }
    }
}
