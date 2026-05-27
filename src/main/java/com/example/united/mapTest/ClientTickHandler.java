package com.example.united.mapTest;

import io.socket.client.IO;
import io.socket.client.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.*;

import static com.example.united.mapTest.Const.MAP_BACKEND_ENABLED;
import static com.example.united.mapTest.Const.local_ip;
import static com.example.united.mapTest.Get.listenClickEvent;
import static com.example.united.mapTest.Get.listenDisConnectEvent;
import static com.example.united.mapTest.Post.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientTickHandler {
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);

    // ✅ 可调的区块扫描半径 (扫描 (2n+1)^2 个区块)
    private static final int CHUNK_SCAN_RADIUS = 2;
    private static final int TOTAL_CHUNKS = (2 * CHUNK_SCAN_RADIUS + 1) * (2 * CHUNK_SCAN_RADIUS + 1);
    private static final int[][] OFFSETS = new int[TOTAL_CHUNKS][2];


    private static int tickCounter = 0;
    private static int offsetIndex = 0;


    static {
        int idx = 0;
        for (int dx = -CHUNK_SCAN_RADIUS; dx <= CHUNK_SCAN_RADIUS; dx++) {
            for (int dz = -CHUNK_SCAN_RADIUS; dz <= CHUNK_SCAN_RADIUS; dz++) {
                OFFSETS[idx++] = new int[]{dx, dz};
            }
        }
    }

    // ✅ 每 tick 调用的主入口
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (!MAP_BACKEND_ENABLED) return;
        if (event.phase != TickEvent.Phase.END) return;


        if (++tickCounter < 1) return; // 每 tick 处理一个区块
        tickCounter = 0;

        Minecraft mc = Minecraft.getInstance();
        Player player=mc.player;
        if (mc.level == null || mc.player == null) return;

        int[] offset = OFFSETS[offsetIndex];
        offsetIndex = (offsetIndex + 1) % OFFSETS.length;
        BlockPos center = mc.player.blockPosition().offset(offset[0] * 16, 0, offset[1] * 16);
        LevelChunk chunk = mc.level.getChunkAt(center);
        int minX = chunk.getPos().getMinBlockX();
        int minZ = chunk.getPos().getMinBlockZ();

        executor.submit(() -> {
            handleChunk(mc.level, chunk, minX, minZ);
            handlePlayerPos(player.getX(),player.getY(),player.getZ(),player.getViewYRot(20));
            handleEntityPos(new ArrayList<>(player.level().getEntities(player, player.getBoundingBox().inflate(10))));
        });
    }



}
