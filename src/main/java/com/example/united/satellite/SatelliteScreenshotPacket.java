//package com.example.united.satellite;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.Screenshot;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.entity.Entity;
//import net.minecraftforge.network.NetworkEvent;
//
//import java.util.function.Supplier;
//
//public class SatelliteScreenshotPacket {
//    public double x, y, z;
//    public int id;
//
//    public SatelliteScreenshotPacket(double x, double y, double z,int id) {
//        this.x = x; this.y = y; this.z = z;this.id=id;
//    }
//
//    public static void encode(SatelliteScreenshotPacket msg, FriendlyByteBuf buf) {
//        buf.writeDouble(msg.x);
//        buf.writeDouble(msg.y);
//        buf.writeDouble(msg.z);
//        buf.writeInt(msg.id);
//    }
//
//    public static SatelliteScreenshotPacket decode(FriendlyByteBuf buf) {
//        return new SatelliteScreenshotPacket(buf.readDouble(), buf.readDouble(), buf.readDouble(),buf.readInt());
//    }
//
//
//    public static void handle(SatelliteScreenshotPacket msg, Supplier<NetworkEvent.Context> ctx) {
//        ctx.get().enqueueWork(() -> {
//            Minecraft mc = Minecraft.getInstance();
//            Entity player = mc.player;
//            Entity target = mc.level.getEntity(msg.id);
//
//            if (player == null || target == null) return;
//
//            // 保存原位置和视角
//            double oldX = player.getX(), oldY = player.getY(), oldZ = player.getZ();
//            float oldYaw = player.getYRot();
//            float oldPitch = player.getXRot();
//            boolean oldHideGui = mc.options.hideGui;
//
//            // 存储截图中心点坐标
//            ScreenshotEventHandler.setPendingCoordinates(msg.x, msg.y, msg.z);
//
//            // 设置 GUI 隐藏
//            mc.options.hideGui = true;
//
//            // 瞬间传送并设置视角
//            player.setPos(target.getX(), target.getY(), target.getZ());
//            player.setYRot(0);    // yaw 朝北（可改为 target.getYRot()）
//            player.setXRot(90);  // pitch 向下看
//
//            // 截图
//            Screenshot.grab(mc.gameDirectory, mc.getMainRenderTarget(), component -> {
//                System.out.println("截图完成");
//            });
////            player.setPos(oldX, oldY, oldZ);
////            player.setYRot(oldYaw);
////            player.setXRot(oldPitch);
////            mc.options.hideGui = oldHideGui;
//        });
//        ctx.get().setPacketHandled(true);
//    }
//}