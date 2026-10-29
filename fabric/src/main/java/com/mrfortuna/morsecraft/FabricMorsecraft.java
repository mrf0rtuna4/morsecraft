package com.mrfortuna.morsecraft;

import com.mrfortuna.morsecraft.api.PlatformApi;
import com.mrfortuna.morsecraft.fabric.platform.FabricPlatform;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

public class FabricMorsecraft implements ModInitializer {

    public static final String MOD_ID = "morsecraft";
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        FabricPlatform.init();
        PlatformApi.setImpl(new FabricPlatform());
        PlatformApi.init();
        PlatformApi.registerEvents();

        LOGGER.info("FabricMorsecraft initialized");
    }
}
