package com.mrfortuna.morsecraft.api;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.sound.SoundEvent;

public abstract class PlatformApi {

    public static final String MOD_ID = "morsecraft";

    public static Block TELEGRAPH_KEY_BLOCK;
    public static Item TELEGRAPH_KEY_ITEM;
    public static Item PAPER_TAPE_ITEM;
    public static BlockEntityType<?> TELEGRAPH_KEY_ENTITY;
    public static CreativeModeTab MORSECRAFT_TAB;
    public static RecipeSerializer<?> CHARGE_TELEGRAPH_RECIPE;
    public static SoundEvent TELEGRAPH_DOT;
    public static SoundEvent TELEGRAPH_DASH;
    public static net.minecraft.world.inventory.MenuType<?> TELEGRAPH_KEY_MENU;

    private static IPlatform impl;

    public static void setImpl(IPlatform impl) {
        PlatformApi.impl = impl;
    }

    public static void init() {
        if (impl != null) impl.init();
    }

    public static void registerEvents() {
        if (impl != null) impl.registerEvents();
    }

    public static void openScreen(ServerPlayer player, MenuProvider provider, BlockPos pos) {
        if (impl != null) impl.openScreen(player, provider, pos);
    }

    public static BlockBehaviour.Properties blockProperties() {
        return BlockBehaviour.Properties.of();
    }

    public static net.minecraft.resources.ResourceLocation id(String path) {
        return net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public interface IPlatform {
        void init();
        void registerEvents();
        void openScreen(ServerPlayer player, MenuProvider provider, BlockPos pos);
    }
}
