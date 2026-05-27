package com.example.examplemod.registry;

import com.example.examplemod.ExampleMod;
import com.example.united.WASP.WaspLauncherItem;
import com.example.united.reversedPickaxe.PickAxe;
import com.example.united.reversedPickaxe.AxePick;
import com.example.examplemod.items.*;
import com.example.examplemod.items.mystery_assistant.MysteryAssistantItem;

import com.example.united.Kg500.Kg500;
import com.example.examplemod.items.potions.pulse.PulsePotionI;
import com.example.examplemod.items.potions.pulse.PulsePotionIII;
import com.example.examplemod.items.potions.pulse.PulsePotionV;
import com.example.examplemod.items.splash_potions.pulse.SplashPulsePotionI;
import com.example.examplemod.items.splash_potions.pulse.SplashPulsePotionIII;
import com.example.examplemod.items.splash_potions.pulse.SplashPulsePotionV;
import com.example.united.daedalusStormBow.DaedalusStormBow;
import com.example.united.pulseBow.PulseBow;
import net.minecraft.world.item.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Moditems {
    private static final DeferredRegister<Item> items = DeferredRegister.create(ForgeRegistries.ITEMS, ExampleMod.MODID);
    public static final RegistryObject<Item> python = items.register("python", () -> new Item(new Item.Properties()
            .rarity(Rarity.EPIC)
            .stacksTo(1)));


    public static final RegistryObject<Item> RechargeableRifle =items.register("rechargeable_rifle",()->new Item(new Item.Properties()));


    public static final RegistryObject<Item> DaedalusStormBow = items.register("daedalus_storm_bow", () -> new DaedalusStormBow(new Item.Properties()));

    public static final RegistryObject<Item> SmallRedEnvelope = items.register("small_red_envelope", com.example.examplemod.items.red_envelope.SmallRedEnvelope::new);


    public static final RegistryObject<Item> BigRedEnvelope = items.register("big_red_envelope", com.example.examplemod.items.red_envelope.BigRedEnvelope::new);
    public static final  RegistryObject<Item> SelfPulsingRedStoneBlock=items.register("selfpulsingredstoneblock",()->new BlockItem(ModBlocks.SELF_PULSING_REDSTONE_BlOCK.get(),new Item.Properties()
            .rarity(Rarity.COMMON).stacksTo(64)));
    public static final  RegistryObject<Item> TargetItem=items.register("target_item",()->new Item(new Item.Properties()));

    public static final  RegistryObject<Item> PulsePotionI=items.register("pulse_potion_1",()->new PulsePotionI(new Item.Properties()));
    public static final  RegistryObject<Item> PulsePotionIII=items.register("pulse_potion_3",()->new PulsePotionIII(new Item.Properties()));
    public static final  RegistryObject<Item> PulsePotionV=items.register("pulse_potion_5",()->new PulsePotionV(new Item.Properties()));
    public static final  RegistryObject<Item> SplashPulsePotionI=items.register("splash_pulse_potion_1",()->new SplashPulsePotionI(new Item.Properties()));
    public static final  RegistryObject<Item> SplashPulsePotionIII=items.register("splash_pulse_potion_3",()->new SplashPulsePotionIII(new Item.Properties()));

    public static final  RegistryObject<Item> SplashPulsePotionV=items.register("splash_pulse_potion_5",()->new SplashPulsePotionV(new Item.Properties()));
    public static final  RegistryObject<Item> PulseBow=items.register("pulsebow",()->new PulseBow(new Item.Properties()));
    public static final  RegistryObject<Item> MeteoriteItem=items.register("meteorite",()->new Item(new Item.Properties()));
    public static final  RegistryObject<Item> TEST=items.register("test",()->new Item(new Item.Properties()));

    public static final  RegistryObject<Item> RAIL_GUN =items.register("rail_gun",()->new Item(new Item.Properties()));

    public static final RegistryObject<Item> INS_MUSHROOM = items.register("ins_mushroom", InsMushroom::new);
    public static final  RegistryObject<Item> MISSILE_TARGET =items.register("missile_target",()->new MissileItem(new Item.Properties()));
    public static final RegistryObject<Item> KG_500 = items.register("kg_500", () -> new Kg500(new Item.Properties()));
    public static final RegistryObject<Item> HA=items.register("ha",()->new Ha(new Item.Properties()));
    public static final RegistryObject<Item> WASP=items.register("wasp",()->new WaspLauncherItem(new Item.Properties()));
    public static final RegistryObject<Item> PICK_AXE=items.register("pickaxe",()->new PickAxe(new Item.Properties()));
    public static final RegistryObject<Item> AXE_PICK=items.register("axepick",()->new AxePick(new Item.Properties()));
    public static final RegistryObject<Item> GUESS_DISEASE =items.register("guess_disease",()->new GuessDisease(new Item.Properties()));
    public static final RegistryObject<Item> MYSTERY_ASSISTANT = items.register("mystery_assistant", () -> new MysteryAssistantItem(new Item.Properties()));

    public static void __init__() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        items.register(bus);
        ExampleMod.LOGGER.debug("items initializeD");
    }

}
