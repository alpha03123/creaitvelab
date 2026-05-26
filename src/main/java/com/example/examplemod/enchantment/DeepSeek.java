package com.example.examplemod.enchantment;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.advanced_functions.EnchantmentsUtils;
import com.example.examplemod.advanced_functions.EntityUtils;
import com.example.examplemod.registry.Enchantments;
import com.mojang.blaze3d.shaders.Effect;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DeepSeek {
    public static int tick = 0;

    // 处理实体受到伤害事件
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        Entity attacker1 = event.getSource().getEntity();
        if (!(attacker1 instanceof LivingEntity attacker)) return;

        // 获取敌人的盔甲
        ItemStack helmet = entity.getItemBySlot(EquipmentSlot.HEAD);  // 头盔
        ItemStack chestplate = entity.getItemBySlot(EquipmentSlot.CHEST);  // 胸甲
        ItemStack leggings = entity.getItemBySlot(EquipmentSlot.LEGS);  // 裤子
        ItemStack boots = entity.getItemBySlot(EquipmentSlot.FEET);  // 靴子

        // 检查各个护甲部件上的 DeepSeek 附魔等级
        ArrayList<Integer> armorStacks = new ArrayList<>();
        armorStacks.add(EnchantmentsUtils.getStackEnchantLevel(helmet, Enchantments.DEEP_SEEK.get()));
        armorStacks.add(EnchantmentsUtils.getStackEnchantLevel(chestplate, Enchantments.DEEP_SEEK.get()));
        armorStacks.add(EnchantmentsUtils.getStackEnchantLevel(leggings, Enchantments.DEEP_SEEK.get()));
        armorStacks.add(EnchantmentsUtils.getStackEnchantLevel(boots, Enchantments.DEEP_SEEK.get()));

        // 获取最高附魔等级
        int armorLevel = armorStacks.stream().max(Integer::compareTo).orElse(0);

        // 如果有 DeepSeek 附魔
        if (armorLevel > 0) {
            attacker.addEffect(new MobEffectInstance(MobEffects.GLOWING, 4*20, 1));  // 给攻击者添加发光效果
            // 如果是玩家并且在潜行
            if (entity instanceof Player player && player.isCrouching()) {
                player.lookAt(EntityAnchorArgument.Anchor.EYES, attacker.getEyePosition(1));  // 玩家看向攻击者
            }
        }
    }

    // 处理玩家的tick事件
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.player.level().isClientSide)return;
        tick += 1;
        Player attacker = event.player;

        // 获取最近的敌人
        LivingEntity target = (LivingEntity) EntityUtils.getNearestEntity(EntityUtils.getEntitiesInRadius(attacker, 7).stream()
                .filter(e -> e instanceof LivingEntity)
                .toList(), attacker);

        if (target == null) return;

        // 获取玩家武器上 DeepSeek 的附魔等级
        int weaponLevel = EnchantmentsUtils.getStackEnchantLevel(attacker.getMainHandItem(), Enchantments.DEEP_SEEK.get());

        // 如果武器有 DeepSeek 附魔并且不是弓
        if (weaponLevel > 0 && !(attacker.getMainHandItem().getItem() instanceof BowItem)) {
            if (attacker.isCrouching()) {
                // 玩家看向目标
                attacker.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition(1));

                // 如果是剑
                if (attacker.getMainHandItem().getItem() instanceof SwordItem item) {
                    float attackSpeed = 1.6F;
                    int range = 5;

                    // 每隔一段时间攻击一次
                    if (tick % (attackSpeed * 20) == 0 && EntityUtils.getNearestEntity(EntityUtils.getEntitiesInRadius(attacker, range).stream()
                            .filter(e -> e instanceof LivingEntity)
                            .toList(), attacker) == target) {

                        if (!event.player.level().isClientSide) {
                            // 伤害处理
                            target.hurt(attacker.damageSources().generic(), item.getDamage()+1);
                            attacker.sweepAttack();  // 执行扫击攻击
                            attacker.swing(attacker.swingingArm);  // 执行攻击动画
                            attacker.setPortalCooldown();  // 设置传送门冷却
                        } else {
                            // 客户端的处理（如果需要的话）
                        }
                    }
                }
            }
        }
    }

    // 判断物品是否可以应用 DeepSeek 附魔
    public static int canApplyDeepSeekEnchantment(ItemStack stack) {
        Item item = stack.getItem();

        // 判断物品是否为剑
        if (item instanceof SwordItem) {
            return 1;  // 如果是剑，返回 1
        }

        // 判断物品是否为镐
        if (item instanceof PickaxeItem) {
            return 2;  // 如果是镐，返回 2
        }

        // 判断物品是否为护甲
        if (item instanceof ArmorItem) {
            ArmorItem armor = (ArmorItem) item;
            EquipmentSlot slot = armor.getEquipmentSlot();
            return 3;  // 如果是护甲，返回 3
        }

        return 0; // 其他物品不适用
    }
}
