package com.example.examplemod.advanced_functions;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Map;

public class EnchantmentsUtils {
    public static int getStackEnchantLevel(ItemStack stack, Enchantment enchantment){
        Map<Enchantment, Integer> enchantmentsLevelMap= EnchantmentHelper.getEnchantments(stack);
        for(Enchantment enchantment1:enchantmentsLevelMap.keySet()){
            if(enchantment1 == enchantment){
                return enchantmentsLevelMap.get(enchantment1);
            }
        }
        return 0;
    }
}
