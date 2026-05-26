package com.example.united.reversedPickaxe;


import com.example.examplemod.ExampleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID)
public class AxePick extends Item {
    public AxePick(Properties props) {
        super(props);
    }
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event){
        if(!(event.getPlayer().getMainHandItem().getItem() instanceof AxePick)){return;}
        Level level = event.getPlayer().level();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);

        if (!level.isClientSide && be instanceof VoidBlockEntity ve) {
            BlockState original = ve.getOriginalState();
            CompoundTag originalNBT = ve.getOriginalNBT();
            event.setCanceled(true);
            // 先移除虚空方块
            level.removeBlock(pos, false);

            // 恢复原始方块
            level.setBlock(pos, original, 3);
            // 如果有 BlockEntity NBT，就恢复它
            if (originalNBT != null) {
                BlockEntity restored = level.getBlockEntity(pos);
                if (restored != null) {
                    restored.load(originalNBT);
                }
            }
        }
    }


//    @Override
//    public InteractionResult useOn(UseOnContext context) {
//        Level level = context.getLevel();
//        BlockPos pos = context.getClickedPos();
//        BlockState state = level.getBlockState(pos);
//        BlockEntity be = level.getBlockEntity(pos);
//
//        if (!level.isClientSide && be instanceof VoidBlockEntity ve) {
//            BlockState original = ve.getOriginalState();
//            CompoundTag originalNBT = ve.getOriginalNBT();
//
//            // 先移除虚空方块
//            level.removeBlock(pos, false);
//
//            // 恢复原始方块
//            level.setBlock(pos, original, 3);
//
//            // 如果有 BlockEntity NBT，就恢复它
//            if (originalNBT != null) {
//                BlockEntity restored = level.getBlockEntity(pos);
//                if (restored != null) {
//                    restored.load(originalNBT);
//                }
//            }
//            return InteractionResult.SUCCESS;
//        }
//
//        return InteractionResult.PASS;
//    }
}
