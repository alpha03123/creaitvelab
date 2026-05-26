package com.example.united.reversedPickaxe;

import com.example.examplemod.registry.Moditems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class VoidBlockRenderer implements BlockEntityRenderer<VoidBlockEntity> {

    List<Block> vanillaBlocks = new ArrayList<>(ForgeRegistries.BLOCKS.getValues());
    public int index=1;
    @Override
    public void render(@NotNull VoidBlockEntity entity, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        // 如果玩家没拿 ToolB，就不渲染
        if (player == null || player.getMainHandItem().getItem() != Moditems.AXE_PICK.get()) {
            return;
        }

//        if(player.level().getGameTime()%20==0){
//            index=new Random().nextInt(vanillaBlocks.size());
//        }
//        Block block=vanillaBlocks.get(index);
//        poseStack.pushPose();
//        poseStack.translate(0, 0, 0);
//        BlockRenderDispatcher dispatcher = mc.getBlockRenderer();
//        dispatcher.renderSingleBlock(block.defaultBlockState(), poseStack, buffer, combinedLight, combinedOverlay);
//        poseStack.popPose();




//        BlockState barrierState = Blocks.JIGSAW.defaultBlockState(); // 可替换为你自己的显示模型
//        dispatcher.renderSingleBlock(barrierState, poseStack, buffer, combinedLight, combinedOverlay);
//        poseStack.pushPose();
//        poseStack.translate(1,0,0);
//        BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
//        BlockState state = Blocks.CHEST.defaultBlockState();
//        blockRenderDispatcher.renderSingleBlock(state,poseStack,buffer,combinedLight,combinedOverlay);
//        poseStack.popPose();
//        poseStack.pushPose();
//        poseStack.translate(0,1,0);
//        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
//        ItemStack stack = new ItemStack(Items.DIAMOND);
//        BakedModel bakedModel = itemRenderer.getModel(stack,entity.getLevel(),null,0);
//        itemRenderer.render(stack, ItemDisplayContext.FIXED,true,poseStack,buffer,combinedLight,combinedOverlay,bakedModel);
//        poseStack.popPose();
    }
}