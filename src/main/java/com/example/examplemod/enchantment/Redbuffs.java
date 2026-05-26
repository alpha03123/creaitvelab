package com.example.examplemod.enchantment;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.registry.Enchantments;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Redbuffs {
    private static final Map<UUID, List<Vec3>> playerPositions = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        UUID playerId = player.getUUID();

        // 记录玩家位置，保存最近20个tick的位置
        List<Vec3> positions = playerPositions.getOrDefault(playerId, new ArrayList<>());
        if (positions.size() >= 20) {
            positions.remove(0); // 移除最早的一个位置
        }
        positions.add(player.position());
        playerPositions.put(playerId, positions);

        // 遍历玩家的护甲，检查是否有附魔，并计算概率
        boolean hasEnchantment = false;
        int enchantmentLevel = 0;
        for (ItemStack armorItem : player.getArmorSlots()) {
            int level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.RED_BUFFS.get(), armorItem);
            if (level > 0) {
                hasEnchantment = true;
                enchantmentLevel = Math.max(enchantmentLevel, level); // 获取最高的附魔等级
            }
        }

        // 根据最高的附魔等级计算触发几率
        if (hasEnchantment && player.level().random.nextFloat() < (0.02f * enchantmentLevel) && positions.size() >= 20) {
            Vec3 rewindPosition = positions.get(0);
            player.teleportTo(rewindPosition.x(), rewindPosition.y(), rewindPosition.z());
        }
    }
}
