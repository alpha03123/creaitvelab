package com.example.examplemod.items.mystery_assistant;

import com.example.examplemod.ExampleMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class MysteryAssistantWeaponChaos {
    public static final String KEY = MysteryAssistantModes.WEAPON_CHAOS;
    public static final String DAMAGE_TAG = "examplemod_mystery_damage";

    private static final int MIN_DAMAGE = -200;
    private static final int MAX_DAMAGE = 100;

    private MysteryAssistantWeaponChaos() {
    }

    public static void enable(Player player) {
        MysteryAssistantModes.enable(player, KEY);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide) {
            return;
        }

        Entity sourceEntity = event.getSource().getEntity();
        if (!(sourceEntity instanceof Player player) || !MysteryAssistantModes.ensureReady(player, KEY)) {
            return;
        }

        ItemStack weapon = player.getMainHandItem();
        if (weapon.isEmpty()) {
            return;
        }

        int mysteryDamage = getOrCreateMysteryDamage(player, weapon);
        if (mysteryDamage > 0) {
            event.setAmount(event.getAmount() + mysteryDamage);
            MysteryAssistantModes.recordTrigger(player, KEY, weaponSuccessMessage(weapon, mysteryDamage));
            return;
        }

        if (mysteryDamage < 0) {
            target.heal(-mysteryDamage);
            event.setAmount(0.0F);
            MysteryAssistantModes.recordTrigger(player, KEY, weaponSuccessMessage(weapon, mysteryDamage));
            return;
        }

        MysteryAssistantModes.recordTrigger(player, KEY, weaponSuccessMessage(weapon, mysteryDamage));
    }

    private static int getOrCreateMysteryDamage(Player player, ItemStack weapon) {
        CompoundTag tag = weapon.getOrCreateTag();
        if (!tag.contains(DAMAGE_TAG, Tag.TAG_INT)) {
            tag.putInt(DAMAGE_TAG, randomDamage(player));
        }
        return tag.getInt(DAMAGE_TAG);
    }

    private static int randomDamage(Player player) {
        return player.getRandom().nextInt(MAX_DAMAGE - MIN_DAMAGE + 1) + MIN_DAMAGE;
    }

    private static String weaponSuccessMessage(ItemStack weapon, int mysteryDamage) {
        return "你可说的太对了!" + weapon.getHoverName().getString() + "的伤害一定是" + mysteryDamage + "，这次一定不会错了!";
    }
}
