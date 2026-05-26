//package com.example.united.satellite;
//
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.Level;
//import net.minecraftforge.network.PacketDistributor;
//
//import java.util.UUID;
//
//public class BranchSatelliteEntity extends Entity {
//    private UUID boundPlayerUUID;
//
//    public BranchSatelliteEntity(EntityType<?> type, Level level) {
//        super(type, level);
//    }
//
//    @Override
//    protected void defineSynchedData() {
//
//    }
//
//    public void init(Player player) {
//        this.boundPlayerUUID = player.getUUID();
//    }
//
//    @Override
//    public void tick() {
//        super.tick();
//        if (!level().isClientSide) {
//            Player player = level().getPlayerByUUID(boundPlayerUUID);
//            if (player != null) {
//                // 向客户端发送截图请求
//                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
//                        new SatelliteScreenshotPacket(this.getX(), this.getY(), this.getZ(),this.getId()));
//            }
//            this.discard(); // 只发送一次截图请求
//        }
//    }
//
//    @Override
//    protected void readAdditionalSaveData(CompoundTag pCompound) {
//
//    }
//
//    @Override
//    protected void addAdditionalSaveData(CompoundTag pCompound) {
//
//    }
//}