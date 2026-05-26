package com.example.examplemod.items.uncompleted;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.registry.Moditems;
import com.example.particlecomplex.particles.base.BaseParticleType;
import com.example.particlecomplex.particles.base.ParticleAreaSpawner;
import com.example.particlecomplex.particles.custom.END_ROD;
import com.example.particlecomplex.particles.custom.FALLING_LAVA;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector4i;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID)
public class RailGun_Uncompleted {
    @SubscribeEvent
    public static void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
            if (event.getItemStack().getItem() == Moditems.RAIL_GUN.get()) {
            Level level = event.getLevel();
            float x_f = (float) event.getEntity().getX();
            float y_f = (float) event.getEntity().getY();
            float z_f = (float) event.getEntity().getZ();
            if (level.isClientSide) {
                BaseParticleType particleType = new FALLING_LAVA();
                particleType

                        .setLifetime(100)
                        .setColor(new Vector4i(100, 100, 100, 500))
                        .setDiameter(1f)// 设置缩放
                        .setCenter(x_f, y_f, z_f)// 设置中心坐标,x,y,z,e?x,e?y,e?z返回的都是实际实体||粒子坐标-中心坐标,也就是相对位置
//                        .setRotation(1,4,1)
                        .setFps(100)
                        .setVecExpX("0.010*(sRandom)*BezierCurve3(1000,1,0,t/lifetime)")
                        .setVecExpY("0")
                        .setVecExpZ("0")
//                        .setDynamicExp("w<-255*0.5*(1-cos((2*pi*t)/lifetime))") //渐隐渐显
                        .setDynamicExp("w <- threshold(255 * (1 - (t / lifetime)^3)-30)"); //渐隐
                for (int i = 0; i < 3; i++) {
                    ParticleAreaSpawner areaParticle = new ParticleAreaSpawner(level, particleType, -360, 360, (float) (8));
                    areaParticle.setPolarPositionExpression(String.valueOf(i*2),"90","t");
                    areaParticle.createByPolarPositionExpression(x_f, y_f, z_f);
                }
                    }


        }
}
}
