package com.example.examplemod.items.uncompleted;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.registry.Moditems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.TickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RechargeableRifle {
    private static final int PARTICLE_DURATION = 40; // Duration in ticks (2 seconds)
    private static final double BEAM_LENGTH = 70; // Length of the beam
    private static final double BEAM_RADIUS = 0.5; // Radius of the beam

    private static final Map<Player, Long> particleBeamStartTimes = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        Level world = player.level();
        ItemStack itemStack = event.getItemStack();

        if (itemStack.getItem() == Moditems.RechargeableRifle.get()) {
            // Start particle beam and track start time
            particleBeamStartTimes.put(player, System.currentTimeMillis());
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            long currentTime = System.currentTimeMillis();
            for (Map.Entry<Player, Long> entry : particleBeamStartTimes.entrySet()) {
                Player player = entry.getKey();
                long startTime = entry.getValue();
                if (currentTime - startTime < PARTICLE_DURATION * 50) {
                    spawnParticleBeam(player.level(), player.getEyePosition(), player.getViewVector(1.0F), BEAM_LENGTH);
                } else {
                    // After duration ends, deal damage and release a one-time particle effect
                    dealDamageInBeam(player.level(), player, player.getEyePosition(), player.getViewVector(1.0F), BEAM_LENGTH);
                    spawnEndParticleEffect(player.level(), player.getEyePosition(), player.getViewVector(1.0F), BEAM_LENGTH);
                    particleBeamStartTimes.remove(player);
                }
            }
        }
    }

    private static void spawnParticleBeam(Level world, Vec3 start, Vec3 direction, double length) {
        Random random = new Random();
        int numParticles = 200; // Total number of particles

        double spiralRadius = 0.2; // Radius of the spiral ring
        int spiralTurns = 3; // Number of turns in the spiral

        // Generate straight particles
        for (int i = 0; i < numParticles / 2; i++) {
            double progress = (double) i / ((numParticles / 2) - 1);
            Vec3 particlePos = start.add(direction.scale(progress * length));

            // Randomize the particle offsets for a more natural look
            double offsetX = (random.nextDouble() - 0.5) * 0.1;
            double offsetY = (random.nextDouble() - 0.5) * 0.1;
            double offsetZ = (random.nextDouble() - 0.5) * 0.1;

            // Spawn the straight particle
            world.addParticle(
                    ParticleTypes.FALLING_LAVA,
                    particlePos.x + offsetX,
                    particlePos.y + offsetY,
                    particlePos.z + offsetZ,
                    0, 0, 0
            );
        }

        // Generate spiral particles around the straight beam
        for (int i = 0; i < numParticles / 2; i++) {
            double progress = (double) i / ((numParticles / 2) - 1);
            Vec3 particlePos = start.add(direction.scale(progress * length));

            // Spiral effect around the beam
            double angle = progress * 2 * Math.PI * spiralTurns; // Spiral turns
            double offsetX = Math.cos(angle) * spiralRadius;
            double offsetY = (random.nextDouble() - 0.5) * 0.2; // Vary Y to give more depth
            double offsetZ = Math.sin(angle) * spiralRadius;

            // Randomize the particle offsets for a more natural look
            double randomOffsetX = (random.nextDouble() - 0.5) * 0.1;
            double randomOffsetY = (random.nextDouble() - 0.5) * 0.1;
            double randomOffsetZ = (random.nextDouble() - 0.5) * 0.1;

            // Spawn the spiral particle
            world.addParticle(
                    ParticleTypes.FALLING_LAVA,
                    particlePos.x + offsetX + randomOffsetX,
                    particlePos.y + offsetY + randomOffsetY,
                    particlePos.z + offsetZ + randomOffsetZ,
                    0, 0, 0
            );
        }
    }

    private static void spawnEndParticleEffect(Level world, Vec3 start, Vec3 direction, double length) {
        Vec3 end = start.add(direction.scale(length));
        Random random = new Random();

        int numParticles = 50; // Number of end particles
        for (int i = 0; i < numParticles; i++) {
            double progress = (double) i / (numParticles - 1);
            Vec3 particlePos = start.add(direction.scale(progress * length));

            // Randomize the particle offsets for a more natural look
            double offsetX = (random.nextDouble() - 0.5) * 0.3;
            double offsetY = (random.nextDouble() - 0.5) * 0.3;
            double offsetZ = (random.nextDouble() - 0.5) * 0.3;

            // Spawn the end particle
            world.addParticle(
                    ParticleTypes.EGG_CRACK,
                    particlePos.x + offsetX,
                    particlePos.y + offsetY,
                    particlePos.z + offsetZ,
                    0, 0, 0
            );
        }
    }

    private static void dealDamageInBeam(Level world, Player player, Vec3 start, Vec3 direction, double length) {
        Vec3 end = start.add(direction.scale(length));
        AABB boundingBox = new AABB(start, end);

        for (Entity entity : world.getEntitiesOfClass(LivingEntity.class, boundingBox)) {
            if (entity != player) { // Skip the player
                double distance = start.distanceTo(entity.position());
                if (distance <= length) {
                    // Damage the entity
//                    ((LivingEntity) entity).hurt(ExampleMod.DAMAGE_SOURCE, 10.0F); // Change damage value as needed
                    break; // Only damage the first entity encountered
                }
            }
        }
    }
}
