package com.example.united.mapTest;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ColorUtils {

    public static Color blendColors(Color c1, Color c2, float alpha) {
        float inv = 1.0f - alpha;
        int r = (int) (c1.getRed() * inv + c2.getRed() * alpha);
        int g = (int) (c1.getGreen() * inv + c2.getGreen() * alpha);
        int b = (int) (c1.getBlue() * inv + c2.getBlue() * alpha);
        return new Color(Math.min(255, r), Math.min(255, g), Math.min(255, b));
    }
    // ✅ 计算颜色
    public static Color calculateColor(int[][] heightMap, int x, int z, int worldX, int worldZ, Level level) {
        float baseY = 90;
        BlockPos pos = new BlockPos(worldX, heightMap[x][z], worldZ);
        BlockState state = level.getBlockState(pos);
        int mapCol = state.getMapColor(level, pos).col;
        Color base = new Color(mapCol);

        float t = (heightMap[x][z] - baseY) / 30f;
        t = Math.max(-1f, Math.min(1f, t));

        float brightness = 0.65f + 0.35f * (float) (0.5f + 0.5f * Math.sin(t * Math.PI / 2));

        int dy = 0;
        if (x > 0 && z > 0) {
            int h1 = heightMap[x][z];
            int h2 = heightMap[x - 1][z];
            int h3 = heightMap[x][z - 1];
            dy = h1 - Math.min(h2, h3);
        }
        float shade = 1.0f + dy * 0.05f;

        float[] hsb = Color.RGBtoHSB(base.getRed(), base.getGreen(), base.getBlue(), null);
        hsb[0] = (hsb[0] + t * 0.08f) % 1f;
        hsb[2] = Math.min(1f, hsb[2] * brightness * shade);

        Color finalColor = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);

        int hCur = heightMap[x][z];
        int hL = (x > 0) ? heightMap[x - 1][z] : hCur;
        int hU = (z > 0) ? heightMap[x][z - 1] : hCur;
        int hR = (x < 15) ? heightMap[x + 1][z] : hCur;
        int hD = (z < 15) ? heightMap[x][z + 1] : hCur;

        int maxDiff = Math.max(
                Math.max(Math.abs(hCur - hL), Math.abs(hCur - hR)),
                Math.max(Math.abs(hCur - hU), Math.abs(hCur - hD))
        );
        if (maxDiff >= 3) {
            finalColor = blendColors(finalColor, new Color(0, 0, 0), 0.3f);
        }

        return finalColor;
    }
}