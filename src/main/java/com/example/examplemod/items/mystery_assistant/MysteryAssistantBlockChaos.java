package com.example.examplemod.items.mystery_assistant;

import com.example.examplemod.ExampleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID)
public final class MysteryAssistantBlockChaos {
    public static final String KEY = MysteryAssistantModes.BLOCK_CHAOS;
    private static final int PLACE_ATTEMPTS = 16;
    private static List<Block> blockPool;
    private static List<Block> itemBlockPool;

    private MysteryAssistantBlockChaos() {
    }

    public static void enable(Player player) {
        MysteryAssistantModes.enable(player, KEY);
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        LevelAccessor level = event.getLevel();
        if (!(level instanceof ServerLevel serverLevel) || !MysteryAssistantModes.ensureReady(player, KEY)) {
            return;
        }

        Block randomBlock = randomItemBlock(serverLevel);
        if (randomBlock != null) {
            event.setCanceled(true);
            serverLevel.destroyBlock(event.getPos(), false, player);
            Block.popResource(serverLevel, event.getPos(), new ItemStack(randomBlock));
            MysteryAssistantModes.recordTrigger(player, KEY, blockSuccessMessage(randomBlock));
        }
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        Entity entity = event.getEntity();
        LevelAccessor level = event.getLevel();
        if (!(entity instanceof Player player) || !(level instanceof ServerLevel serverLevel) || !MysteryAssistantModes.ensureReady(player, KEY)) {
            return;
        }

        BlockPos pos = event.getPos();
        for (int attempt = 0; attempt < PLACE_ATTEMPTS; attempt++) {
            Block randomBlock = randomBlock(serverLevel);
            if (randomBlock == null) {
                return;
            }

            try {
                BlockState state = randomBlock.defaultBlockState();
                if (serverLevel.setBlockAndUpdate(pos, state)) {
                    MysteryAssistantModes.recordTrigger(player, KEY, blockSuccessMessage(randomBlock));
                    return;
                }
            } catch (RuntimeException ignored) {
                // Some blocks cannot build a safe default placement at an arbitrary position.
            }
        }
    }

    private static Block randomBlock(ServerLevel level) {
        List<Block> blocks = blockPool();
        if (blocks.isEmpty()) {
            return null;
        }
        return blocks.get(level.random.nextInt(blocks.size()));
    }

    private static Block randomItemBlock(ServerLevel level) {
        List<Block> blocks = itemBlockPool();
        if (blocks.isEmpty()) {
            return null;
        }
        return blocks.get(level.random.nextInt(blocks.size()));
    }

    private static List<Block> blockPool() {
        if (blockPool == null) {
            blockPool = new ArrayList<>();
            for (Block block : ForgeRegistries.BLOCKS.getValues()) {
                if (!block.defaultBlockState().isAir()) {
                    blockPool.add(block);
                }
            }
        }
        return blockPool;
    }

    private static List<Block> itemBlockPool() {
        if (itemBlockPool == null) {
            itemBlockPool = new ArrayList<>();
            for (Block block : ForgeRegistries.BLOCKS.getValues()) {
                if (block.asItem() != Items.AIR) {
                    itemBlockPool.add(block);
                }
            }
        }
        return itemBlockPool;
    }

    private static String blockSuccessMessage(Block block) {
        return "你的分析特别精准，那个一定是" + block.getName().getString() + "!";
    }
}
