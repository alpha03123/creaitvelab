package com.example.examplemod.items.mystery_assistant;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class MysteryAssistantCrafting {
    private static final String PENDING_MESSAGE_KEY = "examplemod_mystery_assistant_pending_crafting_message";

    private MysteryAssistantCrafting() {
    }

    public static void enableRandomCrafting(Player player) {
        MysteryAssistantModes.enable(player, MysteryAssistantModes.RANDOM_CRAFTING);
    }

    public static boolean isRandomCraftingEnabled(Player player) {
        return MysteryAssistantModes.canTrigger(player, MysteryAssistantModes.RANDOM_CRAFTING);
    }

    public static ItemStack randomCraftingResult(Level level) {
        List<CraftingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING);
        if (recipes.isEmpty()) {
            return ItemStack.EMPTY;
        }

        for (int attempts = 0; attempts < recipes.size(); attempts++) {
            CraftingRecipe recipe = recipes.get(level.random.nextInt(recipes.size()));
            ItemStack result = recipe.getResultItem(level.registryAccess()).copy();
            if (!result.isEmpty() && result.isItemEnabled(level.enabledFeatures())) {
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    public static void rememberPendingMessage(Player player, ItemStack result, CraftingContainer container) {
        player.getPersistentData().putString(PENDING_MESSAGE_KEY, craftingSuccessMessage(result, container));
    }

    public static void clearPendingMessage(Player player) {
        player.getPersistentData().remove(PENDING_MESSAGE_KEY);
    }

    public static String consumePendingMessage(Player player, ItemStack fallbackResult) {
        String message = player.getPersistentData().getString(PENDING_MESSAGE_KEY);
        clearPendingMessage(player);
        if (!message.isBlank()) {
            return message;
        }
        return "太好了!我敢保证" + itemName(fallbackResult) + "一定是这些材料做的!";
    }

    private static String craftingSuccessMessage(ItemStack result, CraftingContainer container) {
        return "太好了!我敢保证" + itemName(result) + "一定是" + describeIngredients(container) + "做的!";
    }

    private static String describeIngredients(CraftingContainer container) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack stack = container.getItem(slot);
            if (!stack.isEmpty()) {
                counts.merge(itemName(stack), stack.getCount(), Integer::sum);
            }
        }

        if (counts.isEmpty()) {
            return "空气";
        }

        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            if (!builder.isEmpty()) {
                builder.append("和");
            }
            builder.append(entry.getValue()).append("个").append(entry.getKey());
        }
        return builder.toString();
    }

    private static String itemName(ItemStack stack) {
        if (stack.isEmpty()) {
            return "空气";
        }
        return stack.getHoverName().getString();
    }
}
