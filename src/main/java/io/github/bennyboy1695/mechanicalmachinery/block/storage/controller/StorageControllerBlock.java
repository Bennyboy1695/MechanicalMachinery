package io.github.bennyboy1695.mechanicalmachinery.block.storage.controller;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import io.github.bennyboy1695.mechanicalmachinery.register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import java.util.concurrent.atomic.AtomicBoolean;

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
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!world.isClientSide) {
            StorageControllerBlockEntity tile = (StorageControllerBlockEntity) world.getBlockEntity(pos);
            if (tile != null && tile.createMenu(1, player.getInventory(), player) != null) {
                NetworkHooks.openScreen((ServerPlayer) player, tile, tile.getBlockPos());
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.MEDIUM;
    }

    public boolean addLinkToController(Level level, BlockPos controller, BlockPos linkCoords) {
        AtomicBoolean returnValue = new AtomicBoolean(false);
        withBlockEntityDo(level, controller, controllerTile -> {
           returnValue.set(controllerTile.addLink(linkCoords));
        });
        return returnValue.get();
    }
}
