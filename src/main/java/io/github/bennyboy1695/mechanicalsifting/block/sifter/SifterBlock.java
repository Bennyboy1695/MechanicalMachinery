package io.github.bennyboy1695.mechanicalsifting.block.sifter;

import com.simibubi.create.content.contraptions.base.KineticBlock;
import com.simibubi.create.content.contraptions.components.deployer.DeployerRenderer;
import com.simibubi.create.content.contraptions.relays.elementary.ICogWheel;
import com.simibubi.create.foundation.block.ITE;
import io.github.bennyboy1695.mechanicalsifting.item.MeshItem;
import io.github.bennyboy1695.mechanicalsifting.register.ModTiles;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidUtil;
import org.jetbrains.annotations.Nullable;

public class SifterBlock extends KineticBlock implements ITE<SifterTileEntity>, ICogWheel {
    public SifterBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos offsetPos = context.getClickedPos().relative(context.getHorizontalDirection().getOpposite());
        return context.getLevel().getBlockState(offsetPos).canBeReplaced(context)
                ? this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, (context.getHorizontalDirection().getOpposite()))
                : null;
    }

    @Override
    public VoxelShape getShape(BlockState p_48816_, BlockGetter p_48817_, BlockPos p_48818_, CollisionContext p_48819_) {
        return Block.box(0.0, 0.0, 0.0, 16.0, 12.5, 16.0);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack itemStack = player.getItemInHand(hand);
        BlockEntity tileEntity = level.getBlockEntity(pos);

        if (!(tileEntity instanceof SifterTileEntity)) {
            return InteractionResult.SUCCESS;
        }

        SifterTileEntity sifter = (SifterTileEntity) tileEntity;
        if (itemStack.getItem() instanceof BucketItem) {
            if (!level.isClientSide()) {
                FluidUtil.interactWithFluidHandler(player, hand, sifter.inputTank.getPrimaryHandler());
            }
        }
        if (itemStack.getItem() instanceof MeshItem) {
            if (!level.isClientSide()) {
                if (!sifter.hasMeshStack()) {
                    sifter.meshInv().insertItem(0, itemStack, false);
                    player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                } else {
                    ItemStack oldMesh = sifter.meshInv().extractItem(0, 1, false);
                    sifter.meshInv().insertItem(0, itemStack, false);
                    player.setItemInHand(hand, oldMesh);
                }
                sifter.setChanged();
                sifter.sendData();
            }
        }
        if (itemStack.isEmpty() && !sifter.meshInv().isEmpty()) {
            if (!level.isClientSide()) {
                player.setItemInHand(hand, sifter.meshInv().extractItem(0, 1, false));
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
    public Class<SifterTileEntity> getTileEntityClass() {
        return SifterTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends SifterTileEntity> getTileEntityType() {
        return ModTiles.SIFTER.get();
    }
}
