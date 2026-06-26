package com.mobracestudio.entity;

import com.mobracestudio.MobRaceStudio;
import com.mobracestudio.ai.SmartMovement;
import com.mobracestudio.arena.Arena;
import com.mobracestudio.arena.SpawnPoint;
import com.mobracestudio.config.MobConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public class RaceMobSpawner {
    private final Map<UUID, SmartMovement> activeMobs = new HashMap<>();
    private final Map<UUID, MobEntity> mobEntities = new HashMap<>();

    public List<MobEntity> spawnMobs(Arena arena, ServerWorld world, List<MobConfig> mobConfigs) {
        List<MobEntity> spawned = new ArrayList<>();
        List<SpawnPoint> spawnPoints = arena.getSpawnPoints();

        if (spawnPoints.isEmpty()) {
            MobRaceStudio.LOGGER.warn("No spawn points defined for arena: {}", arena.getName());
            return spawned;
        }

        for (MobConfig config : mobConfigs) {
            if (!config.isEnabled()) continue;

            Identifier entityId = Identifier.tryParse(config.getEntityId());
            if (entityId == null) continue;

            EntityType<?> entityType = Registries.ENTITY_TYPE.get(entityId);
            if (entityType == null) {
                MobRaceStudio.LOGGER.warn("Unknown entity type: {}", config.getEntityId());
                continue;
            }

            for (int i = 0; i < config.getSpawnCount(); i++) {
                SpawnPoint spawnPoint = spawnPoints.get(i % spawnPoints.size());
                Vec3d pos = spawnPoint.getPosition();

                MobEntity mob = (MobEntity) entityType.create(world);
                if (mob == null) continue;

                mob.refreshPositionAndAngles(pos.x, pos.y, pos.z, spawnPoint.getYaw(), 0);
                mob.setPersistent();

                applyConfig(mob, config);

                world.spawnEntity(mob);
                spawned.add(mob);

                SmartMovement ai = new SmartMovement(mob, arena.getCheckpoints());
                activeMobs.put(mob.getUuid(), ai);
                mobEntities.put(mob.getUuid(), mob);
            }
        }

        MobRaceStudio.LOGGER.info("Spawned {} race mobs for arena: {}", spawned.size(), arena.getName());
        return spawned;
    }

    private void applyConfig(MobEntity mob, MobConfig config) {
        if (config.getCustomName() != null && !config.getCustomName().isEmpty()) {
            mob.setCustomName(Text.literal(config.getCustomName()));
            mob.setCustomNameVisible(true);
        }

        if (mob.getAttributes() != null) {
            var healthAttr = mob.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
            if (healthAttr != null) {
                healthAttr.setBaseValue(config.getHealth());
            }
            mob.setHealth(config.getHealth());

            var speedAttr = mob.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
            if (speedAttr != null) {
                speedAttr.setBaseValue(speedAttr.getBaseValue() * config.getSpeedMultiplier());
            }

            // GENERIC_JUMP_STRENGTH not available in 1.20.1
            mob.setSilent(true); // placeholder for jump multiplier logic
        }

        mob.setInvulnerable(config.isInvincible());
        mob.setNoGravity(config.isNoGravity());
        mob.setGlowing(config.isGlow());
        mob.setAiDisabled(config.isNoAI());
    }

    public void tickAll() {
        activeMobs.values().forEach(SmartMovement::tick);
    }

    public SmartMovement getAI(UUID mobId) {
        return activeMobs.get(mobId);
    }

    public MobEntity getMobEntity(UUID mobId) {
        return mobEntities.get(mobId);
    }

    public void removeMob(UUID mobId) {
        activeMobs.remove(mobId);
        MobEntity mob = mobEntities.remove(mobId);
        if (mob != null) {
            mob.removeAllPassengers();
            mob.kill();
        }
    }

    public void clear() {
        new ArrayList<>(mobEntities.keySet()).forEach(this::removeMob);
        activeMobs.clear();
        mobEntities.clear();
    }

    public Map<UUID, SmartMovement> getActiveMobs() {
        return activeMobs;
    }

    public int getActiveCount() {
        return activeMobs.size();
    }
}
