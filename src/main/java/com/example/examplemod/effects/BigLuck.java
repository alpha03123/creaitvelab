package com.example.examplemod.effects;

import com.example.examplemod.registry.ModDamageSources;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BigLuck extends MobEffect {
    public BigLuck(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        Level level = entity.level();

        // 获取实体当前视线方向
        Vec3 lookVec = entity.getLookAngle();
        Vec3 horizontalLookVec = new Vec3(lookVec.x, 0, lookVec.z);

        if (horizontalLookVec.lengthSqr() > 1.0E-6) {
            horizontalLookVec = horizontalLookVec.normalize();
            double speed = 0.3 + 0.15 * amplifier;
            Vec3 motion = new Vec3(horizontalLookVec.x * speed, entity.getDeltaMovement().y, horizontalLookVec.z * speed);
            entity.setDeltaMovement(motion);

            if (!level.isClientSide) {
                BlockPos basePos = entity.blockPosition();
                ((ServerLevel) level).sendParticles(ParticleTypes.END_ROD,entity.position().x,entity.position().y,entity.position().z,7,0,0,0,0);

                // 定义八个方向的偏移
                int[][] directions = {
                        {1, 0}, {1, 1}, {0, 1}, {-1, 1},
                        {-1, 0}, {-1, -1}, {0, -1}, {1, -1}
                };

                boolean brokeBlock = false;

                for (int[] dir : directions) {
                    int dx = dir[0];
                    int dz = dir[1];

                    // 检测底部(A点)四周
                    BlockPos checkPosA = basePos.offset(dx, 0, dz);
                    if (!level.isEmptyBlock(checkPosA)) {
                        level.destroyBlock(checkPosA, true);
                        ((ServerLevel) level).sendParticles(ParticleTypes.EXPLOSION,checkPosA.getX(),checkPosA.getY(),checkPosA.getZ(),1,0,0,0,0);
                        brokeBlock = true;
                    }

                    // 检测顶部(B点)四周
                    BlockPos checkPosB = basePos.offset(dx, 1, dz);
                    if (!level.isEmptyBlock(checkPosB)) {
                        level.destroyBlock(checkPosB, true);
                        ((ServerLevel) level).sendParticles(ParticleTypes.EXPLOSION,checkPosB.getX(),checkPosB.getY(),checkPosB.getZ(),1,0,0,0,0);
                        brokeBlock = true;
                    }
                }

                if (brokeBlock) {
                    entity.hurt(entity.damageSources().fall(), 2.0F); // 碰到方块扣1血
                }

                // ===== 新增：检测撞到实体 =====
                AABB collisionBox = entity.getBoundingBox().inflate(0.5); // 稍微扩展一点范围
                for (Entity target : level.getEntities(entity, collisionBox)) {
                    if (target instanceof LivingEntity livingTarget && target != entity) {
                        DamageSource damageSource = new DamageSource(
                                level.registryAccess()
                                        .registryOrThrow(Registries.DAMAGE_TYPE)
                                        .getHolderOrThrow(ModDamageSources.HIT),
                                entity // attackerEntity 需要是一个 Entity 实体
                        );


                        // 造成撞击伤害
                        livingTarget.hurt(damageSource, 10.0F); // 对方扣10血

                        // 计算玩家到目标的水平向量
                        Vec3 direction = livingTarget.position().subtract(entity.position());
                        Vec3 horizontalDirection = new Vec3(direction.x, 0, direction.z);

                        if (horizontalDirection.lengthSqr() > 1.0E-6) {
                            horizontalDirection = horizontalDirection.normalize();
                        } else {
                            horizontalDirection = new Vec3(1, 0, 0); // 如果方向太小，默认向东推
                        }

                        double pushStrength = 1.8; // 水平方向推出的速度大小

                        // 设置被撞飞的速度
                        Vec3 knockback = new Vec3(
                                horizontalDirection.x * pushStrength*amplifier,
                                0.6*amplifier, // 固定向上2格的速度
                                horizontalDirection.z * pushStrength*amplifier
                        );

                        livingTarget.setDeltaMovement(knockback);
                        livingTarget.hasImpulse = true; // 标记有移动冲量（重要，不然有时候不生效！）

                        // 自己扣3点血
                        entity.hurt(entity.damageSources().fall(), 5.0F);
                    }
                }
            }
        }
    }







    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
