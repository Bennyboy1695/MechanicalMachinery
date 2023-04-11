package io.github.bennyboy1695.mechanicalsifting.block.sifter;

import com.mojang.math.Vector3f;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.tileEntity.behaviour.fluid.SmartFluidTankBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class SifterTileEntity extends KineticTileEntity implements IHaveGoggleInformation {
    public SmartFluidTankBehaviour inputTank;
    private SmartInventory meshInv;
    private boolean contentsChanged;
    protected LazyOptional<IFluidHandler> fluidCapability;
    private LazyOptional<IItemHandlerModifiable> itemCapability;

    private boolean shouldTopMove;
    private boolean hasMeshStack;
    private Vector3f renderStep = new Vector3f(0, 0, 0);
    private int step = 0;
    private int tick = 0;

    public SifterTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        meshInv = new SmartInventory(1, this, 1, false);
        meshInv.whenContentsChanged($ -> contentsChanged = true);
        itemCapability = LazyOptional.of(() -> new CombinedInvWrapper(meshInv));
        fluidCapability = inputTank.getCapability().cast();
        contentsChanged = true;
    }

    @Override
    public void tick() {
        sendData();
        setChanged();
        tick++;
        shouldTopMove = true;
        hasMeshStack = true;
        if (tick % 5 == 0) {
            switch (step) {
                case 0 -> {
                    renderStep = new Vector3f(0, 0, 0);
                    step = 1;
                }
                case 1 -> {
                    renderStep = getBlockState().getValue(HORIZONTAL_FACING).getOpposite().step();
                    step = 2;
                }
                case 2 -> {
                    renderStep = new Vector3f(0, 0, 0);
                    step = 3;
                }
                case 3 -> {
                    renderStep = getBlockState().getValue(HORIZONTAL_FACING).step();
                    step = 0;
                }
            }
        }
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(worldPosition).expandTowards(0, 0, 0)
                .expandTowards(0, +.5, 0);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {
        behaviours.add(inputTank = SmartFluidTankBehaviour.single(this, 1000)
                .allowExtraction()
                .allowInsertion());
    }

    public float getTotalFluidUnits(float partialTicks) {
        int renderedFluids = 0;
        float totalUnits = 0;

        for (SmartFluidTankBehaviour.TankSegment tankSegment : inputTank.getTanks()) {
            if (tankSegment.getRenderedFluid()
                    .isEmpty())
                continue;
            float units = tankSegment.getTotalUnits(partialTicks);
            if (units < 1)
                continue;
            totalUnits += units;
            renderedFluids++;
        }

        if (renderedFluids == 0)
            return 0;
        if (totalUnits < 1)
            return 0;
        return totalUnits;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (isItemHandlerCap(cap)) {
            return itemCapability.cast();
        }
        if (isFluidHandlerCap(cap)) {
            return inputTank.getCapability().cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        meshInv.deserializeNBT(compound.getCompound("meshInv"));
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("meshInv", meshInv.serializeNBT());
    }

    @Override
    public void notifyUpdate() {
        super.notifyUpdate();
    }
    @Override
    public void invalidate() {
        super.invalidate();
        fluidCapability.invalidate();
        itemCapability.invalidate();
    }


    public SmartFluidTankBehaviour getInputTank() {
        return inputTank;
    }

    public SmartInventory meshInv() {
        return meshInv;
    }

    public boolean shouldTopMove() {
        return shouldTopMove;
    }

    public boolean hasMeshStack() {
        return !meshInv.isEmpty();
    }

    public Vector3f renderStep() {
        return renderStep;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedFluidTooltip(tooltip, isPlayerSneaking, getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY));
    }
}
