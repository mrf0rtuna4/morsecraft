package com.mrfortuna.morsecraft.forge.client;

import com.mrfortuna.morsecraft.api.PlatformApi;
import com.mrfortuna.morsecraft.ForgeMorsecraft;
import com.mrfortuna.morsecraft.gui.TelegraphKeyScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ForgeMorsecraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeClient {

    public static void init() {
        MenuScreens.register(
                PlatformApi.TELEGRAPH_KEY_MENU,
                TelegraphKeyScreen::new
        );
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(ForgeClient::init);
    }
}
