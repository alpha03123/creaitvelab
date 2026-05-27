package com.example.united.mapTest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;

import java.awt.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static com.example.united.mapTest.ColorUtils.calculateColor;
import static com.example.united.mapTest.Const.MAP_BACKEND_ENABLED;
import static com.example.united.mapTest.Const.local_ip;

public class Post {

    // ✅ 构造颜色数据
    public static JsonArray constructMapChunkColorAndPosForServer(int minX, int minZ, int[][] heightMap, Level level) {
        JsonArray result = new JsonArray();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = minX + x;
                int worldZ = minZ + z;
                int y = heightMap[x][z];
                Color finalColor = calculateColor(heightMap, x, z, worldX, worldZ, level);

                JsonObject obj = new JsonObject();
                obj.addProperty("x", worldX);
                obj.addProperty("y", y);
                obj.addProperty("z", worldZ);
                obj.addProperty("r", finalColor.getRed());
                obj.addProperty("g", finalColor.getGreen());
                obj.addProperty("b", finalColor.getBlue());
                result.add(obj);
            }
        }
        return result;
    }

    // ✅ 获取高度图
    private static void updateHeightMap(int[][] heightMap, LevelChunk chunk) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                heightMap[x][z] = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
            }
        }
    }



    public static void handleChunk(Level level, LevelChunk chunk, int minX, int minZ) {
        try {
            int[][] heightMap = new int[16][16];
            updateHeightMap(heightMap, chunk);
            JsonArray result = constructMapChunkColorAndPosForServer(minX, minZ, heightMap, level);
            postToServer(result,"/update_bulk");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void handleEntityPos(ArrayList<Entity> entities) {
        try {
            JsonArray result = new JsonArray();
            for(Entity entity:entities){
                JsonObject obj=new JsonObject();
                obj.addProperty("x",entity.getX());
                obj.addProperty("z",entity.getZ());
                result.add(obj);
            }

            postToServer(result,"/entity_pos");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void handlePlayerPos(double x,double y,double z,float yaw) {
        try {
            JsonArray result = new JsonArray();
            JsonObject obj=new JsonObject();
            obj.addProperty("x",x);
            obj.addProperty("y",y);
            obj.addProperty("z",z);
            obj.addProperty("yaw",yaw);
            result.add(obj);
            postToServer(result,"/player_pos");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void postToServer(JsonArray data,String route) {
        if (!MAP_BACKEND_ENABLED) return;
        try {
            URL url = new URL(local_ip+route);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(data.toString().getBytes(StandardCharsets.UTF_8));
            }

            conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
