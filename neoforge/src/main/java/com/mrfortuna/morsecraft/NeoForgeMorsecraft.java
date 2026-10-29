package com.mrfortuna.morsecraft;

import com.mrfortuna.morsecraft.api.PlatformApi;
import com.mrfortuna.morsecraft.neoforge.platform.NeoForgePlatform;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(NeoForgeMorsecraft.MOD_ID)
public class NeoForgeMorsecraft {

    public static final String MOD_ID = "morsecraft";
    private static final Logger LOGGER = LogUtils.getLogger();

    public NeoForgeMorsecraft() {
        IEventBus modEventBus = net.neoforged.neoforge.common.NeoForgeModLoadingContext.get().getModEventBus();

        NeoForgePlatform.BLOCKS.register(modEventBus);
        NeoForgePlatform.ITEMS.register(modEventBus);
        NeoForgePlatform.BLOCK_ENTITIES.register(modEventBus);
        NeoForgePlatform.CREATIVE_MODE_TABS.register(modEventBus);
        NeoForgePlatform.RECIPE_SERIALIZERS.register(modEventBus);
        NeoForgePlatform.SOUND_EVENTS.register(modEventBus);
        NeoForgePlatform.CONTAINERS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(this);

        LOGGER.info("NeoForgeMorsecraft initialized");
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
                com.mrfortuna.morsecraft.neoforge.client.NeoForgeClient.init();
            });
            LOGGER.info("Client setup complete");
        }
    }
}
