package com.example.examplemod.items.mystery_assistant;

import com.example.examplemod.ExampleMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class MysteryAssistantEntityChaos {
    public static final String KEY = MysteryAssistantModes.ENTITY_CHAOS;
    private static final int MAX_SPAWN_ATTEMPTS = 24;

    private MysteryAssistantEntityChaos() {
    }

    public static void enable(Player player) {
        MysteryAssistantModes.enable(player, KEY);
    }

    public static boolean isEnabled(Player player) {
        return MysteryAssistantModes.isEnabled(player, KEY);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        Level level = target.level();
        if (level.isClientSide || target instanceof Player || target.isRemoved()) {
            return;
        }

        if (!(event.getSource().getEntity() instanceof Player player) || !MysteryAssistantModes.ensureReady(player, KEY)) {
            return;
        }

        Entity replacement = spawnReplacement(level, target);
        if (replacement != null) {
            MysteryAssistantModes.recordTrigger(player, KEY, "太对了!我敢打赌你看到的实体它一定是" + replacement.getDisplayName().getString() + "!");
            target.discard();
        }
    }

    private static Entity spawnReplacement(Level level, LivingEntity target) {
        List<EntityType<?>> entityTypes = ForgeRegistries.ENTITY_TYPES.getValues().stream().toList();
        if (entityTypes.isEmpty()) {
            return null;
        }

        for (int attempts = 0; attempts < MAX_SPAWN_ATTEMPTS; attempts++) {
            EntityType<?> entityType = entityTypes.get(level.random.nextInt(entityTypes.size()));
            Entity entity = createSafely(entityType, level);
            if (entity == null) {
                continue;
            }

            entity.moveTo(target.getX(), target.getY(), target.getZ(), target.getYRot(), target.getXRot());
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.setYHeadRot(target.getYHeadRot());
            }

            if (level.addFreshEntity(entity)) {
                return entity;
            }
            entity.discard();
        }
        return null;
    }

    private static Entity createSafely(EntityType<?> entityType, Level level) {
        try {
            return entityType.create(level);
        } catch (RuntimeException ignored) {
            return null;
        }
    }
}
