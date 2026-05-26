package com.example.united.selfPulsingRedStoneBlock;


import com.example.examplemod.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SelfPulsingRedStoneBlockEntity extends BlockEntity {
    private int ticks = 0;
    public SelfPulsingRedStoneBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SelfPulsingRedStoneBlockEntity.get(), pPos, pBlockState);
    }
    public void clientTick(){
        if (level != null) {
            level.updateNeighborsAt(getBlockPos(),this.getBlockState().getBlock());
        }
    }
    public void serverTick(){
        if (level != null) {
            level.updateNeighborsAt(getBlockPos(),this.getBlockState().getBlock());
        }
    }
    //方块实体的自定义方法Increase
    public int increase(){
        ticks++;
        setChanged();
        return ticks;
    }
    public int decrease(){
        ticks--;
        setChanged();
        return ticks;
    }
    public void setValue(int value){
        ticks=value;
        setChanged();
    }
    //从方块nbt读取到内存
    @Override
    public void load(CompoundTag pTag) {
        ticks = pTag.getInt("ticks");
        super.load(pTag);
    }
    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("ticks", ticks);
    }
    public int get(){
        return ticks;
    }

    //把内存中的counter以nbt形式存储到方块里

}