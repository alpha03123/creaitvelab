package com.example.examplemod.particle;

import com.example.examplemod.advanced_functions.DynamicStringManager;
import com.example.examplemod.advanced_functions.MotionStrings;
import com.example.particlecomplex.particles.base.BaseParticleType;
import com.example.particlecomplex.particles.base.ParticleAreaSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4i;

public class Circle {
    private final  Level level;
    private final BaseParticleType type;

    private final double radius;
    private  final double step;
    public Circle(Level level, BaseParticleType type, double radius, double step) {
        this.type=type;
        this.radius=radius;
        this.level=level;
        this.step=step;
    }




    //可能需要Spawner为-4,4
    public void circle(Vec3 pos){
        this.type.setCenter(pos.x,pos.y,pos.z);
        this.type.setColor(new Vector4i( 100,100,100,255));
        this.type.setLifetime(300);
        this.type.setFps(100);
        this.type.setDiameter(0.5f);

        String pitchXEase;
        ParticleAreaSpawner spawner2=new ParticleAreaSpawner(level,type,-radius,radius,step);
        pitchXEase=DynamicStringManager.toEaseDynamicExp("pitchX",180,DynamicStringManager.reverse(MotionStrings.Bounce[1]),0,3);
        this.type.setDynamicExp(pitchXEase);
        spawner2.setPolarPositionExpression(String.valueOf(radius),"90","t*50");
        spawner2.createByPolarPositionExpression(pos.x, pos.y, pos.z);


    }
}
