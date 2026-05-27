package com.example.examplemod.items.mystery_assistant;

import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public final class MysteryAssistantModes {
    public static final String RANDOM_CRAFTING = "examplemod_mystery_assistant_random_crafting";
    public static final String ENTITY_CHAOS = "examplemod_mystery_assistant_entity_chaos";
    public static final String BLOCK_CHAOS = "examplemod_mystery_assistant_block_chaos";
    public static final String WEAPON_CHAOS = "examplemod_mystery_assistant_weapon_chaos";

    public static final String MESSAGE_PREFIX = "<神秘助手> ";
    public static final String BUSY_MESSAGE = "服务器繁忙，请稍后再试!";
    public static final String LIMIT_MESSAGE = "你已达到今日限额，请升级套餐!";
    public static final String PURCHASE_SUCCESS_MESSAGE = "正在思考...";
    public static final String CONSUMED_MESSAGE = "你已消耗一次免费额度!";

    private static final int PURCHASE_USES = 8;
    private static final int COOLDOWN_TICKS = 100;
    private static final List<String> KEYS = List.of(RANDOM_CRAFTING, ENTITY_CHAOS, BLOCK_CHAOS, WEAPON_CHAOS);

    private MysteryAssistantModes() {
    }

    public static void enable(Player player, String key) {
        purchase(player, key);
    }

    public static boolean isEnabled(Player player, String key) {
        return remainingUses(player, key) > 0;
    }

    public static int remainingUses(Player player, String key) {
        return player.getPersistentData().getInt(usesKey(key));
    }

    public static List<String> keys() {
        return KEYS;
    }

    public static PurchaseResult purchase(Player player, String key) {
        if (remainingUses(player, key) > 0) {
            return PurchaseResult.ACTIVE;
        }

        if (!consumeGoldBlock(player)) {
            sendMessage(player, LIMIT_MESSAGE);
            return PurchaseResult.NOT_ENOUGH_GOLD;
        }

        player.getPersistentData().putInt(usesKey(key), PURCHASE_USES);
        player.getPersistentData().putLong(cooldownKey(key), 0L);
        sendMessage(player, PURCHASE_SUCCESS_MESSAGE);
        return PurchaseResult.PURCHASED;
    }

    public static boolean canTrigger(Player player, String key) {
        return remainingUses(player, key) > 0 && player.level().getGameTime() >= cooldownUntil(player, key);
    }

    public static boolean ensureReady(Player player, String key) {
        if (remainingUses(player, key) <= 0) {
            return false;
        }

        if (player.level().getGameTime() < cooldownUntil(player, key)) {
            sendMessage(player, BUSY_MESSAGE);
            return false;
        }

        return true;
    }

    public static void recordTrigger(Player player, String key, String successMessage) {
        int remaining = remainingUses(player, key);
        if (remaining <= 0) {
            return;
        }

        player.getPersistentData().putInt(usesKey(key), remaining - 1);
        player.getPersistentData().putLong(cooldownKey(key), player.level().getGameTime() + COOLDOWN_TICKS);
        sendMessage(player, CONSUMED_MESSAGE);
        sendMessage(player, successMessage);
    }

    private static long cooldownUntil(Player player, String key) {
        return player.getPersistentData().getLong(cooldownKey(key));
    }

    private static boolean consumeGoldBlock(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(Items.GOLD_BLOCK)) {
                stack.shrink(1);
                player.getInventory().setChanged();
                return true;
            }
        }
        return false;
    }

    private static String usesKey(String key) {
        return key + "_remaining";
    }

    private static String cooldownKey(String key) {
        return key + "_cooldown_until";
    }

    private static void sendMessage(Player player, String message) {
        player.sendSystemMessage(Component.literal(MESSAGE_PREFIX).withStyle(ChatFormatting.YELLOW).append(Component.literal(message).withStyle(ChatFormatting.WHITE)));
    }

    public enum PurchaseResult {
        PURCHASED,
        ACTIVE,
        NOT_ENOUGH_GOLD
    }
}
