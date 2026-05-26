package com.example.examplemod.enchantment;

import com.example.examplemod.registry.ModEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;

import java.util.List;

public class Ha extends Enchantment {
    public Ha() {
        super(Rarity.RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    public static float getWeaponAttackDamage(ItemStack stack) {
        if (stack.isEmpty()) return 1.0f; // 空手默认伤害
        return (float) stack.getAttributeModifiers(EquipmentSlot.MAINHAND)
                .get(Attributes.ATTACK_DAMAGE)
                .stream()
                .mapToDouble(modifier -> modifier.getAmount())
                .sum();
    }

    @Override
    public void doPostAttack(LivingEntity user, Entity target1, int level) {

        if (level <= 0) return;
        if(!(target1 instanceof LivingEntity target)){return;}
        ItemStack weapon = user.getMainHandItem();
        CompoundTag tag = weapon.getOrCreateTag();

        int count = tag.getInt("ha_counter") + 1;
        System.out.println(count);
        if (count >= 3) {
            count = 0;
            // 造成三倍伤害
            float baseDamage = getWeaponAttackDamage(user.getMainHandItem()); // 可以替换为你计算的伤害值
            target.hurt(user.damageSources().mobAttack(user), baseDamage * (level+1));

            // 添加凋零效果（10秒，等级1）
            target.addEffect(new MobEffectInstance(ModEffects.FRIGHTENED.get(), 200, 0));

            if (!user.level().isClientSide) {
                ServerLevel levelServer = (ServerLevel) user.level();

                levelServer.sendParticles(ParticleTypes.CRIT, target.getX(), target.getY() + 1, target.getZ(), 20, 0.5, 0.5, 0.5, 0.2);
                levelServer.sendParticles(ParticleTypes.ENCHANT, target.getX(), target.getY() + 1, target.getZ(), 20, 0.3, 0.3, 0.3, 0.1);

                List<Mob> mobs = levelServer.getEntitiesOfClass(
                        Mob.class,
                        user.getBoundingBox().inflate(20),
                        mob -> mob != user &&
                                mob.isAlive());


                for (Mob mob : mobs) {
                    System.out.println(mob);
                    mob.setTarget(user);
                }
            }


        }

        tag.putInt("ha_counter", count);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return EnchantmentCategory.WEAPON.canEnchant(stack.getItem());
    }

    @Override
    public boolean isTreasureOnly() {
        return false;
    }

    @Override
    public boolean isCurse() {
        return false;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }
}
