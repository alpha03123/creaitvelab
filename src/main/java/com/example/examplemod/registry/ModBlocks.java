package com.example.examplemod.registry;

import com.example.examplemod.ExampleMod;
import com.example.united.selfPulsingRedStoneBlock.SelfPulsingRedStoneBlock;
import com.example.united.reversedPickaxe.VoidBlock;
import net.minecraft.client.Screenshot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    private static final DeferredRegister<Block> blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, ExampleMod.MODID);
    public static final RegistryObject<Block> myblock = blocks.register("myblock", () -> new Block(BlockBehaviour.Properties.of()
            .strength(4.0f)
            .destroyTime(1.0f)));
    public static final RegistryObject<Block> SELF_PULSING_REDSTONE_BlOCK = blocks.register("self_pulsing_redstone_block", SelfPulsingRedStoneBlock::new);
    public static final RegistryObject<Block> VOID_BLOCK =
            blocks.register("void_block",
                    () -> new VoidBlock(BlockBehaviour.Properties.of().strength(0f).noCollission().destroyTime(0F)));


    public static void __init__() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        blocks.register(bus);
        ExampleMod.LOGGER.debug("BLOCKS initialized");

    }

}
