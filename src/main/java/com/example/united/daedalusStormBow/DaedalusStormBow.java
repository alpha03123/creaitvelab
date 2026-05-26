package com.example.united.daedalusStormBow;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.advanced_functions.EntityUtils;


import com.example.particlecomplex.particles.base.BaseParticleType;
import com.example.particlecomplex.particles.base.ParticleAreaSpawner;
import com.example.particlecomplex.particles.custom.FALLING_LAVA;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class DaedalusStormBow extends BowItem {
    private int mode = 2; // Default to Mode 1


    public DaedalusStormBow(Item.Properties properties) {
        super(properties);

    }

    @Override
    public void onUseTick(Level world, LivingEntity entity, ItemStack stack, int count) {
        BaseParticleType baseParticleType=null;
        if (entity instanceof Player player) {
            ParticleAreaSpawner spawner=new ParticleAreaSpawner(world, new FALLING_LAVA());
            if(!EntityUtils.getPlayerTargetBlocks(player,1,200).isEmpty()){
            BlockHitResult hitResult=EntityUtils.getPlayerTargetBlocks(player,1,200).get(0);
            BlockPos pos=hitResult.getBlockPos();
            spawner.createSingle(pos.getX(),pos.getY(),pos.getZ());}
        }
        super.onUseTick(world, entity, stack, count);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            int chargeTime = this.getUseDuration(stack) - timeLeft;
            float power = getPowerTime(chargeTime);

            if (power >= 0.1) {
                if (!world.isClientSide) {
                    if (mode == 1) {
                        // Mode 1: Standard shooting
                        DaedalusStormBowArrow arrowEntity = new DaedalusStormBowArrow(world, player);
                        arrowEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, power * 3.0F, 1.0F);
                        arrowEntity.setBaseDamage(power * 2.0F);

                        if (power >= 1.0F) {
                            arrowEntity.setCritical(true);
                        }

                        world.addFreshEntity(arrowEntity);

                    } else if (mode == 2) {
                        // Mode 2: Spawn arrow above nearest block
//                        BlockHitResult hitResult = player.level().clip(new ClipContext(player.getEyePosition(1.0F), player.getLookAngle().scale(100), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));

                        BlockHitResult hitResult=EntityUtils.getPlayerTargetBlocks(player,1,200).get(0);
                        // Couldn't find a block, apply cooldown
                        // 20 ticks cooldown
                        if (hitResult != null && hitResult.getType() != BlockHitResult.Type.MISS) {
                            Vec3 blockPos = hitResult.getLocation();
                            ExampleMod.LOGGER.info(String.valueOf(blockPos));
                            int height = 60;
                            Vec3 spawnPos = blockPos.add(0, height, 0); // 10 blocks above

                            DaedalusStormBowArrow arrowEntity = new DaedalusStormBowArrow(world, player);
                            arrowEntity.setPos(spawnPos);
                            arrowEntity.shoot(0, -1, 0, power * 3.0F, 1.0F); // Shoot downwards
                            world.addFreshEntity(arrowEntity);

                        }
                    }

                    if (!player.isCreative()) {
                        stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
                    }
                }
            }
        }
    }

    @Override
    public AbstractArrow customArrow(AbstractArrow arrow) {
        return arrow;
    }

    private float getPowerTime(int chargeTime) {
        float f = (float)chargeTime / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }
}
