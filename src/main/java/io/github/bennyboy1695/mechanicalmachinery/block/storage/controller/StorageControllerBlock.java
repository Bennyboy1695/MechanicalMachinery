package io.github.bennyboy1695.mechanicalmachinery.block.storage.controller;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import io.github.bennyboy1695.mechanicalmachinery.register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class StorageControllerBlock extends HorizontalKineticBlock implements IBE<StorageControllerBlockEntity> {

    public StorageControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public Class<StorageControllerBlockEntity> getBlockEntityClass() {
        return StorageControllerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends StorageControllerBlockEntity> getBlockEntityType() {
        return ModBlockEntities.STORAGE_CONTROLLER.get();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.DOWN || face == Direction.UP;
    }

    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.MEDIUM;
    }

    public boolean addLinkToController(Level level, BlockPos controller, BlockPos linkCoords) {
        withBlockEntityDo(level, controller, controllerTile -> {

        });
        return false;
    }
}
