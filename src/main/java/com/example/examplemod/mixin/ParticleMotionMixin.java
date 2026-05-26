package com.example.examplemod.mixin;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.advanced_functions.extend.Expression;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Particle.class)
public abstract class ParticleMotionMixin {

    @Shadow
    protected double xd;
    @Shadow
    protected double yd;
    @Shadow
    protected double zd;
    @Shadow
    protected double x; // 粒子当前 X 坐标
    @Shadow
    protected double y; // 粒子当前 Y 坐标
    @Shadow
    protected double z; // 粒子当前 Z 坐标
    @Shadow
    protected int age;

    private double spawnX;
    private double spawnY;
    private double spawnZ;

    private Expression xSpeedExpression = (t, x, y, z) -> 1.0f; // 默认 X 方向速度表达式是一个常量 1.0
    private Expression ySpeedExpression = (t, x, y, z) -> 1.0f; // 默认 Y 方向速度表达式是一个常量 1.0
    private Expression zSpeedExpression = (t, x, y, z) -> 1.0f; // 默认 Z 方向速度表达式是一个常量 1.0
    private double startTime = 0.0;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        Particle particle = (Particle) (Object) this;
        this.spawnX = particle.getPos().x;
        this.spawnY = particle.getPos().y;
        this.spawnZ = particle.getPos().z;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        float currentTime = (float) (this.age - startTime);

        // 计算相对坐标
        float relativeX = (float) (this.x - this.spawnX);
        float relativeY = (float) (this.y - this.spawnY);
        float relativeZ = (float) (this.z - this.spawnZ);

        // 计算速度因子
        double xSpeedFactor = xSpeedExpression.apply(currentTime, relativeX, relativeY, relativeZ);
        double ySpeedFactor = ySpeedExpression.apply(currentTime, relativeX, relativeY, relativeZ);
        double zSpeedFactor = zSpeedExpression.apply(currentTime, relativeX, relativeY, relativeZ);

        // 更新速度
        this.xd *= xSpeedFactor;
        this.yd *= ySpeedFactor;
        this.zd *= zSpeedFactor;
    }

    /**
     * 设置 X 方向速度的表达式
     * @param expression X 方向速度的表达式
     */
    public void setXSpeedExpression(Expression expression) {
        this.xSpeedExpression = expression;
        this.startTime = (double) this.age;
    }

    /**
     * 设置 Y 方向速度的表达式
     * @param expression Y 方向速度的表达式
     */
    public void setYSpeedExpression(Expression expression) {
        this.ySpeedExpression = expression;
        this.startTime = (double) this.age;
    }

    /**
     * 设置 Z 方向速度的表达式
     * @param expression Z 方向速度的表达式
     */
    public void setZSpeedExpression(Expression expression) {
        this.zSpeedExpression = expression;
        this.startTime = (double) this.age;
    }
    public void debug(){
        ExampleMod.LOGGER.info("a test particle");
    }
}
