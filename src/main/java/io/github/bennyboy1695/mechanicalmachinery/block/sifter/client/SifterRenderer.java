package io.github.bennyboy1695.mechanicalmachinery.block.sifter.client;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import io.github.bennyboy1695.mechanicalmachinery.block.sifter.SifterBlockEntity;
import io.github.bennyboy1695.mechanicalmachinery.register.ModPartials;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class SifterRenderer extends KineticBlockEntityRenderer<SifterBlockEntity> {

    private Vector3f renderStep = new Vector3f(0, 0, 0);
    private int step = 0;

    public SifterRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    protected void renderSafe(SifterBlockEntity sifterBlockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(sifterBlockEntity, partialTicks, ms, buffer, light, overlay);
        if (sifterBlockEntity.hasMeshStack()) {
            ms.pushPose();
            renderItem(sifterBlockEntity, partialTicks, ms, buffer, light, overlay);
            ms.popPose();
        }
        if (!sifterBlockEntity.getInputTank().isEmpty()) {
            ms.pushPose();
            renderFluid(sifterBlockEntity, partialTicks, ms, buffer, light, overlay);
            ms.popPose();
        }
        if (Backend.canUseInstancing(sifterBlockEntity.getLevel()))
            return;
        BlockState blockState = sifterBlockEntity.getBlockState();

        SuperByteBuffer topRender = CachedBufferer.partialFacing(ModPartials.SIFTER_TOP, blockState,
                blockState.getValue(HorizontalKineticBlock.HORIZONTAL_FACING));
        topRender.translate(0, +1, 0)
                .light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.cutout()));
    }

    private void renderItem(SifterBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemTransforms.TransformType transformType = ItemTransforms.TransformType.HEAD;
        float scale = 0.98f;
        ms.translate(getActualValue(te.renderStep().x()) +.490, 0.115, getActualValue(te.renderStep().z()) +.510);
        ms.scale(scale, 1f, scale);
        itemRenderer.renderStatic(te.meshInv().getItem(0), transformType, light, overlay, ms, buffer, 0);
    }

    private void renderFluid(SifterBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        FluidStack fluidStack = te.getInputTank().getPrimaryHandler().getFluidInTank(1);
        TransformStack msr = TransformStack.cast(ms);
        msr.scale(0.8f, 0.05f, 0.8f);
        switch (te.getBlockState().getValue(HorizontalKineticBlock.HORIZONTAL_FACING)) {
            case WEST -> msr.translate(getActualValue(te.renderStep().x()) +0.65f, 10f, getActualValue(te.renderStep().z()) +0.15f);
            case EAST -> msr.translate(getActualValue(te.renderStep().x()) -0.4f, 10f, getActualValue(te.renderStep().z()) +0.15f);
            case NORTH -> msr.translate(getActualValue(te.renderStep().x()) +0.15f, 10f, getActualValue(te.renderStep().z()) +0.65f);
            case SOUTH -> msr.translate(getActualValue(te.renderStep().x()) +0.15f, 10f, getActualValue(te.renderStep().z()) -0.4f);
        }
        FluidRenderer.renderFluidStream(fluidStack, te.getBlockState().getValue(HorizontalKineticBlock.HORIZONTAL_FACING), 0.5f, 3.8f, false, buffer, ms, light);
    }

    private float getActualValue(float value) {
        return (value == 0f ? 0f : (value == 1f ? value - 0.980f : (value == -1 ? value + 0.980f : value)));
    }

    private void doRenderTicks(BlockState state) {
        if (1 % 5 == 0) {
            switch (step) {
                case 0 -> {
                    renderStep = new Vector3f(0, 0, 0);
                    step = 1;
                }
                case 1 -> {
                    renderStep = state.getValue(HorizontalKineticBlock.HORIZONTAL_FACING).getOpposite().step();
                    step = 2;
                }
                case 2 -> {
                    renderStep = new Vector3f(0, 0, 0);
                    step = 3;
                }
                case 3 -> {
                    renderStep = state.getValue(HorizontalKineticBlock.HORIZONTAL_FACING).step();
                    step = 0;
                }
            }
        }
    }
}
