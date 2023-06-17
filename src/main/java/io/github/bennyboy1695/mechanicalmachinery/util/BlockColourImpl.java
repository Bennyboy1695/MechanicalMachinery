package io.github.bennyboy1695.mechanicalmachinery.util;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BlockColourImpl implements BlockColor {

    private final int color;
    private final int layer;

    public BlockColourImpl(int color, int tintIndex) {
        this.color = color;
        this.layer = tintIndex;
    }

    public static Supplier<BlockColor> supplier(int color, int tintIndex) {
        return () -> new BlockColourImpl(color, tintIndex);
    }

    public int getColor() {
        return color;
    }

    public int getLayer() {
        return layer;
    }

    @Override
    public int getColor(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex) {
        return this.layer == tintIndex ? color : 0xFFFFFFFF;
    }
}
