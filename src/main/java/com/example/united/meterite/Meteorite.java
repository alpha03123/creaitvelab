package com.example.united.meterite;


import com.example.particlecomplex.particles.base.BaseParticleType;
import com.example.particlecomplex.particles.base.ParticleAreaSpawner;
import com.example.particlecomplex.registry.ModParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.joml.Vector4i;

import java.util.Random;

public class Meteorite extends Entity{

    private final Vec3 targetPos;

    public Meteorite(EntityType<?> pEntityType, Level pLevel, Vec3 targetPos) {
        super(pEntityType, pLevel);
        this.targetPos=targetPos;
    }
    private static Vector3d getNormalizedVecByEntityAndEntity(Entity entity1,Entity entity2){
        double xd=entity1.getX()-entity2.getX();
        double yd=entity1.getY()-entity2.getY();
        double zd=entity1.getZ()-entity2.getZ();
        double r=Math.sqrt(Math.pow(xd,2)+Math.pow(yd,2)+Math.pow(zd,2));
        return new Vector3d(xd/-r,yd/-r,zd/-r);
    }
    private static Vector3d getNormalizedVecByEntityAndPos(Entity entity1,double x,double y,double z){

        double xd=entity1.getX()-x;
        double yd=entity1.getY()-y;
        double zd=entity1.getZ()-z;
        double r=-Math.sqrt(Math.pow(xd,2)+Math.pow(yd,2)+Math.pow(zd,2));
        if(r==0f){
            r=1000f;
        }

        return new Vector3d(xd/r,yd/r,zd/r);
    }
    private static void createRandomSquare(Level level,double x_f,double y_f,double z_f,int radius){
        if(!level.isClientSide){
            BaseParticleType particleType = ModParticleType.DUST.get();
            particleType
                    .setLifetime(36)
                    .setColor(new Vector4i(100, 100, 100, 500))
                    .setDiameter(1f)// 设置缩放
                    .setCenter(x_f, y_f, z_f)// 设置中心坐标,x,y,z,e?x,e?y,e?z返回的都是实际实体||粒子坐标-中心坐标,也就是相对位置
                    .setRotation(0,0,0)
                    .setFps(200)
                    .setVecExpX("(sRandom-0.5)*((t)/lifetime)*cos((x/(t+1))*2*pi)*4")
                    .setVecExpY("(sRandom-0.5)*((t)/lifetime)*sin((y/(t+1))*2*pi)*4")
                    .setVecExpZ("(sRandom-0.5)*((t)/lifetime)*cos((z/(t+1))*2*pi)*4")
                    .setDynamicExp("w <- threshold(255 * (1 - (t / lifetime)^3)-30)"); //渐隐
            ParticleAreaSpawner areaParticle = new ParticleAreaSpawner(level, particleType, -1, 1, (float) (1));
            areaParticle.setPolarPositionExpression(String.valueOf(0),"0","0");
            for (int i = 0; i < 2; i++) {
                float randomX=new Random().nextFloat()-0.5f;
                float randomZ=new Random().nextFloat()-0.5f;
                areaParticle.createByPolarPositionExpression((float) (x_f+randomX*radius/2), (float) y_f, (float) (z_f+randomZ*radius/2));
            }
        }
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        //todo 改为白色粒子随机轰炸,黑色粒子定点打击

        Vector3d vec= getNormalizedVecByEntityAndPos(this,targetPos.x,targetPos.y,targetPos.z);
        System.out.println(vec.y);
        this.setDeltaMovement(vec.x,vec.y,vec.z);
//        this.setVecExp("-1","0","0");
        createRandomSquare(this.level(),this.getX(),this.getY(),this.getZ(),24-(int)(1.5F));

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }
}
