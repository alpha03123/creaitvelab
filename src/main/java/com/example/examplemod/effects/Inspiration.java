package com.example.examplemod.effects;

import com.example.examplemod.registry.ModEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Inspiration extends MobEffect {
    public Inspiration(MobEffectCategory pCategory) {
        super(pCategory, 0x998877);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        // 获取领域内所有实体
        Level level = entity.level();

        int duration = entity.getEffect(ModEffects.INSPIRATION.get()).getDuration();
        if (duration==0){
            entity.addEffect(new MobEffectInstance(MobEffects.HUNGER,50,amplifier));
        }
        AABB area = new AABB(entity.blockPosition()).inflate(10.0); // 领域范围10格
        for (Entity nearbyEntity : level.getEntitiesOfClass(Entity.class, area)) {
            // 如果实体是玩家，并且不是释放药水的玩家，则应用效果
            if (nearbyEntity != entity) {
                applyEffectToEntity(nearbyEntity, amplifier);
            }
        }
    }

    private void applyEffectToEntity(Entity entity, int amplifier) {
        // 减缓速度
        double speedFactor = 1.0 - (0.01 * (amplifier));
        Vec3 currentMotion = entity.getDeltaMovement();
        Vec3 newMotion = new Vec3(currentMotion.x * speedFactor, 0, currentMotion.z * speedFactor);
        entity.setDeltaMovement(newMotion);

    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration%2==0;
    }
}
