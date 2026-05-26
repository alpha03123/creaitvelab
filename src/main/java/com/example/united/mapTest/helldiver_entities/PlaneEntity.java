package com.example.united.mapTest.helldiver_entities;

import com.example.examplemod.entities.BaseComplexEntity;
import com.example.examplemod.entities.bullet.CommonBullet;
import com.example.examplemod.registry.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PlaneEntity extends CommonBullet {
    public PlaneEntity(Level pLevel, Vec3 pos, Vec3 velocity) {
        super(ModEntities.BASE_COMPLEX_ENTITY.get(), pLevel, pos, velocity);
        this.lifetime*=2;
    }
}
