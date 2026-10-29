package com.mrfortuna.morsecraft;

import com.mrfortuna.morsecraft.api.PlatformApi;
import com.mrfortuna.morsecraft.forge.platform.ForgePlatform;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ForgeMorsecraft.MOD_ID)
public class ForgeMorsecraft {

    public static final String MOD_ID = "morsecraft";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ForgeMorsecraft() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ForgePlatform.BLOCKS.register(modEventBus);
        ForgePlatform.ITEMS.register(modEventBus);
        ForgePlatform.BLOCK_ENTITIES.register(modEventBus);
        ForgePlatform.CREATIVE_MODE_TABS.register(modEventBus);
        ForgePlatform.RECIPE_SERIALIZERS.register(modEventBus);
        ForgePlatform.SOUND_EVENTS.register(modEventBus);
        ForgePlatform.CONTAINERS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("ForgeMorsecraft initialized");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        PlatformApi.init();
        PlatformApi.registerEvents();
        LOGGER.info("Common setup complete");
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                com.mrfortuna.morsecraft.forge.client.ForgeClient.init();
            });
            LOGGER.info("Client setup complete");
        }
    }
}
