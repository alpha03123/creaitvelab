package com.example.examplemod.mixin;

import com.example.examplemod.items.mystery_assistant.MysteryAssistantCrafting;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(CraftingMenu.class)
public class CraftingMenuMixin {
    @Inject(method = "slotChangedCraftingGrid", at = @At("HEAD"), cancellable = true)
    private static void examplemod$randomizeCraftingResult(AbstractContainerMenu menu, Level level, Player player, CraftingContainer container, ResultContainer resultContainer, CallbackInfo ci) {
        if (level.isClientSide || !MysteryAssistantCrafting.isRandomCraftingEnabled(player)) {
            return;
        }

        ServerPlayer serverPlayer = (ServerPlayer) player;
        ItemStack result = ItemStack.EMPTY;
        Optional<CraftingRecipe> recipe = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, container, level);
        if (recipe.isPresent() && resultContainer.setRecipeUsed(level, serverPlayer, recipe.get())) {
            ItemStack originalResult = recipe.get().assemble(container, level.registryAccess());
            if (!originalResult.isEmpty() && originalResult.isItemEnabled(level.enabledFeatures())) {
                result = MysteryAssistantCrafting.randomCraftingResult(level);
                if (!result.isEmpty()) {
                    MysteryAssistantCrafting.rememberPendingMessage(player, result, container);
                } else {
                    MysteryAssistantCrafting.clearPendingMessage(player);
                }
            }
        } else {
            MysteryAssistantCrafting.clearPendingMessage(player);
        }

        resultContainer.setItem(0, result);
        menu.setRemoteSlot(0, result);
        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, result));
        ci.cancel();
    }
}
