package com.example.united.mapTest;

import com.example.examplemod.entities.BaseComplexEntity;
import com.example.examplemod.entities.Missile;
import com.example.examplemod.registry.ModEntities;
import com.example.united.mapTest.helldiver_entities.OrbitalPrecisionStrikeEntity;
import com.example.united.mapTest.helldiver_entities.PlaneEntity;
import com.example.united.pulseBow.PulseArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Random;

//{
//    "typeA": {
//        "name": "侦察无人机",
//        "initialUses": 3,
//        "cooldown": 30000,
//        "shortCooldown": 5000,
//        "description": "发射一架无人机侦察目标区域。",
//        "iconPath": "/static/images/equipment/a.png"
//    },
//    "typeB": {
//        "name": "烟雾弹",
//        "initialUses": 2,
//        "cooldown": 60000,
//        "shortCooldown": 10000,
//        "description": "在目标点制造烟雾。",
//        "iconPath": "/static/images/equipment/a.png"
//    },
//    "typeC": {
//        "name": "轨道精准打击",
//        "initialUses": 2,
//        "cooldown": 10000,
//        "shortCooldown": 10000,
//        "description": "由阿特拉斯驱逐舰扔下的绝对精准的导弹。",
//        "iconPath": "/static/images/equipment/a.png"
//    },
//    "typeD": {
//        "name": "飞鹰空袭",
//        "initialUses": 2,
//        "cooldown": 10000,
//        "shortCooldown": 10000,
//        "description": "由阿特拉斯驱逐舰扔下的绝对精准的导弹。",
//        "iconPath": "/static/images/equipment/a.png"
//    },
//    "typeE": {
//        "name": "飞鹰集束",
//        "initialUses": 2,
//        "cooldown": 10000,
//        "shortCooldown": 10000,
//        "description": "由阿特拉斯驱逐舰扔下的绝对精准的导弹。",
//         "iconPath": "/static/images/equipment/a.png"
//    },
//    "typeF": {
//        "name": "轨道毒气",
//        "initialUses": 2,
//        "cooldown": 10000,
//        "shortCooldown": 10000,
//        "description": "由阿特拉斯驱逐舰扔下的绝对精准的导弹。",
//         "iconPath": "/static/images/equipment/a.png"
//    },
//    "typeG": {
//        "name": "轨道380",
//        "initialUses": 2,
//        "cooldown": 10000,
//        "shortCooldown": 10000,
//        "description": "由阿特拉斯驱逐舰扔下的绝对精准的导弹。",
//         "iconPath": "/static/images/equipment/a.png"
//    }
//}
public class handleStrategies {
    public static ArrayList<PlaneEntity> planeEntities=new ArrayList<>();
    public static void handleAllStrategies(Level level,String strategyType, int tx1, int tz1, int tx2, int tz2){
        if(tx2==Integer.MAX_VALUE || tz2== Integer.MAX_VALUE){
            tx2=new Random().nextInt(-1000,1000);
            tz2=new Random().nextInt(-1000,1000);
        }
        handleCommon(level,strategyType,tx1,tz1,tx2,tz2);
    }
    public static void handleCommon(Level level,String strategyType, int tx1, int tz1, int tx2, int tz2){
        int offY=60;
        int y = level.getHeight(Heightmap.Types.WORLD_SURFACE,tx1,tz1)+offY;
        switch (strategyType){
            case "typeA": {
                PlaneEntity entity=new PlaneEntity(level,new Vec3(tx1,y,tz1),new Vec3(tx2-tx1,0,tz2-tz1).normalize());
                level.addFreshEntity(entity);
                planeEntities.add(entity);
            }
            case "typeC": {
                OrbitalPrecisionStrikeEntity entity=new OrbitalPrecisionStrikeEntity(level,new Vec3(tx1,y,tz1));
                level.addFreshEntity(entity);
            }
            case "typeD":
//                handleOrbitalEagleStrike();
            case "typeE":
//                handleEagleClusterBomb();
            case "typeF":
//                handleOrbitalGasStrike();
            case "typeG":
//                handleOrbital380Strike();

        }
    }



}
