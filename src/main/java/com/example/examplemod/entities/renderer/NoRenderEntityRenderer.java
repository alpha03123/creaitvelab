package com.example.examplemod.entities.renderer;

import com.example.examplemod.entities.AreaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class NoRenderEntityRenderer<T extends Entity> extends EntityRenderer<T> {
    public NoRenderEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(T pEntity) {
        return null;
    }
    @Override
    public void render(T pEntity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        // 不渲染任何内容
    }
}
