package com.example.examplemod.particle;

import com.example.examplemod.advanced_functions.DynamicStringManager;
import com.example.examplemod.advanced_functions.MotionStrings;

import com.example.particlecomplex.particles.base.BaseParticleType;
import com.example.particlecomplex.particles.base.ParticleAreaSpawner;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4i;

import java.util.Collections;

public class Cross extends ParticleAreaSpawner {
    private final Level level;
    private final BaseParticleType type;
    private final double radius;
    private final double step;
    private final Entity entity;
    public Cross(Level level, BaseParticleType type,double radius, double step,Entity entity) {
        super(level, type, -radius, radius, step);
        this.level=level;
        this.type=type;
        this.step=step;
        this.radius=radius;
        this.entity=entity;
    }




    //可能需要Spawner为-4,4
    public void cross(Vec3 pos){
        this.type.setCenter(pos.x,pos.y,pos.z);
        this.type.setColor(new Vector4i( 100,100,100,255));
        this.type.setLifetime(300);
        this.type.setFps(120);
        this.type.setDiameter(0.5f);
        this.type.setEntitiesID(Collections.singletonList(entity.getId()));
        String pitchXEase=DynamicStringManager.toEaseDynamicExp("pitchX",190,MotionStrings.Bounce[1],0,10);
        String alphaEase = DynamicStringManager.toEaseDynamicExp("w","threshold(255 * (1 - (pt)^3))");
        String entityChaseX=DynamicStringManager.toEaseDynamicExp("x","e0x-x-0.3");
        String entityChaseY=DynamicStringManager.toEaseDynamicExp("y","e0y-y+1");
        String entityChaseZ=DynamicStringManager.toEaseDynamicExp("z","e0z-z");

        this.type.setDynamicExp(pitchXEase+entityChaseX+entityChaseY+entityChaseZ);


        this.setPositionExpression("0","0","t");
        this.createByPositionExpression(pos.x, pos.y, pos.z);
        this.setPositionExpression("0","t","0");
        this.createByPositionExpression(pos.x, pos.y, pos.z);

    }
}
