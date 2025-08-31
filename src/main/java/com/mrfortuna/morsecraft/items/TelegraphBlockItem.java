package com.mrfortuna.morsecraft.items;

import com.mrfortuna.morsecraft.blocks.TelegraphKeyBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TelegraphBlockItem extends BlockItem {
    public TelegraphBlockItem(net.minecraft.world.level.block.Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public @NotNull InteractionResult place(@NotNull BlockPlaceContext context) {
        InteractionResult res = super.place(context);
        if (res.consumesAction() && !context.getLevel().isClientSide()) {
            BlockPos pos = context.getClickedPos();
            Level level = context.getLevel();
            ItemStack stack = context.getItemInHand();
            if (level.getBlockEntity(pos) instanceof TelegraphKeyBlockEntity te) {
                if (stack.hasTag()) {
                    assert stack.getTag() != null;
                    if (stack.getTag().contains("paper_count")) {
                        int cnt = stack.getTag().getInt("paper_count");
                        te.setPaperCount(cnt);
                    }
                }
            }
        }
        return res;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        int paper = stack.getOrCreateTag().getInt("Paper");
        tooltip.add(Component.translatable("subtitles.morsecraft.paper").append(String.valueOf(paper)).withStyle(ChatFormatting.GRAY));

        if (stack.hasTag()) {
            assert stack.getTag() != null;
            if (stack.getTag().contains("PrintedText")) {
                String printed = stack.getTag().getString("PrintedText");
                if (!printed.isEmpty()) {
                    String preview = printed.length() > 40 ? printed.substring(0, 40) + "..." : printed;
                    tooltip.add(Component.translatable("subtitles.morsecraft.tape").append(preview).withStyle(ChatFormatting.DARK_GREEN));
                }
            }
        }
    }
}
