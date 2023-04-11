package io.github.bennyboy1695.mechanicalsifting.block.sifter.client;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import io.github.bennyboy1695.mechanicalsifting.block.sifter.SifterTileEntity;
import io.github.bennyboy1695.mechanicalsifting.register.ModItems;
import io.github.bennyboy1695.mechanicalsifting.register.ModPartials;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class SifterRenderer extends KineticTileEntityRenderer {
    public SifterRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(KineticTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(te, partialTicks, ms, buffer, light, overlay);
        SifterTileEntity sifterTileEntity = (SifterTileEntity) te;
        if (sifterTileEntity.hasMeshStack()) {
            ms.pushPose();
            renderItem(sifterTileEntity, partialTicks, ms, buffer, light, overlay);
            ms.popPose();
        }
        if (!sifterTileEntity.getInputTank().isEmpty()) {
            ms.pushPose();
            renderFluid(sifterTileEntity, partialTicks, ms, buffer, light, overlay);
            ms.popPose();
        }
        if (Backend.canUseInstancing(te.getLevel()))
            return;

        BlockState blockState = te.getBlockState();

        SuperByteBuffer topRender = CachedBufferer.partialFacing(ModPartials.SIFTER_TOP, blockState,
                blockState.getValue(HORIZONTAL_FACING));
        topRender.translate(0, +1, 0)
                .light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.cutout()));
    }

    private void renderItem(SifterTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemTransforms.TransformType transformType = ItemTransforms.TransformType.HEAD;
        float scale = 0.98f;
        ms.translate(getActualValue(te.renderStep().x()) +.490, 0.115, getActualValue(te.renderStep().z()) +.510);
        ms.scale(scale, 1f, scale);
        itemRenderer.renderStatic(te.meshInv().getItem(0), transformType, light, overlay, ms, buffer, 0);
    }

    private void renderFluid(SifterTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        FluidStack fluidStack = te.getInputTank().getPrimaryHandler().getFluidInTank(1);
        TransformStack msr = TransformStack.cast(ms);
        msr.scale(0.8f, 0.05f, 0.8f);
        switch (te.getBlockState().getValue(HORIZONTAL_FACING)) {
            case WEST -> msr.translate(getActualValue(te.renderStep().x()) +0.65f, 10f, getActualValue(te.renderStep().z()) +0.15f);
            case EAST -> msr.translate(getActualValue(te.renderStep().x()) -0.4f, 10f, getActualValue(te.renderStep().z()) +0.15f);
            case NORTH -> msr.translate(getActualValue(te.renderStep().x()) +0.15f, 10f, getActualValue(te.renderStep().z()) +0.65f);
            case SOUTH -> msr.translate(getActualValue(te.renderStep().x()) +0.15f, 10f, getActualValue(te.renderStep().z()) -0.4f);
        }
        FluidRenderer.renderFluidStream(fluidStack, te.getBlockState().getValue(HORIZONTAL_FACING), 0.5f, 3.8f, false, buffer, ms, light);
    }

    private float getActualValue(float value) {
        return (value == 0f ? 0f : (value == 1f ? value - 0.975f : (value == -1 ? value + 0.975f : value)));
    }
}
