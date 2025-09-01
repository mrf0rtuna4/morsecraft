package com.mrfortuna.morsecraft.gui;

import com.mrfortuna.morsecraft.Morsecraft;
import com.mrfortuna.morsecraft.blocks.TelegraphKeyBlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static com.mrfortuna.morsecraft.blocks.TelegraphKeyBlock.POWERED;

public class TelegraphKeyMenu extends AbstractContainerMenu {

    private final TelegraphKeyBlockEntity telegraph;

    public TelegraphKeyMenu(int id, Inventory playerInventory, TelegraphKeyBlockEntity telegraph) {
        super(Morsecraft.TELEGRAPH_KEY_MENU.get(), id);
        this.telegraph = telegraph;
    }

    public TelegraphKeyBlockEntity getTelegraph() {
        return telegraph;
    }

    public InteractionResult inputSymbol(char symbol, Player player) {
        if (telegraph != null) {
            Level level = player.level();
            telegraph.recordSymbol(level, telegraph.getBlockPos(), symbol);

            if (level.isClientSide) {
                if (telegraph.getPaperCount() > 0) {
                    BlockState state = telegraph.getBlockState();
                    if (!state.getValue(POWERED)) {
                        level.setBlock(telegraph.getBlockPos(), state.setValue(POWERED, Boolean.TRUE), 3);
                    }
                    if (symbol == '.') {
                        player.playSound(Morsecraft.TELEGRAPH_DOT.get(), 1.0f, 1.0f);
                    } else if (symbol == '-') {
                        player.playSound(Morsecraft.TELEGRAPH_DASH.get(), 1.0f, 1.0f);
                    }
                }
            }
        }

        return InteractionResult.SUCCESS;
    }


    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return telegraph != null && player.distanceToSqr(telegraph.getBlockPos().getCenter()) < 64.0;
    }
}
