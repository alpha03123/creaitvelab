package com.example.examplemod.enchantment;


import com.example.examplemod.ExampleMod;
import com.example.examplemod.registry.Enchantments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HotBrick {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        int level=EnchantmentHelper.getEnchantmentLevel(Enchantments.HOT_BRICK.get(), entity);

        // 检查实体是否有附魔a1
        if (level > 0) {
            // 获取实体视线方向
            Vec3 lookVec = entity.getLookAngle();
            // 计算反方向
            Vec3 knockbackDirection = lookVec.scale(-1);
            // 设置击退力度
            // 应用击退效果
            entity.setDeltaMovement(entity.getDeltaMovement().add(knockbackDirection.x *  level, knockbackDirection.y * (double) level, knockbackDirection.z * (double) level));
        }
    }
}
