package io.github.bennyboy1695.mechanicalmachinery.block.sifter;

import com.mojang.math.Vector3f;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.simple.DeferralBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import io.github.bennyboy1695.mechanicalmachinery.MechanicalMachinery;
import io.github.bennyboy1695.mechanicalmachinery.data.recipe.SifterRecipe;
import io.github.bennyboy1695.mechanicalmachinery.register.ModItems;
import io.github.bennyboy1695.mechanicalmachinery.register.ModRecipeTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SifterBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {
    public SmartFluidTankBehaviour inputTank;
    private final SmartInventory meshInv;
    private final SmartInventory inputInv;
    private final SmartInventory outputInv;
    protected LazyOptional<IFluidHandler> fluidCapability;
    private final LazyOptional<IItemHandlerModifiable> itemCapability;
    private final CombinedInvWrapper meshAndInput;
    private SifterRecipe lastRecipe;

    private boolean shouldTopMove;
    private Vector3f renderStep = new Vector3f(0, 0, 0);
    private int step = 0;
    private int tick = 0;
    private int timer;
    private boolean fluidChanged;

    public SifterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        meshInv = new SmartInventory(1, this, 1, false);
        meshInv.whenContentsChanged($ -> sendData()).forbidInsertion().forbidExtraction();
        inputInv = new SmartInventory(1, this, 64, false).forbidExtraction().whenContentsChanged($ -> sendData());
        outputInv = new SmartInventory(64, this, 512, false).forbidInsertion().whenContentsChanged($ -> sendData());
        itemCapability = LazyOptional.of(() -> new CombinedInvWrapper(meshInv, inputInv, outputInv));
        fluidCapability = inputTank.getCapability().cast();
        meshAndInput = new CombinedInvWrapper(meshInv, inputInv);
    }


    @Override
    public void tick() {
        super.tick();
        tick++;
        if (getSpeed() == 0) {
            return;
        }
        if (meshInv().isEmpty()) {
            return;
        }
        if (fluidChanged) {
            fluidChanged = false;
            shouldTopMove = false;
            lastRecipe = null;
            return;
        }
        for (int i = 0; i < outputInv().getSlots(); i++) {
            if (outputInv().getStackInSlot(i).getCount() == outputInv().getSlotLimit(i)) {
                return;
            }
        }

        if (lastRecipe != null && lastRecipe.consumesFluid() != 0 && inputTank.getPrimaryHandler().getFluidAmount() < lastRecipe.consumesFluid()) {
            shouldTopMove = false;
            lastRecipe = null;
            return;
        }

        if (timer > 0) {
            timer -= getProcessingSpeed();

            if (level.isClientSide) {
                doRenderTicks();
                shouldTopMove = true;
                return;
            }
            if (timer <= 0)
                processRecipe();
            return;
        }
        if (inputInv.getStackInSlot(0)
                .isEmpty())
            return;

        RecipeWrapper inventoryIn = new RecipeWrapper(meshAndInput);
        if (lastRecipe == null || !lastRecipe.matches(inventoryIn, level)) {
            Optional<SifterRecipe> recipe = ModRecipeTypes.SIFTER.find(inventoryIn, level, inputTank.getPrimaryHandler().getFluid());
            if (!recipe.isPresent()) {
                timer = 100;
                shouldTopMove = false;
                sendData();
            } else {
                lastRecipe = recipe.get();
                timer = lastRecipe.getProcessingDuration();
                sendData();
            }
            return;
        }
        timer = lastRecipe.getProcessingDuration();
        sendData();
    }

    private void processRecipe() {
        RecipeWrapper recipeWrapper = new RecipeWrapper(meshAndInput);
        if (lastRecipe == null || !lastRecipe.matches(recipeWrapper, level)) {
            Optional<SifterRecipe> sifterRecipe = ModRecipeTypes.SIFTER.find(recipeWrapper, level, inputTank.getPrimaryHandler().getFluid());
            if (sifterRecipe.isEmpty()) {
                return;
            }
            lastRecipe = sifterRecipe.get();
        }
        if (lastRecipe.consumesFluid() != 0 && !lastRecipe.requiredFluid().equals(FluidIngredient.EMPTY) && inputTank.getPrimaryHandler().getFluidAmount() > lastRecipe.consumesFluid()) {
            inputTank.getPrimaryHandler().drain(lastRecipe.consumesFluid(), IFluidHandler.FluidAction.EXECUTE);
        } else {
            return;
        }
        ItemStack stack = inputInv.getStackInSlot(0);
        stack.shrink(1);
        ItemStack mesh = meshInv.getStackInSlot(0);
        mesh.hurt(1, level.random, null);
        if (mesh.getDamageValue() >= mesh.getMaxDamage()) {
            meshInv.removeItem(0, 1);
        } else {
            meshInv.setItem(0, mesh);
        }
        if (lastRecipe.consumesFluid() != 0 && !lastRecipe.requiredFluid().equals(FluidIngredient.EMPTY)) {
            inputTank.getPrimaryHandler().drain(lastRecipe.consumesFluid(), IFluidHandler.FluidAction.EXECUTE);
        }
        lastRecipe.rollResults().forEach(out -> {
            outputInv.allowInsertion();
            ItemHandlerHelper.insertItem(outputInv(), out, false);
            outputInv.forbidInsertion();
        });
        shouldTopMove = false;
        sendData();
        setChanged();
    }

    @Override
    public float calculateStressApplied() {
        return (lastRecipe != null ? super.calculateStressApplied() + lastRecipe.addedStress() : super.calculateStressApplied());
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

    public int getProcessingSpeed() {
        return Mth.clamp((int) Math.abs(getSpeed() / 16f), 1, 512);
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
                .allowInsertion()
                .whenFluidUpdates(() -> fluidChanged = true));
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
        Lang.translate(MechanicalMachinery.MOD_ID + ".sifter.current_fluid")
                .style(ChatFormatting.AQUA)
                .forGoggles(tooltip);
        if (inputTank.getPrimaryHandler().getFluid().equals(FluidStack.EMPTY)) {
            Lang.text("Empty").style(ChatFormatting.WHITE).forGoggles(tooltip, 1);
        } else {
            Lang.fluidName(inputTank.getPrimaryHandler().getFluid()).style(ChatFormatting.WHITE).forGoggles(tooltip, 1);
        }
        return true;
    }
}
