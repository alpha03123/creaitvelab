package com.example.examplemod;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.world.phys.Vec3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Predictor {
    public static Vec3 getPredictedDirection(double dx, double dy, double dz) {
        try {
            URL url = new URL("http://192.168.1.104:5000"+"/predict");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // 添加 dy 参数
            String jsonInput = String.format("{\"dx\": %.4f, \"dy\": %.4f, \"dz\": %.4f}", dx, dy, dz);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                os.write(input);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
            }

            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(response.toString(), JsonObject.class);
            float vx = obj.get("vx").getAsFloat();
            float vy = obj.get("vy").getAsFloat();
            float vz = obj.get("vz").getAsFloat();

            return new Vec3(vx, vy, vz);  // Forge 自带的三维向量类

        } catch (Exception e) {
            e.printStackTrace();
            return new Vec3(0, 0, 0);
        }
    }
}
