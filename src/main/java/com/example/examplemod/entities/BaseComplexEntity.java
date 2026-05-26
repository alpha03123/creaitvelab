package com.example.examplemod.entities;


import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public class BaseComplexEntity extends Entity {

    public int lifetime=80;
    public int age=0;
    public Vec3 velocity =new Vec3(0,0,0);
    public Vec3 center;
    public Vec3 angularVelocity=new Vec3(0,0,0);
    private Function<BaseComplexEntity, Vec3> centerFormula=e->e.center;
    private Function<BaseComplexEntity, Vec3> speedFormula=e->e.velocity;
    private Function<BaseComplexEntity, Vec3> angularVelocityFormula=e->e.angularVelocity;
    private Vec3 initialSpeed=Vec3.ZERO;
    private Function<BaseComplexEntity, Float> XRotFunction= Entity::getXRot;
    private Function<BaseComplexEntity, Float> YRotFunction= Entity::getYRot;
    private Function<BaseComplexEntity, Vec3> posFunction=Entity::position;
    private Function<BaseComplexEntity,Vec3> lookAtPosFunction =null;

    public BaseComplexEntity(EntityType<?> pEntityType, Level pLevel, Vec3 pos, Vec3 offset) {
        super(pEntityType, pLevel);
        this.setPos(pos);
        this.center=pos.add(offset);
    }
    public BaseComplexEntity(EntityType<?> pEntityType, Level pLevel, Vec3 pos) {
        super(pEntityType, pLevel);
        this.setPos(pos);
        this.center=pos;
    }
    public BlockHitResult getBlockHitResult(Level world) {
        // 获取实体的当前位置
        Vec3 from = this.getEyePosition(1.0F); // 获取实体眼睛的位置，可以根据需要调整

        // 获取实体的朝向（假设是实体的速度或者朝向方向）
        Vec3 motion = this.velocity.normalize();
        double times=0.5d;

        // 设置射线检测的目标位置（例如基于速度或朝向方向来设置）
        Vec3 to = from.add(motion.x * times, motion.y * times, motion.z * times);

        // 使用射线检测来获取碰撞的结果

        // 返回碰撞结果
        return world.clip(new ClipContext(from, to, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this));
    }
    public static double[] rotateX(double ignoredCenterX, double centerY, double centerZ,
                                   double ignoredX, double Y, double Z, double angleDegrees) {
        double angleRadians = Math.toRadians(angleDegrees);

        // 移动到以 center 为原点的坐标系
        double translatedY = Y - centerY;
        double translatedZ = Z - centerZ;

        // 使用绕 X 轴的旋转矩阵旋转
        double rotatedY = translatedY * Math.cos(angleRadians) - translatedZ * Math.sin(angleRadians);
        double rotatedZ = translatedY * Math.sin(angleRadians) + translatedZ * Math.cos(angleRadians);

        // 移回原来的坐标系
        rotatedY += centerY;
        rotatedZ += centerZ;

        // 返回从原始点到旋转后点的向量
        return new double[]{0, rotatedY - Y, rotatedZ - Z};
    }

    // 绕 Y 轴旋转
    public static double[] rotateY(double centerX, double ignoredCenterY, double centerZ,
                                   double X, double ignoredY, double Z, double angleDegrees) {
        double angleRadians = Math.toRadians(angleDegrees);

        // 移动到以 center 为原点的坐标系
        double translatedX = X - centerX;
        double translatedZ = Z - centerZ;

        // 使用绕 Y 轴的旋转矩阵旋转
        double rotatedX = translatedX * Math.cos(angleRadians) + translatedZ * Math.sin(angleRadians);
        double rotatedZ = -translatedX * Math.sin(angleRadians) + translatedZ * Math.cos(angleRadians);

        // 移回原来的坐标系
        rotatedX += centerX;
        rotatedZ += centerZ;

        // 返回从原始点到旋转后点的向量
        return new double[]{rotatedX - X, 0, rotatedZ - Z};
    }

    // 绕 Z 轴旋转
    public static double[] rotateZ(double centerX, double centerY, double ignoredCenterZ,
                                   double X, double Y, double ignoredZ, double angleDegrees) {
        double angleRadians = Math.toRadians(angleDegrees);

        // 移动到以 center 为原点的坐标系
        double translatedX = X - centerX;
        double translatedY = Y - centerY;

        // 使用绕 Z 轴的旋转矩阵旋转
        double rotatedX = translatedX * Math.cos(angleRadians) - translatedY * Math.sin(angleRadians);
        double rotatedY = translatedX * Math.sin(angleRadians) + translatedY * Math.cos(angleRadians);

        // 移回原来的坐标系
        rotatedX += centerX;
        rotatedY += centerY;

        // 返回从原始点到旋转后点的向量
        return new double[]{rotatedX - X, rotatedY - Y, 0};
    }

    public void rotate() {
        if(angularVelocity.x!=0||angularVelocity.y!=0||angularVelocity.z!=0){
            if(!center.equals(new Vec3(0,0,0))){
            double v1x = rotateX(this.center.x, this.center.y, this.center.z, this.getX(), this.getY(), this.getZ(), this.angularVelocity.x)[0];
            double v1y = rotateX(this.center.x, this.center.y, this.center.z, this.getX(), this.getY(), this.getZ(), this.angularVelocity.x)[1];
            double v1z = rotateX(this.center.x, this.center.y, this.center.z, this.getX(), this.getY(), this.getZ(), this.angularVelocity.x)[2];
            double v2x = rotateY(this.center.x, this.center.y, this.center.z, this.getX(), this.getY(), this.getZ(), this.angularVelocity.y)[0];
            double v2y = rotateY(this.center.x, this.center.y, this.center.z, this.getX(), this.getY(), this.getZ(), this.angularVelocity.y)[1];
            double v2z = rotateY(this.center.x, this.center.y, this.center.z, this.getX(), this.getY(), this.getZ(), this.angularVelocity.y)[2];
            double v3x = rotateZ(this.center.x, this.center.y, this.center.z, this.getX(), this.getY(), this.getZ(), this.angularVelocity.z)[0];
            double v3y = rotateZ(this.center.x, this.center.y, this.center.z, this.getX(), this.getY(), this.getZ(), this.angularVelocity.z)[1];
            double v3z = rotateZ(this.center.x, this.center.y, this.center.z, this.getX(), this.getY(), this.getZ(), this.angularVelocity.z)[2];
            double xd=this.velocity.x;
            double yd=this.velocity.y;
            double zd=this.velocity.z;
            this.velocity=new Vec3(xd+v1x+v2x+v3x,yd+v1y+v2y+v3y,zd+v1z+v2z+v3z);
         }
        }
    }

    public void setPosFunction(Function<BaseComplexEntity, Vec3> formula){
        this.posFunction=formula;//纵向朝向
    }
    public void setXRotFunction(Function<BaseComplexEntity, Float> formula){
        this.XRotFunction=formula;//纵向朝向
    }
    public void setYRotFunction(Function<BaseComplexEntity, Float> formula){
        this.YRotFunction=formula;//横向朝向
    }
    public void setLookAtPosFunction(Function<BaseComplexEntity, Vec3> formula){
        this.lookAtPosFunction =formula;//横向朝向
    }
    public void setInitialSpeed(Vec3 initialSpeed){
        this.initialSpeed=initialSpeed;
    }
    public void setSpeedFunction(Function<BaseComplexEntity, Vec3> formula) {
        this.speedFormula = formula;
    }
    public void setAngularVelocityFunction(Function<BaseComplexEntity, Vec3> formula) {
        this.angularVelocityFormula = formula;
    }
    public void setCenterFunction(Function<BaseComplexEntity, Vec3> formula) {
        this.centerFormula = formula;
    }
    public Vec3 getLookPos(double times){
        Vec3 direction=this.getLookAngle().scale(times);

        return this.getEyePosition().add(direction);
    }
    public void transferTO(Vec3 pos){
        this.center=pos;
        this.setPos(pos);

    }
    public void applyFunctions(){
        this.initialSpeed =speedFormula.apply(this);
        this.center= centerFormula.apply(this);
        this.angularVelocity=angularVelocityFormula.apply(this);
        this.setXRot(XRotFunction.apply(this));
        this.setYRot(YRotFunction.apply(this));
        this.setPos(posFunction.apply(this));
        if(lookAtPosFunction!=null){
            this.lookAt(EntityAnchorArgument.Anchor.EYES,lookAtPosFunction.apply(this));
        }

    }
    public void updateMotion(){
        this.velocity=initialSpeed;
        rotate(); //根据angularVelocity更改Velocity
        this.move(MoverType.SELF, velocity);// 根据Velocity更改Pos
    }

    @Override
    public void tick(){
        applyFunctions();
        updateMotion();
        this.age++;
        if(this.age>this.lifetime){
            this.discard();
        }
        releaseParticle();
    }

    public void releaseParticle(){
//        Level level=level();
//        if(level.isClientSide){
//            level.addParticle(ParticleTypes.EXPLOSION,getX(),getY(),getZ(),0,0,0);
//        }else {
//            ServerLevel sLever=(ServerLevel) level;
//            sLever.sendParticles(ParticleTypes.EXPLOSION,getX(),getY(),getZ(),1,0,0,0,0);
//        }
    }


    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }
}
