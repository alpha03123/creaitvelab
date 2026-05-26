package com.example.examplemod.enchantment;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.registry.Enchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Repel {
    static long start;


    @SubscribeEvent
    public static void InterAct(PlayerInteractEvent event){
        if(event.getItemStack().getItem() instanceof ShieldItem){
            Player player=event.getEntity();
            start=event.getLevel().getGameTime();
        }

    }
    @SubscribeEvent
    public static void onShieldBlock(ShieldBlockEvent event) {
        Player player = (Player) event.getEntity();
        long end=player.level().getGameTime();
        long duration= end-start;
        ExampleMod.LOGGER.info(String.valueOf(duration));


        int level=EnchantmentHelper.getEnchantmentLevel(Enchantments.REPEL.get(), player);
        // 检查玩家是否装备了附魔Repel的盾牌
        if (level>0) {
            if(duration<22   ){
            Entity attacker = event.getDamageSource().getEntity();
            // 计算玩家视线的反方向作为击退方向
            Vec3 lookVec = attacker.getLookAngle();
            // 计算反方向
            Vec3 knockbackDirection = lookVec.scale(-1);
            double knockbackStrength = 0.5;
            // 应用击退效果
            attacker.setDeltaMovement(attacker.getDeltaMovement().add(knockbackDirection.x *  level*knockbackStrength,
                    knockbackDirection.y *  level*knockbackStrength, knockbackDirection.z *  level*knockbackStrength));
            attacker.hurt(player.damageSources().magic(), (float) (2+Math.pow(2,level)));
            if(level==5&&attacker instanceof LivingEntity){
                float health=((LivingEntity) attacker).getHealth();
                attacker.hurt(player.damageSources().magic(),health*0.2f);

            }

            ExampleMod.LOGGER.info("BLOCKED");
            player.stopUsingItem();

            }

        else {
            ExampleMod.LOGGER.info("FAILED BLOCKED");
            event.setCanceled(true);
            player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 15*level);
                player.getCooldowns().addCooldown(player.getOffhandItem().getItem(), 15*level);
        }

            }

        }
    }
