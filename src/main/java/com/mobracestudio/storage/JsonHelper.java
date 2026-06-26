package com.mobracestudio.storage;

import com.google.gson.*;
import com.mobracestudio.MobRaceStudio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonHelper {
    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .create();

    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static <T> T fromJson(String json, java.lang.reflect.Type type) {
        return GSON.fromJson(json, type);
    }

    public static boolean saveToFile(Path path, Object obj) {
        try {
            Files.createDirectories(path.getParent());
            String json = GSON.toJson(obj);
            Files.writeString(path, json, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            MobRaceStudio.LOGGER.error("Failed to save JSON to: {}", path, e);
            return false;
        }
    }

    public static <T> T loadFromFile(Path path, Class<T> clazz) {
        try {
            if (Files.exists(path)) {
                String json = Files.readString(path, StandardCharsets.UTF_8);
                return GSON.fromJson(json, clazz);
            }
        } catch (IOException e) {
            MobRaceStudio.LOGGER.error("Failed to load JSON from: {}", path, e);
        }
        return null;
    }

    public static JsonObject parse(String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }

    public static JsonElement toJsonTree(Object obj) {
        return GSON.toJsonTree(obj);
    }
}
