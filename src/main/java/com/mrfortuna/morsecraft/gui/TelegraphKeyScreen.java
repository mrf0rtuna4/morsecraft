package com.mrfortuna.morsecraft.gui;

import com.mrfortuna.morsecraft.blocks.TelegraphKeyBlockEntity;
import com.mrfortuna.morsecraft.Morsecraft;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class TelegraphKeyScreen extends AbstractContainerScreen<TelegraphKeyMenu> {

    private static final ResourceLocation GUI_TEXTURE =
            new ResourceLocation("morsecraft", "textures/gui/telegraph_key.png");

    public TelegraphKeyScreen(TelegraphKeyMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        // кнопки для ввода
        this.addRenderableWidget(Button.builder(Component.literal("."), b -> this.menu.inputSymbol('.', this.minecraft.player))
                .pos(x + 10, y + 20).size(20, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("-"), b -> this.menu.inputSymbol('-', this.minecraft.player))
                .pos(x + 40, y + 20).size(20, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("SPACE"), b -> this.menu.inputSymbol(' ', this.minecraft.player))
                .pos(x + 70, y + 20).size(50, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("/"), b -> this.menu.inputSymbol('/', this.minecraft.player))
                .pos(x + 130, y + 20).size(20, 20).build());
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        TelegraphKeyBlockEntity te = this.menu.getTelegraph();
        if (te != null) {
            guiGraphics.drawString(this.font,
                    Component.literal("Paper: " + te.getPaperCount()),
                    this.leftPos + 10, this.topPos + 60, 0xFFFFFF);

            guiGraphics.drawString(this.font,
                    Component.literal("Sequence: " + te.getSequence()),
                    this.leftPos + 10, this.topPos + 80, 0xFFFFFF);
        }

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
