package com.mrbreaknfix.scripting;


import net.fabricmc.loader.api.FabricLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceHelper {
    public static final boolean isProduction = !FabricLoader.getInstance().isDevelopmentEnvironment();

    public static InputStream get(String path) {
        return isProduction ? loadFromResource(path) : loadFromFile(path);
    }

    private static InputStream loadFromResource(String filename) {
        ClassLoader classLoader = ResourceHelper.class.getClassLoader();
        return classLoader.getResourceAsStream(filename);
    }

    private static InputStream loadFromFile(String filename) {
        try {
            Path path = Paths.get(System.getProperty("user.dir"), "../src/main/resources", filename);
            return new FileInputStream(path.toString());
        }
        catch (IOException e) {
            return null;
        }
    }
}