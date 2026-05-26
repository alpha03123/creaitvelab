package com.example.examplemod.items.uncompleted;

import com.example.examplemod.registry.Moditems;
import com.example.particlecomplex.ExampleMod;
import com.example.particlecomplex.entities.test.AmmoEntity;
import com.example.particlecomplex.particles.base.BaseParticleType;
import com.example.particlecomplex.particles.base.ParticleAreaSpawner;

import com.example.particlecomplex.particles.custom.END_ROD;
import com.example.particlecomplex.particles.custom.FALLING_LAVA;
import com.example.particlecomplex.registry.ModEntities;

import com.example.particlecomplex.utils.entity_utils.EntityGetter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector4i;

import java.util.List;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID)
public class ChainLight_Uncompleted {
    static void createParticleBall(Level level, float x_f, float y_f, float z_f,int radius) {
        BaseParticleType particleType = new FALLING_LAVA();
        particleType
                .setDynamicExp("w<-255*0.5*(1-cos((2*pi*t)/lifetime))")
                .setLifetime(30)
                .setRotation(0, 0, 0)
                .setColor(new Vector4i(100, 100, 100, 500))
                .setDiameter(0.4f)// 设置缩放
                .setCenter(x_f, y_f, z_f)// 设置中心坐标,x,y,z,e?x,e?y,e?z返回的都是实际实体||粒子坐标-中心坐标,也就是相对位置
                .setFps(250);

        ParticleAreaSpawner areaParticle = new ParticleAreaSpawner(level, particleType, -radius, radius, 0.3f);
        areaParticle.createByPositionEquation(x_f,y_f,z_f,"x^2+y^2+z^2-"+ radius * radius,0.1);


    }

    //做个连锁闪电
    @SubscribeEvent
    public static void rightClick(PlayerInteractEvent.RightClickItem event) {

        if (event.getItemStack().getItem() == Moditems.TargetItem.get()&&!event.getLevel().isClientSide) {
            Player player = event.getEntity();
            Entity entity1 = EntityGetter.getClosestEntityInSight(player, 40000d,true);
            if(entity1!=null){
            EntityType<AmmoEntity> entityType = ModEntities.AMMO.get();
            AmmoEntity entity = new AmmoEntity(entityType, event.getLevel(),entity1);
            entity.setPos(entity1.getX(),entity1.getY(),entity1.getZ());
            event.getLevel().addFreshEntity(entity);


            ExampleMod.LOGGER.warn(String.valueOf(entity1));}

            int times = 5; // 控制连锁次数
            for (int i = 0; i < times; i += 1) {
                if (entity1 != null) {
                    double x = entity1.getX();
                    double y = entity1.getY();
                    double z = entity1.getZ();
                    int radius = 50; // 可调整的半径
                    createParticleBall(event.getLevel(), (float) x, (float) y, (float) z, 8);

                    // 获取在圆形区域内的实体
                    List<Entity> entities = EntityGetter.getEntitiesInCircle(event.getLevel(), x, y, z, radius,false);
                    entities.remove(player); // 移除玩家
                    entities.remove(entity1); // 移除当前实体

                    // 检查是否还有其他实体
                    if (!entities.isEmpty()) {
                        entity1 = entities.get(0); // 更新为下一个实体
                    } else {
                        break; // 如果没有其他实体，退出循环
                    }
                } else {
                    break; // 如果当前实体为 null，退出循环
                }
            }
        }
    }

}
