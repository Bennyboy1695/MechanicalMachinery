package io.github.bennyboy1695.mechanicalmachinery.block.storage.capability;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;

public interface ILinkCapability {

    Map<Direction, List<ItemStack>> getStoredStacks();
}
