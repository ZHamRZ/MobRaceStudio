package com.mobracestudio.config;

public class MobConfig {
    private String entityId;
    private boolean enabled;
    private String customName;
    private int spawnCount;
    private float health;
    private float speedMultiplier;
    private float jumpMultiplier;
    private float damageMultiplier;
    private boolean attackEnabled;
    private boolean collisionEnabled;
    private boolean respawn;
    private boolean invincible;
    private boolean glow;
    private boolean noGravity;
    private boolean noAI;
    private String team;
    private boolean useCustomName;

    public MobConfig() {
        this.enabled = true;
        this.spawnCount = 1;
        this.health = 20.0f;
        this.speedMultiplier = 1.0f;
        this.jumpMultiplier = 1.0f;
        this.damageMultiplier = 1.0f;
        this.attackEnabled = false;
        this.collisionEnabled = true;
        this.respawn = true;
        this.invincible = false;
        this.glow = false;
        this.noGravity = false;
        this.noAI = false;
    }

    public MobConfig(String entityId) {
        this();
        this.entityId = entityId;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
        this.useCustomName = customName != null && !customName.isEmpty();
    }

    public int getSpawnCount() {
        return spawnCount;
    }

    public void setSpawnCount(int spawnCount) {
        this.spawnCount = Math.max(1, spawnCount);
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = Math.max(1, health);
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = Math.max(0.1f, speedMultiplier);
    }

    public float getJumpMultiplier() {
        return jumpMultiplier;
    }

    public void setJumpMultiplier(float jumpMultiplier) {
        this.jumpMultiplier = Math.max(0.1f, jumpMultiplier);
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = Math.max(0, damageMultiplier);
    }

    public boolean isAttackEnabled() {
        return attackEnabled;
    }

    public void setAttackEnabled(boolean attackEnabled) {
        this.attackEnabled = attackEnabled;
    }

    public boolean isCollisionEnabled() {
        return collisionEnabled;
    }

    public void setCollisionEnabled(boolean collisionEnabled) {
        this.collisionEnabled = collisionEnabled;
    }

    public boolean isRespawn() {
        return respawn;
    }

    public void setRespawn(boolean respawn) {
        this.respawn = respawn;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public boolean isGlow() {
        return glow;
    }

    public void setGlow(boolean glow) {
        this.glow = glow;
    }

    public boolean isNoGravity() {
        return noGravity;
    }

    public void setNoGravity(boolean noGravity) {
        this.noGravity = noGravity;
    }

    public boolean isNoAI() {
        return noAI;
    }

    public void setNoAI(boolean noAI) {
        this.noAI = noAI;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public boolean useCustomName() {
        return useCustomName;
    }
}
