package com.example.examplemod.entities.bullet;

import com.example.examplemod.entities.BaseComplexEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CommonBullet extends BaseComplexEntity {
    public CommonBullet(EntityType<?> pEntityType, Level pLevel, Vec3 pos,Vec3 velocity) {
        super(pEntityType, pLevel, pos);
        this.velocity=velocity;
        this.lifetime=200;
    }
    @Override
    public void releaseParticle(){
        Level level=level();
        if(level.isClientSide){
            level.addParticle(ParticleTypes.END_ROD,getX(),getY(),getZ(),0,0,0);
        }else {
            ServerLevel sLever=(ServerLevel) level;
            sLever.sendParticles(ParticleTypes.END_ROD,getX(),getY(),getZ(),1,0,0,0,0);
        }
    }
}
