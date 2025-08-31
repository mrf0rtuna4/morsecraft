package com.mrfortuna.morsecraft.crafting;

import com.mrfortuna.morsecraft.Morsecraft;
import com.mrfortuna.morsecraft.items.TelegraphBlockItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ChargeTelegraphRecipe extends CustomRecipe {

    public ChargeTelegraphRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer inv, @NotNull Level level) {
        boolean foundTelegraph = false;
        boolean foundPaper = false;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof TelegraphBlockItem) {
                if (foundTelegraph) return false;
                foundTelegraph = true;
            } else if (stack.is(Items.PAPER)) {
                foundPaper = true;
            } else {
                return false;
            }
        }
        return foundTelegraph && foundPaper;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer inv, @NotNull RegistryAccess registryAccess) {
        ItemStack telegraph = ItemStack.EMPTY;
        int totalPaper = 0;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof TelegraphBlockItem) {
                telegraph = stack.copy();
            } else if (stack.is(Items.PAPER)) {
                totalPaper += stack.getCount();
            }
        }

        if (!telegraph.isEmpty() && totalPaper > 0) {
            int current = telegraph.getOrCreateTag().getInt("Paper");
            telegraph.getOrCreateTag().putInt("Paper", current + totalPaper);
            return telegraph;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Morsecraft.CHARGE_TELEGRAPH_RECIPE.get();
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> remain = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(Items.PAPER) || stack.getItem() instanceof TelegraphBlockItem) {
                inv.removeItem(i, stack.getCount());
                remain.set(i, ItemStack.EMPTY);
            }
        }

        return remain;
    }


    @Override
    public boolean isSpecial() {
        return true;
    }
}
