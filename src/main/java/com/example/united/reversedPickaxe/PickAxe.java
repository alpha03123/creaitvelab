package com.example.united.reversedPickaxe;


import com.example.examplemod.ExampleMod;
import com.example.examplemod.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID)
public class PickAxe extends PickaxeItem {
    public PickAxe(Properties props) {
        super(Tiers.DIAMOND,1,-2f,props);
    }
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event){
        Level level = event.getPlayer().level();
        BlockPos pos = event.getPos();
        if(!(event.getPlayer().getMainHandItem().getItem() instanceof PickAxe)){return;}

        BlockState state = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);
        CompoundTag tag = be != null ? be.saveWithFullMetadata() : null;

        if (!level.isClientSide) {
            // 先销毁原方块
            event.setCanceled(true);
            level.removeBlock(pos, false);

            // 放置虚空方块
            level.setBlock(pos, ModBlocks.VOID_BLOCK.get().defaultBlockState(), 3);

            // 存储数据
            BlockEntity voidBe = level.getBlockEntity(pos);
            if (voidBe instanceof VoidBlockEntity ve) {
                ve.storeOriginal(state, tag);
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
//        CompoundTag tag = be != null ? be.saveWithFullMetadata() : null;
//
//        if (!level.isClientSide) {
//            // 先销毁原方块
//            level.removeBlock(pos, false);
//
//            // 放置虚空方块
//            level.setBlock(pos, ModBlocks.VOID_BLOCK.get().defaultBlockState(), 3);
//
//            // 存储数据
//            BlockEntity voidBe = level.getBlockEntity(pos);
//            if (voidBe instanceof VoidBlockEntity ve) {
//                ve.storeOriginal(state, tag);
//            }
//        }
//
//        return InteractionResult.SUCCESS;
//    }
//}
