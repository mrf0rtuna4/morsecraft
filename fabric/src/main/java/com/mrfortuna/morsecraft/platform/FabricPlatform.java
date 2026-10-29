package com.mrfortuna.morsecraft.fabric.platform;

import com.mrfortuna.morsecraft.api.PlatformApi;
import com.mrfortuna.morsecraft.blocks.TelegraphKeyBlock;
import com.mrfortuna.morsecraft.blocks.TelegraphKeyBlockEntity;
import com.mrfortuna.morsecraft.crafting.ChargeTelegraphRecipe;
import com.mrfortuna.morsecraft.gui.TelegraphKeyMenu;
import com.mrfortuna.morsecraft.items.PaperTapeItem;
import com.mrfortuna.morsecraft.items.TelegraphBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.ByteBufUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.SimpleScreenHandlerFactory;

public class FabricPlatform implements PlatformApi.IPlatform {
    public static Block TELEGRAPH_KEY_BLOCK;
    public static Item TELEGRAPH_KEY_ITEM;
    public static Item PAPER_TAPE_ITEM;
    public static BlockEntityType<?> TELEGRAPH_KEY_ENTITY;
    public static CreativeModeTab MORSECRAFT_TAB;
    public static RecipeSerializer<?> CHARGE_TELEGRAPH_RECIPE;
    public static SoundEvent TELEGRAPH_DOT;
    public static SoundEvent TELEGRAPH_DASH;
    public static MenuType<?> TELEGRAPH_KEY_MENU;

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(PlatformApi.MOD_ID, path);
    }

    public static void init() {
        TELEGRAPH_KEY_BLOCK = new TelegraphKeyBlock(
                BlockBehaviour.Properties.of()
                        .strength(1.0f)
                        .noOcclusion()
        );
        Registries.BLOCK.register(id("telegraph_key"), TELEGRAPH_KEY_BLOCK);

        TELEGRAPH_KEY_ITEM = new TelegraphBlockItem(
                TELEGRAPH_KEY_BLOCK,
                new Item.Properties().stacksTo(1)
        );
        Registries.ITEM.register(id("telegraph_key"), TELEGRAPH_KEY_ITEM);

        PAPER_TAPE_ITEM = new PaperTapeItem(
                new Item.Properties().stacksTo(1)
        );
        Registries.ITEM.register(id("paper_tape"), PAPER_TAPE_ITEM);

        TELEGRAPH_KEY_ENTITY = net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.create(
                TelegraphKeyBlockEntity::new,
                TELEGRAPH_KEY_BLOCK
        ).build(null);
        Registries.BLOCK_ENTITY_TYPE.register(id("telegraph_key"), TELEGRAPH_KEY_ENTITY);

        MORSECRAFT_TAB = CreativeModeTab.builder()
                .withTabsBefore(CreativeModeTabs.COMBAT)
                .icon(() -> TELEGRAPH_KEY_ITEM.getDefaultInstance())
                .title(Component.translatable("creativetab.morsecraft_tab"))
                .displayItems((parameters, output) -> {
                    output.accept(TELEGRAPH_KEY_ITEM);
                    output.accept(PAPER_TAPE_ITEM);
                }).build();
        Registries.CREATIVE_MODE_TAB.register(id("morsecraft_tab"), MORSECRAFT_TAB);

        CHARGE_TELEGRAPH_RECIPE = new net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer<>(ChargeTelegraphRecipe::new);
        Registries.RECIPE_SERIALIZER.register(id("charge_telegraph"), CHARGE_TELEGRAPH_RECIPE);

        TELEGRAPH_DOT = SoundEvent.createVariableRangeEvent(id("telegraph_dot"));
        TELEGRAPH_DASH = SoundEvent.createVariableRangeEvent(id("telegraph_dash"));
        Registries.SOUND_EVENT.register(id("telegraph_dot"), TELEGRAPH_DOT);
        Registries.SOUND_EVENT.register(id("telegraph_dash"), TELEGRAPH_DASH);

        TELEGRAPH_KEY_MENU = new MenuType<>(
                (windowId, inv, data) -> {
                    BlockPos pos = data.readBlockPos();
                    net.minecraft.world.level.block.entity.BlockEntity be = inv.player.level().getBlockEntity(pos);
                    if (be instanceof TelegraphKeyBlockEntity telegraph) {
                        return new TelegraphKeyMenu(windowId, inv, telegraph);
                    }
                    return null;
                },
                null
        );
        Registries.MENU_TYPE.register(id("telegraph_key_menu"), TELEGRAPH_KEY_MENU);

        ScreenHandlerRegistry.registerSync(id("telegraph_key_menu"), (SimpleScreenHandlerFactory) (syncId, inventory, data) -> {
            BlockPos pos = data.readBlockPos();
            net.minecraft.world.level.block.entity.BlockEntity be = inventory.player.level().getBlockEntity(pos);
            if (be instanceof TelegraphKeyBlockEntity telegraph) {
                return new TelegraphKeyMenu(syncId, inventory, telegraph);
            }
            return null;
        });

        PlatformApi.TELEGRAPH_KEY_BLOCK = TELEGRAPH_KEY_BLOCK;
        PlatformApi.TELEGRAPH_KEY_ITEM = TELEGRAPH_KEY_ITEM;
        PlatformApi.PAPER_TAPE_ITEM = PAPER_TAPE_ITEM;
        PlatformApi.TELEGRAPH_KEY_ENTITY = TELEGRAPH_KEY_ENTITY;
        PlatformApi.MORSECRAFT_TAB = MORSECRAFT_TAB;
        PlatformApi.CHARGE_TELEGRAPH_RECIPE = CHARGE_TELEGRAPH_RECIPE;
        PlatformApi.TELEGRAPH_DOT = TELEGRAPH_DOT;
        PlatformApi.TELEGRAPH_DASH = TELEGRAPH_DASH;
        PlatformApi.TELEGRAPH_KEY_MENU = TELEGRAPH_KEY_MENU;
    }

    @Override
    public void registerEvents() {
        // ...
    }

    @Override
    public void openScreen(net.minecraft.server.level.ServerPlayer player, MenuProvider provider, BlockPos pos) {
        FriendlyByteBuf buf = new FriendlyByteBuf(ByteBufUtils.DEFAULT);
        buf.writeBlockPos(pos);
        player.openHandledScreen(TELEGRAPH_KEY_MENU, buf);
    }
}
