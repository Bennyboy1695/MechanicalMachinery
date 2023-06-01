package io.github.bennyboy1695.mechanicalmachinery.block.storage.link;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import io.github.bennyboy1695.mechanicalmachinery.register.ModBlockEntities;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class StorageLinkBlock extends HorizontalKineticBlock implements IBE<StorageLinkBlockEntity> {

    public StorageLinkBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public Class<StorageLinkBlockEntity> getBlockEntityClass() {
        return StorageLinkBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends StorageLinkBlockEntity> getBlockEntityType() {
        return ModBlockEntities.STORAGE_LINK.get();
    }
}
