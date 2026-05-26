//package com.example.examplemod.network.packet.servertoplayer;
//
//import com.example.examplemod.entities.manage.EntityManager;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.level.Level;
//import net.minecraftforge.network.NetworkEvent;
//
//import java.util.function.Supplier;
//
//public class EntityRemovePacket {
//    private final int entityId;
//
//
//    public EntityRemovePacket(int entityId) {
//        this.entityId = entityId;
//    }
//
//    public EntityRemovePacket(FriendlyByteBuf buf) {
//        this.entityId = buf.readInt();
//
//    }
//
//    public void encode(FriendlyByteBuf buf) {
//        buf.writeInt(this.entityId);
//    }
//
//    public static EntityRemovePacket decode(FriendlyByteBuf buf) {
//        return new EntityRemovePacket(buf);
//    }
//
//    public static void handle(EntityRemovePacket msg, Supplier<NetworkEvent.Context> ctx) {
//        ctx.get().enqueueWork(() -> {
//            Level level = ctx.get().getSender().level();
//            System.out.println(msg.entityId);
//            Entity entity = level.getEntity(msg.entityId);
//            if (entity != null) {
//                entity.discard();  // 客户端移除实体
//                EntityManager.removeEntity(entity);
//                System.out.println("deleted");
//            }
//        });
//        ctx.get().setPacketHandled(true);
//    }
//}
