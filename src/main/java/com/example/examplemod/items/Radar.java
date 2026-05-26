
package com.example.examplemod.items;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.registry.Moditems;


import com.example.particlecomplex.particles.custom.END_ROD;
import com.example.particlecomplex.particles.custom.FALLING_LAVA;
import com.example.particlecomplex.utils.task_scheduler_utils.ParticleScheduler;
import com.example.particlecomplex.utils.task_scheduler_utils.tasks.addEffectTask;
import com.example.particlecomplex.utils.task_scheduler_utils.tasks.createByPolarPositionExpressionTask;
import com.example.particlecomplex.particles.base.ParticleAreaSpawner;
import com.example.particlecomplex.particles.base.BaseParticleType;

import com.example.particlecomplex.registry.ModParticleType;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3d;
import org.joml.Vector4i;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.particlecomplex.utils.entity_utils.EntityGetter.getEntitiesInCircle;
@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Radar extends Item {
    public static List<Arrow> remarkedDiscard =new ArrayList<>();

    public Radar(Properties pProperties) {
        super(pProperties);
    }

    static LivingEntity getNearestEntity(List<LivingEntity> entities, double x, double y, double z) {
        LivingEntity nearestEntity = null;
        double nearestDistanceSquared = Double.MAX_VALUE;

        for (LivingEntity entity : entities) {
            double distanceSquared = entity.distanceToSqr(x, y, z);
            if (distanceSquared < nearestDistanceSquared) {
                nearestDistanceSquared = distanceSquared;
                nearestEntity = entity;
            }
        }
        return nearestEntity;
    }
    //todo 渐变粒子特效在这
    static void createBallSquare(Level level,float x_f,float y_f,float z_f){
            BaseParticleType particleType = new FALLING_LAVA();


            particleType
                    .setLifetime(50)
                    .setColor(new Vector4i(100, 100, 100, 500))
                    .setDiameter(1f)// 设置缩放
                    .setCenter(x_f, y_f, z_f)// 设置中心坐标,x,y,z,e?x,e?y,e?z返回的都是实际实体||粒子坐标-中心坐标,也就是相对位置
                    .setRotation(0,0,0)
                    .setFps(200)
//                        .setEntitiesID(Collections.singletonList(event.getEntity().getId()))
                    //TYPE1
                    .setVecExpX("(sRandom-0.5)*((t)/lifetime)*cos((x/(t+1))*2*pi)*4")
                    .setVecExpY("(sRandom-0.5)*((t)/lifetime)*sin((y/(t+1))*2*pi)*4")
                    .setVecExpZ("(sRandom-0.5)*((t)/lifetime)*cos((z/(t+1))*2*pi)*4")
                    //TYPE2
//                        .setVecExpX("(sRandom-0.5) * ((t / lifetime) * 10) * cos((t + x / 10) * 2 * pi)")
//                        .setVecExpY("(sRandom-0.5) * ((t / lifetime) * 10) * sin((t + y / 10) * 2 * pi)")
//                        .setVecExpZ("(sRandom-0.5) * ((t / lifetime) * 5)")
                    //TYPE3
//                        .setVecExpX("(sRandom-0.5) * (1 + 0.5 * sin((t / lifetime) * pi)) * cos((x / (t + 1)) * 2 * pi) * 8")
//                        .setVecExpY("(sRandom-0.5) * (1 + 0.5 * sin((t / lifetime) * pi)) * sin((y / (t + 1)) * 2 * pi) * 8")
//                        .setVecExpZ("(sRandom-0.5) * (1 + 0.5 * sin((t / lifetime) * pi)) * cos((z / (t + 1)) * 2 * pi) * 8")

//                        .setVecExpX("(sRandom-0.5) * (t / lifetime) * sin((y / (t + 1)) * pi) * 10")
//                        .setVecExpY("(sRandom-0.5) * (t / lifetime) * cos((z / (t + 1)) * pi) * 10")
//                        .setVecExpZ("(sRandom-0.5) * (t / lifetime) * sin((x / (t + 1)) * pi) * 10")

//                        .setVecExpX("(sRandom-0.5) * 8 * cos((t / lifetime) * 2 * pi + x / 5)")
//                        .setVecExpY("(sRandom-0.5) * 8 * sin((t / lifetime) * 2 * pi + y / 5)")
//                        .setVecExpZ("(sRandom-0.5) * 8 * cos((t / lifetime) * 2 * pi + z / 5)")




//                        .setDynamicExp("w<-255*0.5*(1-cos((2*pi*t)/lifetime))") //渐隐渐显
                    .setDynamicExp("w <- threshold(255 * (1 - (t / lifetime)^3)-30)"); //渐隐
            ParticleAreaSpawner areaParticle = new ParticleAreaSpawner(level, particleType, -5, 5, (float) (0.28));
//                    areaParticle.setPolarPositionExpression(String.valueOf(i*4),"0","t");
//                    areaParticle.createByPolarPositionExpression(x_f+40, y_f+i*5, z_f);
            areaParticle.createByPositionEquation(x_f,y_f,z_f,"x^2+y^2+z^2-25",0.5);

    }
    static void createRandomSquare(Level level,float x_f,float y_f,float z_f){
        BaseParticleType particleType = new FALLING_LAVA();
        particleType
                .setLifetime(50)
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
        for (int i = 0; i < 30; i++) {
            float randomX=new Random().nextFloat()-0.5f;
            float randomZ=new Random().nextFloat()-0.5f;
            int radius=10;
            areaParticle.createByPolarPositionExpression(x_f+randomX*radius, y_f, z_f+randomZ*radius);
        }



    }

    static void createParticleBeam(Level level, float x_f, float y_f, float z_f) {
        BaseParticleType particleType2 = ModParticleType.SONIC_BOOM.get();
        particleType2
                .setRotation(0, 0, 0)
                .setColor(new Vector4i(100, 100, 100, 500))// 设置速度
                .setDiameter(1f)// 设置缩放
                .setCenter(x_f, y_f, z_f)// 设置中心坐标,x,y,z,e?x,e?y,e?z返回的都是实际实体||粒子坐标-中心坐标,也就是相对位置
                .setFps(80)
                .setDynamicExp("centerX<-e0x;centerY<-e0y;centerZ<-e0z");

        ParticleAreaSpawner areaParticle = new ParticleAreaSpawner(level, particleType2, -20, 20, (float) (0.08));
        areaParticle.setPositionExpression("(random()-0.5)*6", "-10+t*3", "(random()-0.5)*6");
        areaParticle.createByPositionExpression(x_f, y_f, z_f);

    }

    static void createParticleCircle(Level level, float x_f, float y_f, float z_f) {
        BaseParticleType particleType = new FALLING_LAVA();
        particleType
                .setLifetime(30)
                .setRotation(0, 0, 0)
                .setColor(new Vector4i(100, 100, 100, 500))
                .setDiameter(0.4f)// 设置缩放
                .setCenter(x_f, y_f, z_f)// 设置中心坐标,x,y,z,e?x,e?y,e?z返回的都是实际实体||粒子坐标-中心坐标,也就是相对位置
                .setFps(1);

        for (int i = 0; i <= 5; i += 1) {
            ParticleAreaSpawner areaParticle = new ParticleAreaSpawner(level, particleType, -50, 50, (float) (5 - 0.3 * i));
            areaParticle.setPolarPositionExpression(String.valueOf((i + 1) * 3), "0", "4*t");
            areaParticle.createByPolarPositionExpression(x_f, y_f + i * 4, z_f);
        }
    }
    static void addEffect(Level level, float x_f, float y_f, float z_f){
        ParticleScheduler scheduler = new ParticleScheduler(level);
        int n=12;
        for (int i = 0; i <= n; i += 1) {
            List<Entity> entities= getEntitiesInCircle(level,x_f,y_f,z_f,(i+1)*5,false);
            for(Entity entity:entities){
                if(entity instanceof LivingEntity){
                    scheduler.addTask(new addEffectTask((LivingEntity) entity, MobEffects.GLOWING,180,3,45+i*8));
                }
            }
        }
    }
    static void createGradualParticleRing(Level level, float x_f, float y_f, float z_f){
        ParticleScheduler scheduler = new ParticleScheduler(level);
        BaseParticleType particleType2 = ModParticleType.FALLING_LAVA.get();
        particleType2
                .setDynamicExp("w<-255*0.5*(1-cos((2*pi*t)/lifetime))")
                .setLifetime(220)
                .setRotation(0, 0, 0)
                .setColor(new Vector4i(100, 100, 100, 0))
                .setDiameter(0.6f)// 设置缩放
                .setCenter(x_f, y_f, z_f)// 设置中心坐标,x,y,z,e?x,e?y,e?z返回的都是实际实体||粒子坐标-中心坐标,也就是相对位置
                .setFps(20);
        ParticleAreaSpawner areaSpawner=new ParticleAreaSpawner(level,particleType2,-10,50,1.5f);
        areaSpawner.setPositionExpression("0","t","0");


        BaseParticleType particleType = new FALLING_LAVA();
        particleType
                .setDynamicExp("w<-255*0.5*(1-cos((2*pi*t)/lifetime))")
                .setSpeed(new Vector3d(0,-50,0))
                .setLifetime(20)
                .setRotation(0, 0, 0)
                .setColor(new Vector4i(100, 100, 100, 0))
                .setDiameter(1f)// 设置缩放
                .setCenter(x_f, y_f, z_f)// 设置中心坐标,x,y,z,e?x,e?y,e?z返回的都是实际实体||粒子坐标-中心坐标,也就是相对位置
                .setFps(600)
                .setRotation(0,1,5);

        int n=12; //范围
        for (int i = 0; i <= n; i += 1) {
            ParticleAreaSpawner areaParticle = new ParticleAreaSpawner(level, particleType, -50, 50, (float) (0.52-0.02*i));
            areaParticle.setPolarPositionExpression(String.valueOf((i + 1) * 5), "0", "4*t");
            scheduler.addTask(new createByPolarPositionExpressionTask(areaParticle,x_f,y_f+20,z_f,45+i*8));
        }
    }




    @SubscribeEvent
    public static void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getItemStack().getItem() == Moditems.python.get()) {
            Level level = event.getLevel();
            float x_f = (float) event.getEntity().getX();
            float y_f = (float) event.getEntity().getY();
            float z_f = (float) event.getEntity().getZ();
            if (level.isClientSide) {
                createParticleCircle(level, x_f, y_f, z_f);
                createParticleBeam(level, x_f, y_f, z_f);
                createGradualParticleRing(level,x_f,y_f,z_f);
            }
            else {
                    addEffect(level,x_f,y_f,z_f);

            }
        }
    }
}

