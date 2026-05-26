package com.example.united.meterite;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Random;

//陨星

public class MeteoriteItem extends Item {
    public MeteoriteItem(Properties pProperties) {
        super(pProperties);
    }
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        for (int i = 0; i < 5; i++) {
                Meteorite meteorite = new Meteorite(EntityType.ARMOR_STAND, pLevel,
                        pPlayer.position());
                meteorite.setPos(
                        pPlayer.position().x + new Random().nextInt(31),
                        pPlayer.position().y+50 ,
                        pPlayer.position().z + new Random().nextInt(31));
               pLevel.addFreshEntity(meteorite);
            }
        return InteractionResultHolder.consume(itemstack);
    }
    

}
