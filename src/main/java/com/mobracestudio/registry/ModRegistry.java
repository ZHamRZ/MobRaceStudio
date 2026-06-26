package com.mobracestudio.registry;

import com.mobracestudio.MobRaceStudio;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ModRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(MobRaceStudio.MOD_ID);
    private static final Map<String, ModInfo> MOD_INFO_MAP = new HashMap<>();
    private static boolean initialized = false;

    public static void initialize() {
        if (initialized) return;
        initialized = true;

        FabricLoader.getInstance().getAllMods().forEach(container -> {
            String id = container.getMetadata().getId();
            String name = container.getMetadata().getName();
            String description = container.getMetadata().getDescription();
            MOD_INFO_MAP.put(id, new ModInfo(id, name, description));
        });

        LOGGER.info("ModRegistry initialized with {} mods", MOD_INFO_MAP.size());
    }

    public static ModInfo getModInfo(String modId) {
        return MOD_INFO_MAP.get(modId);
    }

    public static String getModName(String modId) {
        ModInfo info = MOD_INFO_MAP.get(modId);
        return info != null ? info.name() : modId;
    }

    public static Collection<ModInfo> getAllMods() {
        return MOD_INFO_MAP.values();
    }

    public static boolean hasEntityMod(String modId) {
        return MOD_INFO_MAP.containsKey(modId)
            && !EntityRegistry.getEntitiesByMod(modId).isEmpty();
    }

    public record ModInfo(String id, String name, String description) {}
}
