package com.example.examplemod.advanced_functions.extend;

import com.example.examplemod.ExampleMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID)
public class MotionParticle {

    private static final List<MotionParticle> activeParticles = new ArrayList<>();
    private static float a_x;
    private static float a_y;
    private static float a_z;
    private String statue;


    private long startTime;
    private Particle particle;
    private float init_x;
    private float init_y;
    private float init_z;
    private Expression xSpeedExpression;
    private Expression ySpeedExpression;
    private Expression zSpeedExpression;

    private final ParticleEngine engine = Minecraft.getInstance().particleEngine;
    private float vx;
    private float vy;
    private float vz;

    public MotionParticle() {
        this.particle = null;
        this.startTime = 0;
        this.init_x = 0;
        this.init_y = 0;
        this.init_z = 0;
        a_x=0;
        a_y=0;
        a_z=0;
        this.vx=0;
        this.vy=0;
        this.vz=0;
        this.statue="";
    }

    // 重载一,通过表达式控制粒子运动速度
    public static MotionParticle createMotionParticle(ParticleOptions type, float x, float y, float z,
                                                      Expression xSpeedExpression, Expression ySpeedExpression, Expression zSpeedExpression) {
        MotionParticle motionParticle = new MotionParticle();
        motionParticle.statue="expression";
        motionParticle.create_particle(type, x, y, z);
        motionParticle.setXSpeedExpression(xSpeedExpression);
        motionParticle.setYSpeedExpression(ySpeedExpression);
        motionParticle.setZSpeedExpression(zSpeedExpression);
        activeParticles.add(motionParticle);
        return motionParticle;
    }

    // 重载二,通过加速度与速度共同控制运动
    public static MotionParticle createMotionParticle(ParticleOptions type, float x, float y, float z,float vx,float vy,float vz,float ax,float ay,float az) {
        MotionParticle motionParticle = new MotionParticle();
        motionParticle.statue="acceleration";
        motionParticle.create_particle(type, x, y, z);
        motionParticle.setSpeed(vx,vy,vz);
        motionParticle.setAcceleration(ax,ay,az);
        activeParticles.add(motionParticle);
        return motionParticle;
    }

    // 重载三,通过加速度(不填速度，速度默认为0)来控制运动
    public static MotionParticle createMotionParticle(ParticleOptions type, float x, float y, float z,float ax,float ay,float az) {
        MotionParticle motionParticle = new MotionParticle();
        motionParticle.statue="acceleration";
        motionParticle.create_particle(type, x, y, z);
        motionParticle.setSpeed(0,0,0);
        motionParticle.setAcceleration(ax,ay,az);
        // 将实例添加到活动粒子列表中
        activeParticles.add(motionParticle);
        return motionParticle;
    }

    // 创建粒子
    private void create_particle(ParticleOptions type, float x, float y, float z) {
        this.particle = engine.createParticle(type, x, y, z, 0, 0, 0);
        this.startTime = getTime();
        engine.add(this.particle);
        this.init_x = x;
        this.init_y = y;
        this.init_z = z;
    }

    public void setSpeed(float vx, float vy, float vz) {
        this.vx=vx;
        this.vy=vy;
        this.vz=vz;
        this.particle.setParticleSpeed(vx, vy, vz);
    }
    public void setAcceleration(float ax,float ay,float az){
        a_x=ax;
        a_y=ay;
        a_z=az;
    }

    private long  getTime() {
        return Minecraft.getInstance().level.getGameTime();
    }

    public void setXSpeedExpression(Expression expression) {
        this.xSpeedExpression = expression;
    }

    public void setYSpeedExpression(Expression expression) {
        this.ySpeedExpression = expression;
    }

    public void setZSpeedExpression(Expression expression) {
        this.zSpeedExpression = expression;
    }

    public long getSurviveTime() {
        return getTime() - this.startTime;
    }

    private float get_relative_X() {
        return (float) this.particle.getPos().x - init_x;
    }

    private float get_relative_Y() {
        return (float) this.particle.getPos().y - init_y;
    }

    private float get_relative_Z() {
        return (float) this.particle.getPos().z - init_z;
    }

    // 更新粒子速度
    private void update_by_expression() {
        if (this.particle != null && this.particle.isAlive()) {  // 检查粒子是否已被销毁
            long currentTime = getSurviveTime();
            float r_x = get_relative_X();
            float r_y = get_relative_Y();
            float r_z = get_relative_Z();

            // 分别计算 x、y、z 方向的速度
            float vx = xSpeedExpression.apply(currentTime, r_x, r_y, r_z);
            float vy = ySpeedExpression.apply(currentTime, r_x, r_y, r_z);
            float vz = zSpeedExpression.apply(currentTime, r_x, r_y, r_z);

            setSpeed(vx, vy, vz);
        } else {
            // 标记粒子为已移除
            this.particle = null;
        }
    }


    private void update_by_acceleration() {
        if (this.particle != null && this.particle.isAlive()) {  // 检查粒子是否已被销毁
            setSpeed(this.vx+a_x,this.vy+a_y,this.vz+a_z);

        } else {
            // 标记粒子为已移除
            this.particle = null;
        }
    }
    public boolean isFinished() {
        // 检查粒子是否为 null 或者粒子是否已经被标记为移除
        return this.particle == null || !this.particle.isAlive();
    }

    public void setParticleLifetime(int lifetime){
        this.particle.setLifetime(lifetime);
    }


    // 在每个游戏刻更新所有活动粒子
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            // 使用迭代器来遍历并移除元素
            Iterator<MotionParticle> iterator = activeParticles.iterator();
            while (iterator.hasNext()) {
                MotionParticle particle = iterator.next();
                if(Objects.equals(particle.statue, "expression")){
                    particle.update_by_expression();}
                else
                { particle.update_by_acceleration();}
                if (particle.isFinished()) {
                    iterator.remove();  // 使用迭代器的 remove 方法来移除
                }
            }
        }
    }

}
