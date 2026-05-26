package com.example.united.WASP;

import com.example.examplemod.registry.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class WaspMissileEntity extends Projectile {
    private int life;
    private static final int TRACKING_DELAY = 15;
    private static final float SPEED = 0.6F;
    private static final float TRACKING_STRENGTH = 0.06F;
    private static final float EXPLOSION_POWER = 3.5F;

    private LivingEntity shooter;
    private LivingEntity target;
    public WaspMissileEntity(EntityType<? extends WaspMissileEntity> type, Level level) {
        super(type, level);
    }
    public WaspMissileEntity(Level level, LivingEntity shooter, LivingEntity target) {
        super(ModEntities.WASP_MISSILE.get(), level);
        this.shooter = shooter;
        this.target = target;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();
        life++;

        if (life > TRACKING_DELAY && target != null && target.isAlive()) {
            Vec3 toTarget = target.getEyePosition().subtract(position()).normalize();

            // 平滑追踪
            Vec3 newVel = getDeltaMovement()
                    .add(toTarget.scale(TRACKING_STRENGTH))
                    .normalize()
                    .scale(SPEED);

            setDeltaMovement(newVel);
        }

        // 更新位置
        setPos(position().add(getDeltaMovement()));

        // 检查碰撞或超时
        if (horizontalCollision || verticalCollision || life > 100 || (target != null && !target.isAlive())) {
            explode();
        }
    }


    private void explode() {
        if (!level().isClientSide) {
            level().explode(this, getX(), getY(), getZ(), EXPLOSION_POWER, Level.ExplosionInteraction.MOB);
            discard();
        }
    }
}
