package com.mrfortuna.morsecraft.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.core.component.DataComponents;
import org.jetbrains.annotations.NotNull;

public class PaperTapeItem extends Item {

    public PaperTapeItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            String seq = stack.getOrDefault(DataComponents.CUSTOM_DATA, net.minecraft.nbt.CompoundTag.EMPTY).getString("sequence");
            
            if (!seq.isEmpty()) {
                player.displayClientMessage(Component.translatable("subtitles.morsecraft.tape.sequence").append(seq), false);
            } else {
                player.displayClientMessage(Component.translatable("subtitles.morsecraft.tape.empty"), false);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
