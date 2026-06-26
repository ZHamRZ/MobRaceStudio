package com.mobracestudio.registry;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class AttributeRegistry {
    private static final Map<String, EntityAttribute> ATTRIBUTES = new LinkedHashMap<>();

    public static void initialize() {
        ATTRIBUTES.clear();
        Registries.ATTRIBUTE.forEach(attr -> {
            Identifier id = Registries.ATTRIBUTE.getId(attr);
            if (id != null) {
                ATTRIBUTES.put(id.toString(), attr);
            }
        });
    }

    public static double getAttributeValue(LivingEntity entity, EntityAttribute attribute) {
        if (entity.getAttributes().hasAttribute(attribute)) {
            return entity.getAttributeValue(attribute);
        }
        return attribute.getDefaultValue();
    }

    public static double getDefaultSpeed(LivingEntity entity) {
        return getAttributeValue(entity, EntityAttributes.GENERIC_MOVEMENT_SPEED);
    }

    public static double getMaxHealth(LivingEntity entity) {
        return getAttributeValue(entity, EntityAttributes.GENERIC_MAX_HEALTH);
    }

    public static double getJumpStrength(LivingEntity entity) {
        return 0.42;
    }

    public static Map<String, EntityAttribute> getAllAttributes() {
        return ATTRIBUTES;
    }
}
