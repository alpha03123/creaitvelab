package com.example.united.mapTest;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entities.BaseComplexEntity;
import com.example.examplemod.registry.ModEntities;
import com.example.united.mapTest.helldiver_entities.PlaneEntity;
import com.example.united.mapTest.helldiver_entities.ProjectileEntity;
import com.example.united.mapTest.helldiver_entities.ProjectileHelper;
import com.example.united.mapTest.helldiver_entities.TrajectoryHelper;
import io.socket.client.IO;
import io.socket.client.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



import static com.example.united.mapTest.Const.local_ip;
import static com.example.united.mapTest.Get.*;
import static com.example.united.mapTest.Get.l1;
import static com.example.united.mapTest.Post.handleChunk;
import static com.example.united.mapTest.Post.handlePlayerPos;
import static com.example.united.mapTest.handleStrategies.planeEntities;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID)
public class TickHandler {
    private static int offsetIndex = 0;
    private static final int CHUNK_SCAN_RADIUS = 1;
    private static final int TOTAL_CHUNKS = (2 * CHUNK_SCAN_RADIUS + 1) * (2 * CHUNK_SCAN_RADIUS + 1);
    private static final int[][] OFFSETS = new int[TOTAL_CHUNKS][2];
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);
    private static boolean socketInitialized = false;
    static {
        int idx = 0;
        for (int dx = -CHUNK_SCAN_RADIUS; dx <= CHUNK_SCAN_RADIUS; dx++) {
            for (int dz = -CHUNK_SCAN_RADIUS; dz <= CHUNK_SCAN_RADIUS; dz++) {
                OFFSETS[idx++] = new int[]{dx, dz};
            }
        }
    }
    public static Socket initSocketClient(Level level) {
        if (!socketInitialized) {
            try {
                IO.Options options = IO.Options.builder().setForceNew(true).setReconnection(true).build();
                Socket socket = IO.socket(local_ip, options);
                listenClickEvent(socket,level);
                System.out.println("listening...init");
                listenDisConnectEvent(socket);
                socket.connect();
                socketInitialized = true;
                return socket;
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }return null;
    }
    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event){
        ServerLevel level=event.getServer().getLevel(Level.OVERWORLD);
        initSocketClient(level);
        planeEntities.removeIf(PlaneEntity::isRemoved);

        if (!l1.isEmpty() && !l2.isEmpty()) {
            for(ArrayList<Integer> l1I:l1s){
                Vec3 start = new Vec3(l1I.get(0), l1I.get(1), l1I.get(2));
                Vec3 end = new Vec3(Get.l2.get(0), Get.l2.get(1), Get.l2.get(2));
                System.out.println("Spawning projectile from " + start + " to " + end);

                // 1. 定义飞行参数
                // 1. 定义飞行参数
                double distance = start.distanceTo(end);
                // 根据距离决定飞行时间，作为轨迹计算的输入
                double travelTime = Math.max(20, distance * 2.0);

                // 2. 使用新的工具类一次性计算出整个轨迹
                List<Vec3> trajectory = TrajectoryHelper.calculateTrajectory(level, start, end, travelTime, 0.05D);

                // 如果轨迹有效（例如，没有因为某些原因计算失败）
                if (trajectory != null && !trajectory.isEmpty()) {
                    // 3. 创建实体实例
                    ProjectileEntity projectile = new ProjectileEntity(ModEntities.BASE_COMPLEX_ENTITY.get(), level);

                    // 4. 将计算好的轨迹设置给实体
                    projectile.setTrajectory(trajectory);

                    // 5. 将实体添加到世界中
                    level.addFreshEntity(projectile);
                }
            }
            l1.clear();
            l2.clear();
            l1s.clear();
        }



        for (PlaneEntity entity:planeEntities){

            entity.age++;
            if(entity.age>entity.lifetime){
                entity.discard();
            }
            int[] offset = OFFSETS[offsetIndex];
            offsetIndex = (offsetIndex + 1) % OFFSETS.length;
            BlockPos center = entity.blockPosition().offset(offset[0] * 16, 0, offset[1] * 16);
            LevelChunk chunk = entity.level().getChunkAt(center);
            int minX = chunk.getPos().getMinBlockX();
            int minZ = chunk.getPos().getMinBlockZ();
            executor.submit(() -> {
                handleChunk(entity.level(),chunk , minX, minZ);
            });
        }

    }

}
