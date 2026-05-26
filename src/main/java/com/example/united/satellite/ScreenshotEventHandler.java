//package com.example.united.satellite;
//
//import com.example.examplemod.ExampleMod;
//import com.google.gson.JsonObject;
//import com.mojang.blaze3d.platform.NativeImage;
//import net.minecraftforge.client.event.ScreenshotEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Path;
//import java.util.Base64;
//import com.mojang.blaze3d.platform.NativeImage;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//
//
//@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE) // 修改为你的 mod id
//public class ScreenshotEventHandler {
//    private static double pendingX = 0, pendingY = 0, pendingZ = 0;
//    private static boolean hasPending = false;
//
//    public static void setPendingCoordinates(double x, double y, double z) {
//        pendingX = x;
//        pendingY = y;
//        pendingZ = z;
//        hasPending = true;
//    }
//    public static byte[] nativeImageToPNG(NativeImage image) throws IOException {
//        BufferedImage buffered = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
//        for (int y = 0; y < image.getHeight(); y++) {
//            for (int x = 0; x < image.getWidth(); x++) {
//                buffered.setRGB(x, y, image.getPixelRGBA(x, y));
//            }
//        }
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        ImageIO.write(buffered, "png", out);
//        return out.toByteArray();
//    }
//
//
//    @SubscribeEvent
//    public static void onScreenshot(ScreenshotEvent event) {
//        if (!hasPending) return;
//        hasPending = false;
//
//        NativeImage image = event.getImage(); // 获取截图内容
//        try {
//            // 将 NativeImage 转为字节数组
//            byte[] bytes = nativeImageToPNG(image); // 自定义函数
//            String base64 = Base64.getEncoder().encodeToString(bytes);
//
//            JsonObject json = new JsonObject();
//            json.addProperty("image", "data:image/png;base64," + base64);
//            json.addProperty("x", pendingX);
//            json.addProperty("y", pendingY);
//            json.addProperty("z", pendingZ);
//
//            // 异步上传
//            new Thread(() -> {
//                try {
//                    URL url = new URL("http://172.20.10.3:5000/upload");
//                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                    con.setRequestMethod("POST");
//                    con.setRequestProperty("Content-Type", "application/json");
//                    con.setDoOutput(true);
//                    OutputStream os = con.getOutputStream();
//                    os.write(json.toString().getBytes(StandardCharsets.UTF_8));
//                    os.close();
//                    con.getInputStream().close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}