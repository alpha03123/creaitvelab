package com.example.examplemod.items.red_envelope;

import com.example.particlecomplex.utils.entity_utils.EntityGetter;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BigRedEnvelope extends Item {
    private static final int MAX_ITEMS = 9;  // 最大存储物品数量

    public BigRedEnvelope() {
        super(new Item.Properties().stacksTo(1));  // 设置物品最多只能堆叠1个
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // 检查 Shift + 右键，封闭红包
        if (player.isShiftKeyDown()) {
            if (stack.hasTag() && stack.getTag().getBoolean("IsFixed")) {
                // 如果红包已封闭，释放其中的实体
                if (!level.isClientSide) {
                    releaseEntities(stack, player);
                }
                return InteractionResultHolder.success(stack);
            }

            // 如果红包未封闭，封闭红包并捕捉实体
            CompoundTag nbt = stack.getOrCreateTag();
            nbt.putBoolean("IsFixed", true);
            return InteractionResultHolder.success(stack);
        }

        // 如果是封闭状态，打开红包
        if (stack.hasTag() && stack.getTag().getBoolean("IsFixed")) {
            if (!level.isClientSide) {
                openRedEnvelope(player, stack);
            }
            return InteractionResultHolder.success(stack);
        }

        // 检查副手是否有物品
        if (!level.isClientSide) {
            ItemStack offhandItem = player.getOffhandItem();
            if (!offhandItem.isEmpty()) {
                putItemInEnvelope(stack, offhandItem);
            }
        }

        // 检查玩家是否右键目标实体，且目标实体符合血量条件
        if (!level.isClientSide) {
            Entity targetEntity = getTargetEntity(player, level);
            if (targetEntity instanceof Entity && canCaptureEntity(targetEntity)) {
                captureEntity(stack, targetEntity);
            }
        }

        return InteractionResultHolder.success(stack);
    }

    private Entity getTargetEntity(Player player, Level level) {
        return EntityGetter.getClosestEntityInSight(player, 5d, false);
    }

    private boolean canCaptureEntity(Entity entity) {
        // 判断目标实体血量是否符合条件：低于5点血或者低于生命总值的10%
        if(entity instanceof LivingEntity livingEntity){
            double healthPercentage = livingEntity.getHealth() / livingEntity.getMaxHealth();
            return livingEntity.getHealth() <= 5 || healthPercentage <= 0.2;}
        else {
            return true;
        }

    }

    private void captureEntity(ItemStack stack, Entity entity) {
        // 如果满足条件，抓取并存储实体
        CompoundTag nbt = stack.getOrCreateTag();
        ListTag entities = nbt.getList("StoredEntities", 10);  // 存储实体的列表

        if (entities.size() < MAX_ITEMS && stack.getOrCreateTag().getList("StoredItems", 10).isEmpty()) {
            // 创建实体的NBT数据
            CompoundTag entityNBT = new CompoundTag();
            entity.save(entityNBT);  // 保存实体数据

            // 存储实体
            entities.add(entityNBT);
            nbt.put("StoredEntities", entities);
            nbt.putBoolean("IsFixed", true);

            // 弹出实体
            entity.remove(Entity.RemovalReason.KILLED);
        }
    }

    private void putItemInEnvelope(ItemStack stack, ItemStack offhandItem) {
        CompoundTag nbt = stack.getOrCreateTag();
        ListTag items = nbt.getList("StoredItems", 10);  // 10是CompoundTag的标识符

        // 如果红包未满，则可以放入物品
        if (items.size() < MAX_ITEMS) {
            // 记录副手的物品
            CompoundTag itemNBT = offhandItem.save(new CompoundTag());
            items.add(itemNBT);


            // 更新红包的存储
            nbt.put("StoredItems", items);

            // 消耗副手的物品
            offhandItem.shrink(offhandItem.getCount());
        }
    }

    private void openRedEnvelope(Player player, ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        ListTag storedItems = nbt.getList("StoredItems", 10);  // 10是CompoundTag的标识符

        // 掉落存储的物品
        for (int i = 0; i < storedItems.size(); i++) {
            CompoundTag itemNBT = storedItems.getCompound(i);
            ItemStack itemStack = ItemStack.of(itemNBT); // 从NBT创建ItemStack
            player.drop(itemStack, false); // 掉落物品
        }

        // 清空存储物品
        nbt.remove("StoredItems");
        nbt.putBoolean("IsFixed", false);
        stack.setDamageValue(stack.getMaxDamage()); // 破坏物品

        // 释放捕捉的实体
        releaseEntities(stack, player);
    }

    private void releaseEntities(ItemStack stack, Player player) {
        CompoundTag nbt = stack.getOrCreateTag();
        ListTag storedEntities = nbt.getList("StoredEntities", 10);  // 10是CompoundTag的标识符

        for (int i = 0; i < storedEntities.size(); i++) {
            CompoundTag entityNBT = storedEntities.getCompound(i);

            // 使用 loadEntityRecursive 并传入一个 Identity Function，实体不做任何修改
            Entity entity = EntityType.loadEntityRecursive(entityNBT, player.level(), e -> e);

            if (entity != null) {
                player.level().addFreshEntity(entity);  // 放入世界中
                entity.setPos(new Vec3(player.getX(),player.getY(),player.getZ()));
            }
        }

        // 清空存储的实体
        nbt.remove("StoredEntities");
        nbt.putBoolean("IsFixed", false);  // 解除封闭状态
    }


    @Override
    public void appendHoverText(ItemStack stack, Level level, java.util.List<Component> tooltip, net.minecraft.world.item.TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if(stack.hasTag() && stack.getTag().getBoolean("IsFixed")){
            tooltip.add(Component.literal("这个红包已经装好了哦~猜猜里面有什么~").withStyle(ChatFormatting.RED));
            return;
        }
        if (stack.hasTag()) {
            CompoundTag nbt = stack.getTag();
            if (nbt != null && nbt.contains("StoredItems")) {
                ListTag storedItems = nbt.getList("StoredItems", 10);  // 10是CompoundTag的标识符
                if (!storedItems.isEmpty()) {
                    StringBuilder contents = new StringBuilder("里面存了: ");
                    for (int i = 0; i < storedItems.size(); i++) {
                        CompoundTag itemNBT = storedItems.getCompound(i);
                        ItemStack itemStack = ItemStack.of(itemNBT);
                        contents.append(itemStack.getItem().getName(itemStack).getString())
                                .append(" x")
                                .append(itemStack.getCount())
                                .append(", ");
                    }
                    tooltip.add(Component.literal(contents.toString()));
                } else {
                    tooltip.add(Component.literal("空"));
                }
            }
        }
    }
}
