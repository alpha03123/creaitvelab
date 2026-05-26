package com.example.examplemod.enchantment;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.registry.Enchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent.Finish;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Anger {

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            List<ItemStack> enchantedItems = getEnchantedItems(player);

            for (ItemStack item : enchantedItems) {
                int level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ANGER.get(), item);
                if (level < 20) {
                    EnchantmentHelper.setEnchantments(Map.of(Enchantments.ANGER.get(), level + 1), item);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerKillEntity(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            List<ItemStack> enchantedItems = getEnchantedItems(player);

            for (ItemStack item : enchantedItems) {
                EnchantmentHelper.setEnchantments(Map.of(Enchantments.ANGER.get(), 1), item);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        List<ItemStack> enchantedItems = getEnchantedItems(player);

        for (ItemStack item : enchantedItems) {
            int level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ANGER.get(), item);

            if (level > 1) {
                // Apply strength and health boost effects based on enchantment level
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 60, level - 1));
                player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 60, level - 1));
            }
        }
    }

    private static List<ItemStack> getEnchantedItems(Player player) {
        List<ItemStack> enchantedItems = new ArrayList<>();

        for (ItemStack item : player.getArmorSlots()) {
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ANGER.get(), item) > 0) {
                enchantedItems.add(item);
            }
        }

        return enchantedItems;
    }
}
