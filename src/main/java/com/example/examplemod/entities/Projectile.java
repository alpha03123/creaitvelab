package com.example.examplemod.entities;

import com.example.examplemod.entities.BaseComplexEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class Projectile extends BaseComplexEntity {

    private Vec3 velocity;
    private final Vec3 gravity = new Vec3(0, -0.08, 0);  // 模拟重力 (每tick下落0.05)

    public Projectile(EntityType<?> pEntityType, Level pLevel, Vec3 pos, Vec3 velocity) {
        super(pEntityType, pLevel, pos, new Vec3(0, 0, 0));  // 传入位置和速度
        this.velocity = velocity;
    }

    @Override
    public void tick() {
        super.tick();

        // 计算新的位置
        Vec3 newPos = this.position().add(velocity);
        this.setPos(newPos.x, newPos.y, newPos.z);

        // 应用重力
        velocity = velocity.add(gravity);  // 每tick增加重力

        // 进行碰撞检测
        this.handleCollisions();
    }

    private void handleCollisions() {
        // 使用 ClipContext 来进行碰撞检测
        // ClipContext 参数说明：start, end, blockMode, fluidMode, entity
        ClipContext clipContext = new ClipContext(
                this.position(), // 起始位置
                this.position().add(velocity), // 终点位置（当前位置 + 速度）
                ClipContext.Block.COLLIDER, // 方块碰撞模式
                ClipContext.Fluid.NONE, // 流体碰撞模式，NONE表示不考虑流体
                this // 当前实体
        );

        // 通过 Level.clip() 进行碰撞检测
        BlockHitResult result = this.level().clip(clipContext);

        // 如果发生了碰撞
        if (result.getType() == HitResult.Type.BLOCK) {
            BlockState blockState = this.level().getBlockState(result.getBlockPos());

            // 碰撞后的逻辑处理
            onBlockCollision(blockState, result.getBlockPos());
        }
    }

    // 碰撞后执行的逻辑
    private void onBlockCollision(BlockState blockState, BlockPos blockPos) {
        // 这里可以根据实际需求，执行碰撞后的逻辑
        // 例如：爆炸、产生粒子、改变方块状态等
        if (!this.level().isClientSide) {  // 确保在服务器端发送粒子效果
            ((ServerLevel)this.level()).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 10, 0.5, 0.5, 0.5, 0.1);
        }

        // 执行其他逻辑（例如伤害、方块破坏等）
        this.discard();  // 碰撞后销毁该投掷物
    }
}
