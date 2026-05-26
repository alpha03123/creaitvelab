package com.example.examplemod.items.red_envelope;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SmallRedEnvelope extends Item {
    private static final int MAX_ITEMS = 4;  // 最大存储物品数量

    public SmallRedEnvelope() {
        super(new Item.Properties().stacksTo(1));  // 设置物品最多只能堆叠1个
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // 检查 Shift + 右键，封闭红包
        if (player.isShiftKeyDown()) {
            if (stack.hasTag() && stack.getTag().getBoolean("IsFixed")) {
                return InteractionResultHolder.success(stack);
            }
            // 封闭红包
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

        return InteractionResultHolder.success(stack);
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
