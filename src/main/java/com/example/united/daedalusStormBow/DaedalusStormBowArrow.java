package com.example.united.daedalusStormBow;

import com.example.examplemod.ExampleMod;

import com.example.examplemod.entities.AreaEntity;
import com.example.examplemod.registry.ModEntities;
import com.example.particlecomplex.particles.base.ParticleAreaSpawner;
import com.example.particlecomplex.registry.ModParticleType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DaedalusStormBowArrow extends AbstractArrow {
    ParticleAreaSpawner spawner = new ParticleAreaSpawner(this.level(), ModParticleType.FALLING_LAVA.get());

    public DaedalusStormBowArrow(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
    }

    public DaedalusStormBowArrow(Level world, LivingEntity shooter) {
        super(ModEntities.CUSTOM_ARROW.get(), shooter, world);  // 这里可以自定义 EntityType
    }
    private boolean critical;
    private boolean inGround = false;

    public boolean isCritical() {
        return critical;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            // 检查箭矢是否在地面上
        } else {
            ExampleMod.LOGGER.info("ArrowEntity");
            spawnParticles();
        }

    }

    public boolean isOnGround() {
        // 检查箭矢是否碰到地面或障碍物
        return this.horizontalCollision || this.verticalCollision;
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {

        super.onHitEntity(result);
        // 实现箭击中实体时的逻辑
        Entity target=result.getEntity();
        AreaEntity area=new AreaEntity(ModEntities.AREA.get(),level(),target.position());
        level().addFreshEntity(area);
        area.setPos((target.position()));

        CompoundTag tag = area.getPersistentData();
        tag.putBoolean("targeted",true);


        // 销毁箭矢
        this.discard();  // 或者 this.remove(RemovalReason.DISCARDED);
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        // 实现箭击中地面时的逻辑
        BlockPos pos = result.getBlockPos();
        AreaEntity area=new AreaEntity(ModEntities.AREA.get(),level(),Vec3.atLowerCornerOf(pos));
        level().addFreshEntity(area);
        area.setPos(Vec3.atLowerCornerOf(pos));


        // 销毁箭矢
        this.discard();  // 或者 this.remove(RemovalReason.DISCARDED);
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return null;
    }
    private void spawnParticles() {

        spawner.createSingle(this.getX(), this.getY(), this.getZ());}



}
