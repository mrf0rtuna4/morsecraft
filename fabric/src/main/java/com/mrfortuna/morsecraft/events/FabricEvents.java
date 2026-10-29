package com.mrfortuna.morsecraft.fabric.events;

import com.mrfortuna.morsecraft.FabricMorsecraft;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

public class FabricEvents {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            // ...
        });

        LOGGER.info("Fabric events registered");
    }
}
