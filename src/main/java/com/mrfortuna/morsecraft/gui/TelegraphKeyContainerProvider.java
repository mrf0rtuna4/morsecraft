package com.mrfortuna.morsecraft.gui;

import com.mrfortuna.morsecraft.blocks.TelegraphKeyBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;

public class TelegraphKeyContainerProvider implements MenuProvider {

    private final BlockPos pos;
    private final TelegraphKeyBlockEntity telegraph;

    public TelegraphKeyContainerProvider(BlockPos pos, TelegraphKeyBlockEntity telegraph) {
        this.pos = pos;
        this.telegraph = telegraph;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.morsecraft.telegraph_key");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new TelegraphKeyMenu(id, playerInventory, telegraph);
    }

}
