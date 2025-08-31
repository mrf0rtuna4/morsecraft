package com.mrfortuna.morsecraft;

import com.mrfortuna.morsecraft.blocks.TelegraphKeyBlock;
import com.mrfortuna.morsecraft.blocks.TelegraphKeyBlockEntity;
import com.mrfortuna.morsecraft.crafting.ChargeTelegraphRecipe;
import com.mrfortuna.morsecraft.items.TelegraphBlockItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(Morsecraft.MODID)
public class Morsecraft {

    public static final String MODID = "morsecraft";

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Morsecraft.MODID);

    public static final RegistryObject<SoundEvent> TELEGRAPH_DOT = SOUND_EVENTS.register("telegraph_dot",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Morsecraft.MODID, "telegraph_dot")));

    public static final RegistryObject<SoundEvent> TELEGRAPH_DASH = SOUND_EVENTS.register("telegraph_dash",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Morsecraft.MODID, "telegraph_dash")));

    public static final RegistryObject<Block> TELEGRAPH_KEY = BLOCKS.register("telegraph_key",
            () -> new TelegraphKeyBlock(Block.Properties.of()
                    .strength(1.0f)
                    .noOcclusion()));

    public static final RegistryObject<Item> TELEGRAPH_KEY_ITEM = ITEMS.register("telegraph_key",
            () -> new TelegraphBlockItem(TELEGRAPH_KEY.get(), new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> PAPER_TAPE_ITEM = ITEMS.register("paper_tape",
            () -> new com.mrfortuna.morsecraft.items.PaperTapeItem(
                    new Item.Properties().stacksTo(1)
            ));

    public static final RegistryObject<RecipeSerializer<ChargeTelegraphRecipe>> CHARGE_TELEGRAPH_RECIPE =
            RECIPE_SERIALIZERS.register("charge_telegraph",
                    () -> new SimpleCraftingRecipeSerializer<>(ChargeTelegraphRecipe::new));

    public static final RegistryObject<BlockEntityType<com.mrfortuna.morsecraft.blocks.TelegraphKeyBlockEntity>> TELEGRAPH_KEY_ENTITY =
            BLOCK_ENTITIES.register("telegraph_key", () ->
                    BlockEntityType.Builder.of(TelegraphKeyBlockEntity::new, TELEGRAPH_KEY.get()).build(null));

    public static final RegistryObject<CreativeModeTab> MORSECRAFT_TAB = CREATIVE_MODE_TABS.register("morsecraft_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> TELEGRAPH_KEY_ITEM.get().getDefaultInstance())
            .title(Component.translatable("creativetab.morsecraft_tab"))
            .displayItems((parameters, output) -> {
                output.accept(TELEGRAPH_KEY_ITEM.get());
                output.accept(PAPER_TAPE_ITEM.get());
            }).build());

    public Morsecraft() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);


        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Common Setup 200");
    }



    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Load mod on server 200");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("Mod loaded on client 200");
        }
    }
}
