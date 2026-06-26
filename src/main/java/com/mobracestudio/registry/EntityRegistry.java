package com.mobracestudio.registry;

import com.mobracestudio.MobRaceStudio;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class EntityRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(MobRaceStudio.MOD_ID);
    private static final Map<Identifier, EntityType<?>> ALL_ENTITIES = new LinkedHashMap<>();
    private static final Map<String, List<EntityType<?>>> ENTITIES_BY_MOD = new TreeMap<>();
    private static final Map<Identifier, EntityCategory> ENTITY_CATEGORIES = new HashMap<>();
    private static boolean discovered = false;

    public static void discoverAllEntities() {
        if (discovered) return;
        discovered = true;

        ALL_ENTITIES.clear();
        ENTITIES_BY_MOD.clear();
        ENTITY_CATEGORIES.clear();

        Registries.ENTITY_TYPE.forEach(entityType -> {
            Identifier id = Registries.ENTITY_TYPE.getId(entityType);
            if (id == null) return;

            ALL_ENTITIES.put(id, entityType);

            String modId = id.getNamespace();
            ENTITIES_BY_MOD.computeIfAbsent(modId, k -> new ArrayList<>()).add(entityType);

            EntityCategory category = categorizeEntity(entityType, id);
            ENTITY_CATEGORIES.put(id, category);
        });

        LOGGER.info("Discovered {} entity types across {} mods",
            ALL_ENTITIES.size(), ENTITIES_BY_MOD.size());

        if (LOGGER.isDebugEnabled()) {
            ENTITIES_BY_MOD.forEach((mod, entities) ->
                LOGGER.debug("  Mod '{}': {} entities", mod, entities.size()));
        }
    }

    private static EntityCategory categorizeEntity(EntityType<?> type, Identifier id) {
        if (type.getSpawnGroup() != null) {
            return switch (type.getSpawnGroup()) {
                case MONSTER -> EntityCategory.HOSTILE;
                case CREATURE, AMBIENT, WATER_AMBIENT, UNDERGROUND_WATER_CREATURE,
                     WATER_CREATURE, AXOLOTLS -> EntityCategory.PASSIVE;
                case MISC -> EntityCategory.OTHER;
            };
        }
        if (LivingEntity.class.isAssignableFrom(type.getBaseClass())) {
            return EntityCategory.HOSTILE;
        }
        return EntityCategory.OTHER;
    }

    public static Collection<EntityType<?>> getAllEntities() {
        return Collections.unmodifiableCollection(ALL_ENTITIES.values());
    }

    public static Collection<EntityType<?>> getEntitiesByMod(String modId) {
        return ENTITIES_BY_MOD.getOrDefault(modId, Collections.emptyList());
    }

    public static Set<String> getAllModIds() {
        return ENTITIES_BY_MOD.keySet();
    }

    public static EntityType<?> getEntity(Identifier id) {
        return ALL_ENTITIES.get(id);
    }

    public static EntityCategory getCategory(Identifier id) {
        return ENTITY_CATEGORIES.getOrDefault(id, EntityCategory.OTHER);
    }

    public static EntityCategory getCategory(EntityType<?> type) {
        Identifier id = Registries.ENTITY_TYPE.getId(type);
        return id != null ? getCategory(id) : EntityCategory.OTHER;
    }

    public static int getTotalEntityCount() {
        return ALL_ENTITIES.size();
    }

    public static Identifier getEntityTypeId(EntityType<?> type) {
        return Registries.ENTITY_TYPE.getId(type);
    }

    public static int getModCount() {
        return ENTITIES_BY_MOD.size();
    }

    public static boolean isDiscovered() {
        return discovered;
    }

    public enum EntityCategory {
        HOSTILE,
        PASSIVE,
        BOSS,
        VILLAGER,
        OTHER
    }
}
