package com.example.examplemod;
import com.example.examplemod.registry.*;
import com.example.particlecomplex.registry.ModParticleType;


import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;



// The value here should match an entry in the META-INF/mods.toml file

@Mod(ExampleMod.MODID)
@Mod.EventBusSubscriber(modid = ExampleMod.MODID)
public class ExampleMod
{

    // Define mod id in a common place for everything to reference
    public static final String MODID = "examplemod";
    // Directly reference a slf4j logger
    public static final Logger LOGGER =LogUtils.getLogger();
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =DeferredRegister.create(Registries.CREATIVE_MODE_TAB,MODID);
    private static final DeferredRegister<Item> ITEMS =DeferredRegister.create(ForgeRegistries.ITEMS,MODID);
    private static final DeferredRegister<Block> BLOCKS =DeferredRegister.create(ForgeRegistries.BLOCKS,MODID);
    public static final RegistryObject<CreativeModeTab> mytab =CREATIVE_MODE_TABS.register("mytab",()-> CreativeModeTab.builder()
            .title(Component.translatable("item.examplemod.python"))
            .icon(()->new ItemStack(Moditems.python.get()))
            .displayItems((parm,output)->{
                output.accept(Moditems.python.get());
                output.accept(Moditems.DaedalusStormBow.get());
                output.accept(Moditems.TargetItem.get());
                output.accept(Moditems.RechargeableRifle.get());
                output.accept(Moditems.BigRedEnvelope.get());
                output.accept(Moditems.SmallRedEnvelope.get());
                output.accept(Moditems.PulsePotionI.get());
                output.accept(Moditems.PulsePotionIII.get());
                output.accept(Moditems.PulsePotionV.get());
                output.accept(Moditems.SplashPulsePotionI.get());
                output.accept(Moditems.SplashPulsePotionI.get());
                output.accept(Moditems.SplashPulsePotionV.get());
                output.accept(Moditems.SelfPulsingRedStoneBlock.get());
                output.accept(Moditems.PulseBow.get());
                output.accept(Moditems.MeteoriteItem.get());
                output.accept(Moditems.TEST.get());
                output.accept(Moditems.MISSILE_TARGET.get());
                output.accept(Moditems.RAIL_GUN.get());
            })
            .build());

    public ExampleMod(){
        var bus= FMLJavaModLoadingContext.get().getModEventBus();
        Moditems.__init__();
        ModBlocks.__init__();
        Enchantments.__init__();
        ModEntities.__init__();
        bus.addListener(this::onClientSetup);
//        ModEntityRenderer.__init__();
//        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        ModEffects.register(bus);
        ModBlockEntities.register();
        ModParticleType.register(bus);
        ModPots.register();
        ModSounds.register();
        CREATIVE_MODE_TABS.register(bus);
    }
//    @SubscribeEvent
    private void onClientSetup(FMLClientSetupEvent event) {
//        event.enqueueWork(NetworkHandler::register);
    }




}
