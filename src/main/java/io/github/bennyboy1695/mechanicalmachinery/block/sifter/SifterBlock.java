package io.github.bennyboy1695.mechanicalmachinery.block.sifter;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
import io.github.bennyboy1695.mechanicalmachinery.item.MeshItem;
import io.github.bennyboy1695.mechanicalmachinery.register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidUtil;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class SifterBlock extends HorizontalKineticBlock implements IBE<SifterBlockEntity> {
    public SifterBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(this.getStateDefinition().any().setValue(HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos offsetPos = context.getClickedPos().relative(context.getHorizontalDirection().getOpposite());
        if (context.getPlayer() != null && context.getPlayer().isCrouching()) {
           return context.getLevel().getBlockState(offsetPos).canBeReplaced(context)
                    ? this.defaultBlockState().setValue(HORIZONTAL_FACING, (context.getHorizontalDirection()))
                    : null;
        }
        return context.getLevel().getBlockState(offsetPos).canBeReplaced(context)
                ? this.defaultBlockState().setValue(HORIZONTAL_FACING, (context.getHorizontalDirection().getOpposite()))
                : null;
    }

    @Nonnull
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        return Block.box(0.0, 0.0, 0.0, 16.0, 12.5, 16.0);
    }

    @Nonnull
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack itemStack = player.getItemInHand(hand);
        BlockEntity tileEntity = level.getBlockEntity(pos);

        if (!(tileEntity instanceof SifterBlockEntity sifter)) {
            return InteractionResult.SUCCESS;
        }

        if (itemStack.getItem() instanceof BucketItem) {
            if (!level.isClientSide()) {
                FluidUtil.interactWithFluidHandler(player, hand, sifter.inputTank.getPrimaryHandler());
            }
        }
        if (itemStack.getItem() instanceof MeshItem) {
            if (!level.isClientSide()) {
                if (!sifter.hasMeshStack()) {
                    sifter.meshInv().setStackInSlot(0, itemStack);
                    player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                } else {
                    ItemStack oldMesh = sifter.meshInv().getItem(0);
                    sifter.meshInv().setStackInSlot(0, itemStack);
                    player.setItemInHand(hand, oldMesh);
                }
                sifter.setChanged();
                sifter.sendData();
            }
        }
        if (itemStack.isEmpty() && !sifter.meshInv().isEmpty()) {
            if (!level.isClientSide()) {
                ItemStack mesh = sifter.meshInv().getItem(0);
                sifter.meshInv().setStackInSlot(0, ItemStack.EMPTY);
                player.setItemInHand(hand, mesh);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public Class<SifterBlockEntity> getBlockEntityClass() {
        return SifterBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SifterBlockEntity> getBlockEntityType() {
        return ModBlockEntities.SIFTER.get();
    }

    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.SLOW;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.DOWN;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasBlockEntity() && state.getBlock() != newState.getBlock()) {
            withBlockEntityDo(worldIn, pos, sifter -> {
                ItemHelper.dropContents(worldIn, pos, sifter.meshInv());
                ItemHelper.dropContents(worldIn, pos, sifter.inputInv());
                ItemHelper.dropContents(worldIn, pos, sifter.outputInv());
            });
            worldIn.removeBlockEntity(pos);
        }
    }
}
