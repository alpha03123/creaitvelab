package com.example.examplemod.commands;

import com.example.examplemod.registry.Enchantments;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Collections;
import java.util.Objects;

public class Cast {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("cast").then(Commands.argument("spell", StringArgumentType.string()).executes(Cast::addCurveWithEntitiesAndPos)));
    }
    private static int addCurveWithEntitiesAndPos(CommandContext<CommandSourceStack> context) {
        String spell=StringArgumentType.getString(context,"spell");
        Entity entity= context.getSource().getEntity();
        if(!(entity instanceof Player player))return 0;
        if(Objects.equals(spell, "tung tung tung sahur")&&player.getMainHandItem().getItem() instanceof SwordItem){
            EnchantmentHelper.setEnchantments(Collections.singletonMap
                    (Enchantments.Tung.get(), 2),player.getMainHandItem());
            context.getSource().sendSuccess(() -> Component.literal("success"), true);
        }
        else if (Objects.equals(spell, "bombardino crocodilo")&&player.getMainHandItem().getItem() instanceof BowItem){
            EnchantmentHelper.setEnchantments(Collections.singletonMap
                    (Enchantments.BombBard.get(), 1),player.getMainHandItem());
            context.getSource().sendSuccess(() -> Component.literal("success"), true);
        }
        else if (Objects.equals(spell, "tung tung tung tung tung tung tung tung tung tung sahur")&&player.getMainHandItem().getItem() instanceof SwordItem){
            EnchantmentHelper.setEnchantments(Collections.singletonMap
                    (Enchantments.Tung.get(), 5),player.getMainHandItem());
            context.getSource().sendSuccess(() -> Component.literal("success"), true);}


        else {
            context.getSource().sendFailure(Component.literal("failure"));
        }
        return 1;
    }
}
