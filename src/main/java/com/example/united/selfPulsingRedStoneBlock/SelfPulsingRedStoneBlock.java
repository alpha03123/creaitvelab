package com.example.united.selfPulsingRedStoneBlock;

import com.example.particlecomplex.ExampleMod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;

//承载方块实体的基类方块(BaseEntityBlock继承与BLock)
@Mod.EventBusSubscriber(modid = ExampleMod.MODID)
public class SelfPulsingRedStoneBlock extends BaseEntityBlock {
    public SelfPulsingRedStoneBlock() {
        super(Properties.copy(Blocks.STONE));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new SelfPulsingRedStoneBlockEntity(pPos, pState);
    }

    @Override
    public boolean isSignalSource(@NotNull BlockState state) {
        return true;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    // 向指定方向发出红石信号，16表示最大信号强度
    @Override
    public int getSignal(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull Direction direction) {
        SelfPulsingRedStoneBlockEntity selfPulsingRedStoneBlockEntity = (SelfPulsingRedStoneBlockEntity) world.getBlockEntity(pos);
        if (selfPulsingRedStoneBlockEntity != null) {
            if (Objects.requireNonNull(selfPulsingRedStoneBlockEntity.getLevel()).getGameTime() % (selfPulsingRedStoneBlockEntity.get()+1) == 0) {
                return 16;
            } else {
                return 0;
            }
        }
        return 0;
    }

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getLevel().isClientSide && event.getHand() == InteractionHand.MAIN_HAND) {
            SelfPulsingRedStoneBlockEntity selfPulsingRedStoneBlockEntity=null ;
            if(event.getLevel().getBlockEntity(event.getPos()) instanceof SelfPulsingRedStoneBlockEntity){
                selfPulsingRedStoneBlockEntity = (SelfPulsingRedStoneBlockEntity) event.getLevel().getBlockEntity(event.getPos());
            }
            int counter = 0;
            if (selfPulsingRedStoneBlockEntity != null) {
                if (event.getEntity().isCrouching()) {
                    for (int i = 0; i < 10; i += 1) counter = selfPulsingRedStoneBlockEntity.increase();
                    event.getEntity().sendSystemMessage(Component.literal("脉冲时长:" + counter));
                } else {
                    counter = selfPulsingRedStoneBlockEntity.increase();
                    event.getEntity().sendSystemMessage(Component.literal("脉冲时长:" + counter));
                }
            }
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return (plevel, pPos, pState, pBlockEntityType) -> {
                if (pBlockEntityType instanceof SelfPulsingRedStoneBlockEntity se) {
                    se.clientTick();
                }
            };
        } else {
            return (plevel, pPos, pState, pBlockEntityType) -> {
                if (pBlockEntityType instanceof SelfPulsingRedStoneBlockEntity se) {
                    se.serverTick();
                }
            };
        }
        // 使用 `createTickerHelper` 帮助方法，绑定自定义的 tick 逻辑
    }

    @SubscribeEvent
    public static void onLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        if (!event.getLevel().isClientSide && event.getHand() == InteractionHand.MAIN_HAND && event.getLevel().getBlockEntity(event.getPos()) instanceof SelfPulsingRedStoneBlockEntity selfPulsingRedStoneBlockEntity) {
            int counter = 0;
            event.setCanceled(true);
            if (event.getEntity().isCrouching()) {
                event.setCanceled(false);
            } else {
                counter = selfPulsingRedStoneBlockEntity.decrease();
            }
            if (counter <= 0) {
                selfPulsingRedStoneBlockEntity.setValue(1);
                counter = 0;
            }
            event.getEntity().sendSystemMessage(Component.literal("脉冲时长:" + counter));
        }
    }


}