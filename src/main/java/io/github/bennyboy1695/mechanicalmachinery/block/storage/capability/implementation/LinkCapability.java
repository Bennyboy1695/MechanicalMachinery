package io.github.bennyboy1695.mechanicalmachinery.block.storage.capability.implementation;

import com.google.common.collect.Maps;
import io.github.bennyboy1695.mechanicalmachinery.block.storage.capability.ILinkCapability;
import io.github.bennyboy1695.mechanicalmachinery.block.storage.link.StorageLinkBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinkCapability implements ILinkCapability {

    private StorageLinkBlockEntity linkBlock;

    public LinkCapability(StorageLinkBlockEntity linkBlock) {
        this.linkBlock = linkBlock;
    }

    @Override
    public Map<Direction, List<ItemStack>> getStoredStacks() {
        if (linkBlock == null) {
            return new HashMap<>();
        }

        Map<Direction, List<ItemStack>> directionStacks = Maps.newHashMap();
        for (Direction direction : Direction.values()) {
            if (direction.equals(Direction.UP) || direction.equals(Direction.DOWN)) {
                continue;
            }
            BlockPos storagePos = linkBlock.getBlockPos().relative(direction);
            BlockState storageState = linkBlock.getLevel().getBlockState(storagePos);
            if (storageState.hasBlockEntity()) {
                BlockEntity storageTile = linkBlock.getLevel().getBlockEntity(storagePos);
                IItemHandler iItemHandler = storageTile.getCapability(ForgeCapabilities.ITEM_HANDLER, direction.getOpposite()).orElse(null);
                List<ItemStack> stacks = new ArrayList<>();
                if (iItemHandler != null) {
                    for (int i = 0; i < iItemHandler.getSlots(); i++) {
                        stacks.add(iItemHandler.getStackInSlot(i).copy());
                    }
                    directionStacks.put(direction, stacks);
                } else {
                    continue;
                }
            }
        }
        return directionStacks;
    }
}
