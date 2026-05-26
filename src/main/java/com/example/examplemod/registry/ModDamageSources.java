package com.example.examplemod.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class ModDamageSources {
    public static final ResourceKey<DamageType> HIT = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("examplemod", "hit"));


}
