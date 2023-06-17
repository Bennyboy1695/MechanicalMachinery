package io.github.bennyboy1695.mechanicalmachinery.block.storage.link;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import io.github.bennyboy1695.mechanicalmachinery.block.storage.capability.ILinkCapability;
import io.github.bennyboy1695.mechanicalmachinery.block.storage.capability.implementation.LinkCapability;
import io.github.bennyboy1695.mechanicalmachinery.register.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StorageLinkBlockEntity extends KineticBlockEntity {

    private final ILinkCapability linkCapability;

    public StorageLinkBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        linkCapability = new LinkCapability(this);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ModCapabilities.LINK_CAPABILITY) {
            LazyOptional<ILinkCapability> linkOptional = LazyOptional.of(() -> linkCapability);
            return linkOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    public ILinkCapability getLinkCapability() {
        return linkCapability;
    }
}
