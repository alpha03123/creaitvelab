package com.example.examplemod;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

public class AttackState {
    public final Player player;
    public final Level world;
    public int delayTicks;

    public AttackState(Player player, Level world, int delayTicks) {
        this.player = player;
        this.world = world;
        this.delayTicks = delayTicks;
    }
}
