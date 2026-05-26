package com.example.examplemod.commands;

import com.example.examplemod.entities.Turrets.PredictTurret;
import com.example.examplemod.entities.Turrets.Turret;
import com.example.examplemod.registry.ModEntities;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public class Test {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("testE").then(Commands.argument("test_string", StringArgumentType.string()).executes(Test::addTurret)));
    }
    private static int addTurret(CommandContext<CommandSourceStack> context) {
        String spell=StringArgumentType.getString(context,"test_string");
        Entity entity= context.getSource().getEntity();
        if(!(entity instanceof Player player))return 0;
        if(Objects.equals(spell, "addTurret")){
            Turret turret=new Turret(ModEntities.BASE_COMPLEX_ENTITY.get(),player.level(),player.position());
            entity.level().addFreshEntity(turret);
            context.getSource().sendSuccess(() -> Component.literal("success"), true);
        }
        if(Objects.equals(spell, "addPreTurret")){
            PredictTurret turret=new PredictTurret(ModEntities.BASE_COMPLEX_ENTITY.get(),player.level(),player.position());
            entity.level().addFreshEntity(turret);
            context.getSource().sendSuccess(() -> Component.literal("success"), true);
        }
//        if(Objects.equals(spell, "screenTest")){
//            BranchSatelliteEntity entity1=new BranchSatelliteEntity(ModEntities.BASE_COMPLEX_ENTITY.get(),entity.level());
//            entity1.init((Player) entity);
//            entity1.setPos(entity.position());
//            entity.level().addFreshEntity(entity1);
//            context.getSource().sendSuccess(() -> Component.literal("success"), true);
//        }
        else {
            context.getSource().sendFailure(Component.literal("failure"));
        }
        return 1;
    }
}
