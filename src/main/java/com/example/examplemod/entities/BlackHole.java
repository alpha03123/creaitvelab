package com.example.examplemod.entities;

import com.example.examplemod.advanced_functions.EntityUtils;
import com.example.examplemod.entities.BaseComplexEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class BlackHole extends BaseComplexEntity {

    private Vec3 velocity;
    private final Vec3 gravity = new Vec3(0, -0.08, 0);  // 模拟重力 (每tick下落0.05)

    public BlackHole(EntityType<BaseComplexEntity> pEntityType, Level pLevel,Vec3 pos) {
        super(pEntityType, pLevel,pos,new Vec3(0,0,0));  // 传入位置和速度
        this.lifetime=60;
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 pos=this.position();
        for(Entity entity: EntityUtils.getEntitiesInRadius(this,20)){
            entity.setDeltaMovement(pos.subtract(entity.position()).normalize().scale(0.3));
            if(entity.level().isClientSide)return;
            ServerLevel level= (ServerLevel)entity.level();
            level.sendParticles(ParticleTypes.END_ROD,entity.getX(),entity.getY(),entity.getZ(),3,0,0,0,0);
        }
    }
    @Override
    public void releaseParticle(){}

}
