package com.example.united.reversedPickaxe;

import com.example.examplemod.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class VoidBlockEntity extends BlockEntity {

    private BlockState originalState = Blocks.AIR.defaultBlockState();
    private CompoundTag originalNBT = null;

    public VoidBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.VOID_BLOCK_ENTITY.get(), pos, state);
    }

    public void storeOriginal(BlockState state, @Nullable CompoundTag tag) {
        this.originalState = state;
        this.originalNBT = tag != null ? tag.copy() : null;
        setChanged();
    }

    public BlockState getOriginalState() {
        return originalState;
    }

    public CompoundTag getOriginalNBT() {
        return originalNBT;
    }
    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("originalState", NbtUtils.writeBlockState(originalState));
        if (originalNBT != null) {
            tag.put("originalNBT", originalNBT);
        }
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        originalState = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), tag.getCompound("originalState"));
        if (tag.contains("originalNBT")) {
            originalNBT = tag.getCompound("originalNBT");
        }
    }
}