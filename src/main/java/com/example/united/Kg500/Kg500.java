package com.example.united.Kg500;


import com.example.examplemod.entities.Projectile;
import com.example.examplemod.registry.ModEntities;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

public class Kg500 extends Item {

    private static final long FIRST_ROUND_CD = 10000L; // 20秒冷却
    private static final long SECOND_ROUND_CD = 130000L; // 130秒冷却

    public Kg500(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
//        if (pLevel.isClientSide) return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));  // 防止客户端触发物品效果
//
//        // 如果物品冷却中，直接返回
//        if (pPlayer.getCooldowns().isOnCooldown(this)) {
//            return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
//        }
//
//        // 开始冷却
//        pPlayer.getCooldowns().addCooldown(this, (int) FIRST_ROUND_CD / 50);  // 20秒冷却，单位是tick（1秒=20tick）
//        int randomRange =50;
//        int height=10;
//        // 生成Ship实体并初始化
        Vec3 playerPos = pPlayer.position();
//        Vec3 startPos = playerPos.add(new Vec3(new Random().nextInt(-randomRange, randomRange), height, new Random().nextInt(-randomRange, randomRange)));
//
//
//
//        // 创建 Ship 实体
//        Ship ship = new Ship(ModEntities.BASE_COMPLEX_ENTITY.get(), pLevel, startPos,height);
//        pLevel.addFreshEntity(ship);
//
//        // 设置 Ship 的目标位置（玩家头顶）
//        Vec3 targetPos = pPlayer.position().add(0, height, 0);
//        ship.moveTowards(targetPos);
//
        Projectile projectile=new Projectile(ModEntities.BASE_COMPLEX_ENTITY.get(), pLevel,playerPos,new Vec3(0.6,1,0));
        pLevel.addFreshEntity(projectile);

        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }
}
