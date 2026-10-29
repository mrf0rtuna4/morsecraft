package com.mrfortuna.morsecraft.fabric.client;

import com.mrfortuna.morsecraft.api.PlatformApi;
import com.mrfortuna.morsecraft.gui.TelegraphKeyScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.client.gui.screens.MenuScreens;

@Environment(EnvType.CLIENT)
public class FabricClient {

    public static void init() {
        MenuScreens.register(
                PlatformApi.TELEGRAPH_KEY_MENU,
                TelegraphKeyScreen::new
        );
    }
}
