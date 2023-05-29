package io.github.bennyboy1695.mechanicalmachinery.block.sifter;

import com.mojang.math.Vector3f;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.item.SmartInventory;
import io.github.bennyboy1695.mechanicalmachinery.data.recipe.SifterRecipe;
import io.github.bennyboy1695.mechanicalmachinery.register.ModItems;
import io.github.bennyboy1695.mechanicalmachinery.register.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class SifterBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {
    public SmartFluidTankBehaviour inputTank;
    private final SmartInventory meshInv;
    private final SmartInventory inputInv;
    private final SmartInventory outputInv;
    private boolean contentsChanged;
    protected LazyOptional<IFluidHandler> fluidCapability;
    private final LazyOptional<IItemHandlerModifiable> itemCapability;
    private final CombinedInvWrapper meshAndInput;
    private SifterRecipe lastRecipe;

    private boolean shouldTopMove;
    private Vector3f renderStep = new Vector3f(0, 0, 0);
    private int step = 0;
    private int tick = 0;

    public SifterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        meshInv = new CustomSmartInv(1, this, 1, false, ModItems.MESH.get());
        meshInv.whenContentsChanged($ -> contentsChanged = true);
        meshInv.forbidExtraction();
        inputInv = new SmartInventory(1, this, 512, false);
        inputInv.whenContentsChanged($ -> contentsChanged = true);
        inputInv.forbidExtraction();
        outputInv = new SmartInventory(64, this, 512, false);
        outputInv.whenContentsChanged($ -> contentsChanged = true);
        outputInv.forbidInsertion();
        itemCapability = LazyOptional.of(() -> new CombinedInvWrapper(meshInv, inputInv, outputInv));
        fluidCapability = inputTank.getCapability().cast();
        meshAndInput = new CombinedInvWrapper(meshInv, inputInv);
        contentsChanged = true;
    }



    @Override
    public void tick() {
        super.tick();
        if (getSpeed() == 0) {
            return;
        }

        tick++;
        doRenderTicks();
        processRecipe();
        shouldTopMove = true;
        sendData();
    }

    private void processRecipe() {
        RecipeWrapper recipeWrapper = new RecipeWrapper(meshAndInput);
        if (lastRecipe == null || !lastRecipe.matches(recipeWrapper, level)) {
            Optional<SifterRecipe> sifterRecipe = ModRecipeTypes.SIFTER.find(recipeWrapper, level, !inputTank.isEmpty());
            if (sifterRecipe.isEmpty()) {
                return;
            }
            lastRecipe = sifterRecipe.get();
        }
        ItemStack stack = inputInv.getStackInSlot(0);
        stack.shrink(1);
        lastRecipe.rollResults().forEach(out -> ItemHandlerHelper.insertItem(outputInv, out, false));
    }

    private void doRenderTicks() {
        if (tick % 5 == 0) {
            switch (step) {
                case 0 -> {
                    renderStep = new Vector3f(0, 0, 0);
                    step = 1;
                }
                case 1 -> {
                    renderStep = getBlockState().getValue(HorizontalKineticBlock.HORIZONTAL_FACING).getOpposite().step();
                    step = 2;
                }
                case 2 -> {
                    renderStep = new Vector3f(0, 0, 0);
                    step = 3;
                }
                case 3 -> {
                    renderStep = getBlockState().getValue(HorizontalKineticBlock.HORIZONTAL_FACING).step();
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
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        behaviours.add(inputTank = SmartFluidTankBehaviour.single(this, 1000)
                .allowExtraction()
                .allowInsertion());
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
        inputInv.deserializeNBT(compound.getCompound("inputInv"));
        outputInv.deserializeNBT(compound.getCompound("outputInv"));
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("meshInv", meshInv.serializeNBT());
        compound.put("inputInv", inputInv.serializeNBT());
        compound.put("outputInv", outputInv.serializeNBT());
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

    public SmartInventory inputInv() {
        return inputInv;
    }

    public SmartInventory outputInv() {
        return outputInv;
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
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        return containedFluidTooltip(tooltip, isPlayerSneaking, getCapability(ForgeCapabilities.FLUID_HANDLER));
    }

    private static class CustomSmartInv extends SmartInventory {

        private final Object allowedInstance;
        public CustomSmartInv(int slots, SyncedBlockEntity te, Object allowedInstance) {
            super(slots, te);
            this.allowedInstance = allowedInstance;
        }

        public CustomSmartInv(int slots, SyncedBlockEntity te, int stackSize, boolean stackNonStackables, Object allowedInstance) {
            super(slots, te, stackSize, stackNonStackables);
            this.allowedInstance = allowedInstance;
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (stack.getItem().getClass().isInstance(allowedInstance)) {
                super.insertItem(slot, stack, simulate);
            }
            return stack;
        }
    }
}
