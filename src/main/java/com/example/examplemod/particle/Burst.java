package com.example.examplemod.particle;

import com.example.particlecomplex.particles.base.BaseParticleType;
import com.example.particlecomplex.particles.base.ParticleAreaSpawner;
import com.example.particlecomplex.particles.custom.END_ROD;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4i;

public class Burst {
    private Level level;
    private BaseParticleType type;

    private double start;
    private double end;
    private  double step;
    public Burst(Level level, BaseParticleType type, double start, double end, double step) {
        this.level=level;
        this.type=type;
        this.start=start;
        this.end=end;

        this.step=step;
    }

    public Burst(Level level, BaseParticleType type) {
        this.level=level;
        this.type=type;

    }

    public void burst(Vec3 pos,int particleAmount,double radius,double easeRage){
        for (int i = 0; i < particleAmount; i++) {
            if(!level.isClientSide){
                ParticleAreaSpawner spawner=new ParticleAreaSpawner(level,type,start,end,step);
                type.setVecExpX("min((sRandom-0.5)*"+radius+"-"+easeRage+"*t,0)");
                type.setVecExpY("min((sRandom-0.5)*"+radius+"-"+easeRage+"*t,0)");
                type.setVecExpZ("min((sRandom-0.5)*"+radius+"-"+easeRage+"*t,0)");
                spawner.createSingle(pos.x, pos.y, pos.z);
            }
        }
    }


}
