package com.example.examplemod.registry;

import com.example.examplemod.ExampleMod;
import com.example.united.WASP.WaspMissileEntity;
import com.example.examplemod.entities.AmmoEntity;
import com.example.examplemod.entities.AreaEntity;
import com.example.examplemod.entities.BaseComplexEntity;

import com.example.united.daedalusStormBow.DaedalusStormBowArrow;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ExampleMod.MODID);

    public static final RegistryObject<EntityType<AmmoEntity>> AMMO =
            ENTITY_TYPES.register("ammo", () -> EntityType.Builder.<AmmoEntity>of(AmmoEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .build("ammo"));

    public static final RegistryObject<EntityType<AreaEntity>> AREA =
            ENTITY_TYPES.register("area", () -> EntityType.Builder.<AreaEntity>of((pEntityType, pLevel) -> new AreaEntity(pEntityType,pLevel,new Vec3(0,0,0)), MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .build("area"));

    public static final RegistryObject<EntityType<DaedalusStormBowArrow>> CUSTOM_ARROW = ENTITY_TYPES.register("custom_arrow",
            () -> EntityType.Builder.<DaedalusStormBowArrow>of(DaedalusStormBowArrow::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .build("custom_arrow"));

    public static final RegistryObject<EntityType<BaseComplexEntity>> BASE_COMPLEX_ENTITY =
            ENTITY_TYPES.register("base_complex", () -> EntityType.Builder.<BaseComplexEntity>of((pEntityType, pLevel)
                            -> new BaseComplexEntity(pEntityType,pLevel,new Vec3(0,0,0),new Vec3(0,0,0)), MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .build("base_complex"));
    public static final RegistryObject<EntityType<WaspMissileEntity>> WASP_MISSILE =
            ENTITY_TYPES.register("wasp_missile", () ->
                    EntityType.Builder.<WaspMissileEntity>of(WaspMissileEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f) // 实体大小
                            .clientTrackingRange(64)
                            .updateInterval(1)
                            .build("wasp_missile")
            );













    public static void __init__() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ENTITY_TYPES.register(modEventBus);
        ExampleMod.LOGGER.debug("ENTITIES initialized");
    }
}
