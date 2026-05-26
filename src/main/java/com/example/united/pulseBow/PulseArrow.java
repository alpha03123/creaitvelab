package com.example.united.pulseBow;

import com.example.examplemod.registry.ModEffects;
import com.example.particlecomplex.ExampleMod;
import com.example.particlecomplex.particles.base.BaseParticleType;
import com.example.particlecomplex.particles.base.ParticleAreaSpawner;

import com.example.particlecomplex.particles.custom.FALLING_LAVA;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector4i;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PulseArrow extends AbstractArrow {
    public static List<PulseArrow> remarkedDiscard =new ArrayList<>();

    public PulseArrow(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public PulseArrow(Level level, LivingEntity shooter) {
        super(EntityType.ARROW, shooter, level); // 可替换为自定义箭实体类型
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

    static void createRandomSquare(Level level,double x_f,double y_f,double z_f){
        if(!level.isClientSide){
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
        for (int i = 0; i < 20; i++) {
            float randomX=new Random().nextFloat()-0.5f;
            float randomZ=new Random().nextFloat()-0.5f;
            int radius=10;
            areaParticle.createByPolarPositionExpression(x_f+randomX*radius, y_f, z_f+randomZ*radius);
        }
    }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {

    }



    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if(!level().isClientSide){
            double x_f = this.getX();
            double y_f = this.getY();
            double z_f = this.getZ();
            double minX = x_f - 5;
            double maxX = x_f + 5;
            double minZ = z_f - 5;
            double maxZ = z_f + 5;
            List<LivingEntity> entities_1 = level().getEntitiesOfClass(LivingEntity.class, new AABB(minX, y_f-1, minZ, maxX, y_f+1, maxZ));

            if(!entities_1.isEmpty()) {
                getNearestEntity(entities_1,x_f,y_f,z_f).addEffect(new MobEffectInstance(ModEffects.EXAMPLE_EFFECT.get(),18*20,2));
            }

            createRandomSquare(level(), x_f, y_f, z_f);
            remarkedDiscard.add(this);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if(!level().isClientSide){
            double x_f = this.getX();
            double y_f = this.getY();
            double z_f = this.getZ();
            double minX = x_f - 5;
            double maxX = x_f + 5;
            double minZ = z_f - 5;
            double maxZ = z_f + 5;
            List<LivingEntity> entities_1 = level().getEntitiesOfClass(LivingEntity.class, new AABB(minX, y_f-1, minZ, maxX, y_f+1, maxZ));

            if(!entities_1.isEmpty()) {
                getNearestEntity(entities_1,x_f,y_f,z_f).addEffect(new MobEffectInstance(ModEffects.EXAMPLE_EFFECT.get(),18*20,2));
            }

            createRandomSquare(level(), x_f, y_f, z_f);
            remarkedDiscard.add(this);
        }
    }

    @Override
    public void tick() {
        super.tick();
        // 添加粒子效果
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return Items.ARROW.getDefaultInstance();
    }
}
