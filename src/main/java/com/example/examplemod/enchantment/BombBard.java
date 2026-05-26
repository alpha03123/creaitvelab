package com.example.examplemod.enchantment;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.registry.Enchantments;
import com.example.particlecomplex.particles.base.ParticleAreaSpawner;
import com.example.particlecomplex.particles.custom.END_ROD;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.item.BowItem;

import java.util.Collections;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BombBard {
    private static long startTime = -1;  // 记录事件触发的时间
    private static final long DELAY = 100;  // 延迟时间（5秒，单位是tick）
    private static Vec3 pos1 = null;      // 记录玩家的位置

    public static void generateRippleEffect(Level level, Vec3 pos) {
        END_ROD endRod = new END_ROD();
        String i = "(0.1 * sin((t / 100) * (pi / 2))) * (t / 100) * (t / 100)";
        endRod.setVecExpX("-x*" + i);
        endRod.setVecExpY("-y*" + i);
        endRod.setVecExpZ("-z*" + i);
        endRod.setFps(1000);
        endRod.setDiameter(0.5f);
        endRod.setCenter(pos.x, pos.y, pos.z);
        endRod.setLifetime(120);
        endRod.setDynamicExp("w <- threshold(255 * (1 - (t / lifetime)^3)-30)"); // 渐隐
        ParticleAreaSpawner spawner = new ParticleAreaSpawner(level, endRod, -90, 90, 1);
        spawner.setPolarPositionExpression("10", "0", "4*t");
        spawner.createByPolarPositionExpression(pos.x, pos.y, pos.z);
    }


    // 生成末影水晶
    private static void generateEndCrystal(Level level, Vec3 pos) {
        if (level instanceof ServerLevel) {
            // 在 pos 位置生成末影水晶
            // 末影水晶是一个实体，所以需要创建它并放置
            level.addFreshEntity(new EndCrystal(level, pos.x, pos.y, pos.z));
        }
    }

    // 生成闪电
    private static void generateLightning(Level level, Vec3 pos) {
        if (level instanceof ServerLevel) {
            // 在 pos 位置生成闪电
            LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT,level);
            lightningBolt.setPos(pos);
            level.addFreshEntity(lightningBolt);
        }
    }

    @SubscribeEvent
    public static void onArrowLoose(ArrowLooseEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = event.getEntity();
        ItemStack bow = event.getBow();

        // 检查玩家使用的是弓
        if (bow.getItem() instanceof BowItem) {
            // 获取弓上的 BombBard 附魔等级
            int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.BombBard.get(), player);

            if (level > 0) {

                // 记录玩家的位置 pos1
                pos1 = player.position();
                startTime = event.getLevel().getGameTime();  // 记录开始时间
                generateRippleEffect(event.getLevel(),pos1);

                // 附魔逻辑：当附魔等级为1时减少1级
                if (level == 1) {
                    EnchantmentHelper.setEnchantments(Collections.singletonMap(Enchantments.BombBard.get(), 0), bow);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (startTime == -1 || pos1 == null) return;  // 如果没有触发过事件或未记录位置，则跳过

        // 检查是否已过5秒
        long currentTime = event.player.level().getGameTime();
        long countDown=DELAY-(currentTime-startTime);
        if (countDown <= 0) {


            // 执行生成末影水晶和闪电的操作
            generateEndCrystal(event.player.level(), pos1);
            generateLightning(event.player.level(), pos1);

            // 重置startTime，防止再次触发
            startTime = -1;
            pos1 = null;  // 重置记录的位置
        }
    }
}
