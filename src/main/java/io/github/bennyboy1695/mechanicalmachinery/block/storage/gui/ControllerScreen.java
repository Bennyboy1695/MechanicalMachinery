package io.github.bennyboy1695.mechanicalmachinery.block.storage.gui;

import com.google.common.collect.ImmutableList;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import io.github.bennyboy1695.mechanicalmachinery.register.ModBlocks;
import io.github.bennyboy1695.mechanicalmachinery.register.ModGUITextures;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.Collections;
import java.util.List;

public class ControllerScreen extends AbstractSimiContainerScreen<ControllerMenu> {

    protected static final ModGUITextures BG = ModGUITextures.CONTROLLER;
    protected static final AllGuiTextures PLAYER = AllGuiTextures.PLAYER_INVENTORY;

    private IconButton confirmButton;
    private List<Rect2i> extraAreas = Collections.emptyList();

    public ControllerScreen(ControllerMenu container, Inventory inv, Component title) {
        super(container, inv, title);
        if (Minecraft.getInstance().options.guiScale().get() == 4) {
            Minecraft.getInstance().player.displayClientMessage(Component.literal(ChatFormatting.RED + "This gui will not render well at this gui scale consider lowering it"), true);
        }
        init();
    }

    @Override
    protected void init() {
        setWindowSize(BG.width, BG.height + PLAYER.height - 20);
        setWindowOffset(-11, 0);
        super.init();

        confirmButton = new IconButton(leftPos + 30 + BG.width - 85, topPos + BG.height - 54, AllIcons.I_CONFIRM);
        confirmButton.withCallback(() -> {
            minecraft.player.closeContainer();
        });
        addRenderableWidget(confirmButton);

        extraAreas = ImmutableList.of(
                new Rect2i(leftPos - 20 + BG.width, topPos + BG.height - 90, 72, 75)
        );
    }


    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        int x = leftPos + imageWidth - BG.width - 24;
        int y = topPos;

        BG.render(pPoseStack, x, y, this);
        font.draw(pPoseStack, title, x + 5, y + 5, 0x592424);

        int invX = leftPos - 30;
        int invY = topPos + imageHeight - PLAYER.height + 5;
        renderPlayerInventory(pPoseStack, invX, invY);

        renderController(pPoseStack, x + BG.width + 50, y + BG.height - 15, pPartialTick);
    }

    private void renderController(PoseStack ms, int x, int y, float partialTicks) {
        TransformStack.cast(ms)
                .pushPose()
                .translate(x, y, 100)
                .scale(50)
                .rotateX(-25)
                .rotateY(-202);

        GuiGameElement.of(ModBlocks.STORAGE_CONTROLLER.getDefaultState())
                .render(ms);
        ms.popPose();
    }

    @Override
    protected void renderForeground(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.renderForeground(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public List<Rect2i> getExtraAreas() {
        return extraAreas;
    }
}
