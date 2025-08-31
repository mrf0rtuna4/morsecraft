package com.mrfortuna.morsecraft.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PaperTapeItem extends Item {

    public PaperTapeItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            if (stack.hasTag() && Objects.requireNonNull(stack.getTag()).contains("sequence")) {
                String seq = stack.getTag().getString("sequence");
                player.displayClientMessage(Component.translatable("subtitles.morsecraft.tape.sequence").append(seq), false);
            } else {
                player.displayClientMessage(Component.translatable("subtitles.morsecraft.tape.empty"), false);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
