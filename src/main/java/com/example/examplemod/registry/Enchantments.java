package com.example.examplemod.registry;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.enchantment.Ha;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Enchantments {
    private static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ExampleMod.MODID);

    public static final RegistryObject<Enchantment> HOT_BRICK = ENCHANTMENTS.register("hot_brick",
            () -> new Enchantment(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET}) {
                @Override
                public int getMinCost(int enchantmentLevel) {
                    return 1;
                }

                @Override
                public int getMaxCost(int enchantmentLevel) {
                    return 10;
                }

                @Override
                public int getMaxLevel() {
                    return 5;
                }
            });

    public static final RegistryObject<Enchantment> REPEL = ENCHANTMENTS.register("repel",
            () -> new Enchantment(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.ARMOR, new EquipmentSlot[]{EquipmentSlot.OFFHAND, EquipmentSlot.MAINHAND}) {
                @Override
                public int getMinCost(int enchantmentLevel) {
                    return 1;
                }

                @Override
                public int getMaxCost(int enchantmentLevel) {
                    return 10;
                }

                @Override
                public int getMaxLevel() {
                    return 5;
                }

                @Override
                public boolean canEnchant(ItemStack pStack) {
                    return pStack.getItem() instanceof ArmorItem;
                }
            });

    public static final RegistryObject<Enchantment> RED_BUFFS = ENCHANTMENTS.register("redbuffs",
            () -> new Enchantment(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.BREAKABLE, new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
                @Override
                public int getMinCost(int enchantmentLevel) {
                    return 1;
                }

                @Override
                public int getMaxCost(int enchantmentLevel) {
                    return 10;
                }

                @Override
                public int getMaxLevel() {
                    return 5;
                }

                @Override
                public boolean canEnchant(ItemStack pStack) {
                    return pStack.getItem() instanceof ArmorItem;
                }

                @Override
                public boolean isCurse() {
                    return true; // 标记为诅咒附魔
                }
            });

    public static final RegistryObject<Enchantment> ANGER = ENCHANTMENTS.register("anger", () ->
            new Enchantment(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR,
                    new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {

                @Override
                public int getMinCost(int enchantmentLevel) {
                    return 5 + (enchantmentLevel - 1) * 10;
                }

                @Override
                public int getMaxCost(int enchantmentLevel) {
                    return super.getMinCost(enchantmentLevel) + 50;
                }

                @Override
                public int getMaxLevel() {
                    return 20;
                }

                @Override
                public boolean canEnchant(ItemStack pStack) {
                    return pStack.canApplyAtEnchantingTable(this);
                }
            }
    );
    public static final RegistryObject<Enchantment> DEEP_SEEK = ENCHANTMENTS.register("deep_seek", () ->
            new Enchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND}) {

                @Override
                public int getMinCost(int enchantmentLevel) {
                    return 5 + (enchantmentLevel - 1) * 10;
                }

                @Override
                public int getMaxCost(int enchantmentLevel) {
                    return super.getMinCost(enchantmentLevel) + 50;
                }

                @Override
                public int getMaxLevel() {
                    return 5;  // 可以设置最大等级为5
                }

                @Override
                public boolean canEnchant(ItemStack pStack) {
                    // 判断该附魔是否可以应用于特定物品
                    // 允许附魔在剑、镐、盔甲上
                    return pStack.canApplyAtEnchantingTable(this);
                }
            }
    );
    public static final RegistryObject<Enchantment> OIIAII = ENCHANTMENTS.register("oiiaii", () ->
            new Enchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND}) {

                @Override
                public int getMinCost(int enchantmentLevel) {
                    return 5 + (enchantmentLevel - 1) * 10;
                }

                @Override
                public int getMaxCost(int enchantmentLevel) {
                    return super.getMinCost(enchantmentLevel) + 50;
                }

                @Override
                public int getMaxLevel() {
                    return 5;  // 可以设置最大等级为5
                }

                @Override
                public boolean canEnchant(ItemStack pStack) {
                    // 判断该附魔是否可以应用于特定物品
                    // 允许附魔在剑、镐、盔甲上
                    return true;
                }
            }
    );
    public static final RegistryObject<Enchantment> Tung = ENCHANTMENTS.register("tung", () ->
            new Enchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND}) {

                @Override
                public int getMinCost(int enchantmentLevel) {
                    return 5 + (enchantmentLevel - 1) * 10;
                }

                @Override
                public int getMaxCost(int enchantmentLevel) {
                    return super.getMinCost(enchantmentLevel) + 50;
                }

                @Override
                public int getMaxLevel() {
                    return 5;  // 可以设置最大等级为5
                }

            }
    );
    public static final RegistryObject<Enchantment> BombBard = ENCHANTMENTS.register("bombbard", () ->
            new Enchantment(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.BOW, new EquipmentSlot[] { EquipmentSlot.MAINHAND }) {

                @Override
                public int getMinCost(int enchantmentLevel) {
                    return 5 + (enchantmentLevel - 1) * 10;
                }

                @Override
                public int getMaxCost(int enchantmentLevel) {
                    return super.getMinCost(enchantmentLevel) + 50;
                }

                @Override
                public int getMaxLevel() {
                    return 5;  // 可以设置最大等级为5
                }

            }
    );
    public static final RegistryObject<Enchantment> HA = ENCHANTMENTS.register("ha", Ha::new
    );


    public static void __init__() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        ENCHANTMENTS.register(bus);
        ExampleMod.LOGGER.debug("ENCHANTMENTS initialized");
    }
}
