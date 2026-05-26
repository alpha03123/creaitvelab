package com.example.examplemod.enchantment;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.advanced_functions.EntityUtils;
import com.example.examplemod.registry.Enchantments;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Oiiaii {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        List<Entity> entities = EntityUtils.getEntitiesInRadius(player, 8);  // 获取范围内的实体

        List<Entity> itemEntities = entities.stream().filter(e -> e instanceof ItemEntity).toList();  // 过滤出物品实体

        if (!itemEntities.isEmpty()) {
            // 获取第一个物品实体
            ItemEntity firstItemEntity = (ItemEntity) itemEntities.get(0);
            if(EnchantmentHelper.getEnchantmentLevel(Enchantments.OIIAII.get(),player)>0 &&
                    EnchantmentHelper.getEnchantments(firstItemEntity.getItem()).getOrDefault(Enchantments.OIIAII.get(), 0)>0){
                player.lookAt(EntityAnchorArgument.Anchor.EYES,firstItemEntity.position());
            }
            for(Entity entity:itemEntities){
                ItemEntity itemEntity = (ItemEntity) entity;
                ItemStack itemStack = itemEntity.getItem();  // 获取物品的 ItemStack

                int level = EnchantmentHelper.getEnchantments(itemStack).getOrDefault(Enchantments.OIIAII.get(), 0);
                if (level > 0) {
                    // 获取物品当前位置和玩家位置
                    Vec3 playerPos = player.position();
                    Vec3 itemPos = itemEntity.position();

                    // 计算物品在玩家周围的旋转位置
                    double angle = event.player.level().getGameTime() * 0.3; // 通过游戏时间生成角度，来实现旋转

                    double radius = 3; // 设置旋转半径
                    double xOffset = Math.cos(angle) * radius;
                    double zOffset = Math.sin(angle) * radius;

                    // 更新物品的位置，使其围绕玩家旋转
                    itemEntity.setPos(playerPos.x + xOffset, itemPos.y, playerPos.z + zOffset);
                }
            }


        }
    }
    public static ListTag extractEnchantments(ItemStack itemStack) {
        CompoundTag nbt = itemStack.getTag();  // 获取物品的NBT标签
        if (nbt != null && nbt.contains("Enchantments", 9)) {  // 检查Enchantments是否存在（9表示List类型）
            ListTag enchantments = nbt.getList("Enchantments", 10);  // 获取Enchantments列表，10表示CompoundTag类型
                return enchantments;
        }
        return new ListTag();
    }
    public static int getEnchantLevel(ItemStack itemStack,String enchantName){
        CompoundTag nbt = itemStack.getTag();  // 获取物品的NBT标签
        if (nbt != null && nbt.contains("Enchantments", 9)) {  // 检查Enchantments是否存在（9表示List类型）
            ListTag enchantments = nbt.getList("Enchantments", 10);  // 获取Enchantments列表，10表示CompoundTag类型
            for (int i = 0; i < enchantments.size(); i++) {
                CompoundTag enchantmentTag = enchantments.getCompound(i);  // 获取每个附魔的CompoundTag
                String enchantmentId = enchantmentTag.getString("id");  // 获取附魔的id

                if(Objects.equals(enchantName, enchantmentId)){
                    return  enchantmentTag.getInt("lvl");  // 获取附魔的等级
                }
            }
        }
        return 0;
    }

    private static List<ItemStack> getEnchantedItems(Player player) {
        List<ItemStack> enchantedItems = new ArrayList<>();

        // 获取玩家所有的装备并检查是否有附魔
        for (ItemStack item : player.getArmorSlots()) {
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ANGER.get(), item) > 0) {
                enchantedItems.add(item);
            }
        }

        // 获取玩家主手物品
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ANGER.get(), player.getMainHandItem()) > 0) {
            enchantedItems.add(player.getMainHandItem());
        }

        return enchantedItems;
    }
}
