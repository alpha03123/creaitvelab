package com.example.examplemod.entities.Turrets;

import com.example.examplemod.entities.BaseComplexEntity;
import com.example.examplemod.entities.bullet.CommonBullet;
import com.example.examplemod.registry.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Turret extends BaseComplexEntity {
    public Turret(EntityType<?> pEntityType, Level pLevel, Vec3 pos) {
        super(pEntityType, pLevel, pos);
        this.lifetime=200;
    }
    @Override
    public void tick() {
        super.tick();
        Level level=level();
        if (!level.isClientSide) {

            List<Player> players = level.getEntitiesOfClass(Player.class,
                    new AABB(this.blockPosition()).inflate(20));
            for (Player player : players) {
                // 记录数据
                double dx = player.getX() - this.getX();
                double dy = player.getY() - this.getY();
                double dz = player.getZ() - this.getZ();
                saveTrainingData(dx, dy,dz);

                // 使用子弹
                CommonBullet commonBullet=new CommonBullet(ModEntities.BASE_COMPLEX_ENTITY.get(), level,position(),new Vec3(dx,dy,dz).scale(0.05f));
                level.addFreshEntity(commonBullet);
            }

        }
    }
    private void saveTrainingData(double dx, double dy,double dz) {
        String path = "training_data.csv";  // 建议路径放在 `run` 目录下
        String line = dx + "," +dy + ","+ dz + "\n";
        try {
            Files.write(Paths.get(path), line.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("数据保存失败：" + e.getMessage());
        }
    }
}
