package com.mrfortuna.morsecraft.neoforge.platform;

import com.mrfortuna.morsecraft.api.PlatformApi;
import com.mrfortuna.morsecraft.blocks.TelegraphKeyBlock;
import com.mrfortuna.morsecraft.blocks.TelegraphKeyBlockEntity;
import com.mrfortuna.morsecraft.crafting.ChargeTelegraphRecipe;
import com.mrfortuna.morsecraft.gui.TelegraphKeyMenu;
import com.mrfortuna.morsecraft.items.PaperTapeItem;
import com.mrfortuna.morsecraft.items.TelegraphBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NeoForgePlatform implements PlatformApi.IPlatform {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, PlatformApi.MOD_ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, PlatformApi.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PlatformApi.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PlatformApi.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, PlatformApi.MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, PlatformApi.MOD_ID);
    public static final DeferredRegister<MenuType<?>> CONTAINERS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, PlatformApi.MOD_ID);

    public static final RegistryObject<Block> TELEGRAPH_KEY_BLOCK_REG =
            BLOCKS.register("telegraph_key", () -> new TelegraphKeyBlock(
                    BlockBehaviour.Properties.of()
                            .strength(1.0f)
                            .noOcclusion()
            ));

    public static final RegistryObject<Item> TELEGRAPH_KEY_ITEM_REG =
            ITEMS.register("telegraph_key", () -> new TelegraphBlockItem(
                    TELEGRAPH_KEY_BLOCK_REG.get(),
                    new Item.Properties().stacksTo(1)
            ));

    public static final RegistryObject<Item> PAPER_TAPE_ITEM_REG =
            ITEMS.register("paper_tape", () -> new PaperTapeItem(
                    new Item.Properties().stacksTo(1)
            ));

    public static final RegistryObject<BlockEntityType<?>> TELEGRAPH_KEY_ENTITY_REG =
            BLOCK_ENTITIES.register("telegraph_key", () ->
                    BlockEntityType.Builder.of(TelegraphKeyBlockEntity::new, TELEGRAPH_KEY_BLOCK_REG.get()).build(null)
            );

    public static final RegistryObject<CreativeModeTab> MORSECRAFT_TAB_REG =
            CREATIVE_MODE_TABS.register("morsecraft_tab", () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> TELEGRAPH_KEY_ITEM_REG.get().getDefaultInstance())
                    .title(Component.translatable("creativetab.morsecraft_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(TELEGRAPH_KEY_ITEM_REG.get());
                        output.accept(PAPER_TAPE_ITEM_REG.get());
                    }).build());

    public static final RegistryObject<RecipeSerializer<?>> CHARGE_TELEGRAPH_RECIPE_REG =
            RECIPE_SERIALIZERS.register("charge_telegraph",
                    () -> new net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer<>(ChargeTelegraphRecipe::new));

    public static final RegistryObject<SoundEvent> TELEGRAPH_DOT_REG =
            SOUND_EVENTS.register("telegraph_dot",
                    () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(PlatformApi.MOD_ID, "telegraph_dot")));

    public static final RegistryObject<SoundEvent> TELEGRAPH_DASH_REG =
            SOUND_EVENTS.register("telegraph_dash",
                    () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(PlatformApi.MOD_ID, "telegraph_dash")));

    public static final RegistryObject<MenuType<?>> TELEGRAPH_KEY_MENU_REG =
            CONTAINERS.register("telegraph_key_menu",
                    () -> IForgeMenuType.create((windowId, inv, data) -> {
                        BlockPos pos = data.readBlockPos();
                        net.minecraft.world.level.block.entity.BlockEntity be = inv.player.level().getBlockEntity(pos);
                        if (be instanceof TelegraphKeyBlockEntity telegraph) {
                            return new TelegraphKeyMenu(windowId, inv, telegraph);
                        }
                        return null;
                    }));

    static {
        PlatformApi.setImpl(new NeoForgePlatform());
    }

    @Override
    public void init() {
        PlatformApi.TELEGRAPH_KEY_BLOCK = TELEGRAPH_KEY_BLOCK_REG.get();
        PlatformApi.TELEGRAPH_KEY_ITEM = TELEGRAPH_KEY_ITEM_REG.get();
        PlatformApi.PAPER_TAPE_ITEM = PAPER_TAPE_ITEM_REG.get();
        PlatformApi.TELEGRAPH_KEY_ENTITY = TELEGRAPH_KEY_ENTITY_REG.get();
        PlatformApi.MORSECRAFT_TAB = MORSECRAFT_TAB_REG.get();
        PlatformApi.CHARGE_TELEGRAPH_RECIPE = CHARGE_TELEGRAPH_RECIPE_REG.get();
        PlatformApi.TELEGRAPH_DOT = TELEGRAPH_DOT_REG.get();
        PlatformApi.TELEGRAPH_DASH = TELEGRAPH_DASH_REG.get();
        PlatformApi.TELEGRAPH_KEY_MENU = TELEGRAPH_KEY_MENU_REG.get();
    }

    @Override
    public void registerEvents() {
        // ...
    }

    @Override
    public void openScreen(net.minecraft.server.level.ServerPlayer player, MenuProvider provider, BlockPos pos) {
        net.neoforged.neoforge.network.NetworkHooks.openScreen(
                player,
                provider,
                buf -> buf.writeBlockPos(pos)
        );
    }
}
