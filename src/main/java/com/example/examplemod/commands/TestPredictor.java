package com.example.examplemod.commands;

import com.example.examplemod.Predictor;
import net.minecraft.world.phys.Vec3;

public class TestPredictor {
    public static void main(String[] args) {
        // 示例输入向量
        double dx = 5.0;
        double dy = 3.0;
        double dz = -2.0;

        // 调用预测方法
        Vec3 result = Predictor.getPredictedDirection(dx, dy, dz);

        // 输出结果
        System.out.println("Predicted direction:");
        System.out.println("vx = " + result.x);
        System.out.println("vy = " + result.y);
        System.out.println("vz = " + result.z);
    }
}
