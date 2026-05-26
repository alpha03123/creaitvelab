package com.example.examplemod.registry;

import com.example.examplemod.ExampleMod;
import com.example.united.selfPulsingRedStoneBlock.SelfPulsingRedStoneBlockEntity;
import com.example.united.reversedPickaxe.VoidBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = "yourmodid", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "yourmodid");

    public static final Supplier<BlockEntityType<com.example.united.selfPulsingRedStoneBlock.SelfPulsingRedStoneBlockEntity>> SelfPulsingRedStoneBlockEntity =
            BLOCK_ENTITIES.register("self_pulsing_redstone_blockentity", () ->
                    BlockEntityType.Builder.of(SelfPulsingRedStoneBlockEntity::new,
                            ModBlocks.SELF_PULSING_REDSTONE_BlOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<VoidBlockEntity>> VOID_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("void_block_entity", () ->
                    BlockEntityType.Builder.of(VoidBlockEntity::new, ModBlocks.VOID_BLOCK.get()).build(null)
            );

    public static void register() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCK_ENTITIES.register(bus);
        ExampleMod.LOGGER.debug("BLOCKS initialized");
    }
}


