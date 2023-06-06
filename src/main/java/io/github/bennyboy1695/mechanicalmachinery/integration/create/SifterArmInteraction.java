package io.github.bennyboy1695.mechanicalmachinery.integration.create;

import com.simibubi.create.content.kinetics.mechanicalArm.AllArmInteractionPointTypes;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import io.github.bennyboy1695.mechanicalmachinery.MechanicalMachinery;
import io.github.bennyboy1695.mechanicalmachinery.block.sifter.SifterBlockEntity;
import io.github.bennyboy1695.mechanicalmachinery.item.MeshItem;
import io.github.bennyboy1695.mechanicalmachinery.register.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class SifterArmInteraction {

    public static final SifterType SIFTER_TYPE = register("sifter", SifterType::new);

    public static void register() {}

    private static <T extends ArmInteractionPointType> T register(String id, Function<ResourceLocation, T> factory) {
        T type = factory.apply(MechanicalMachinery.rl(id));
        ArmInteractionPointType.register(type);
        return type;
    }

    public static class SifterType extends ArmInteractionPointType {

        public SifterType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return state.is(ModBlocks.SIFTER.get());
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new SifterPoint(this, level, pos, state);
        }
    }

    public static class SifterPoint extends AllArmInteractionPointTypes.DepositOnlyArmInteractionPoint {

        public SifterPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
        }

        @Override
        public Mode getMode() {
            return Mode.DEPOSIT;
        }

        @Override
        public int getSlotCount() {
            return 1;
        }

        @Override
        public ItemStack insert(ItemStack stack, boolean simulate) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (!(blockEntity instanceof SifterBlockEntity sifterTileEntity)) {
                return stack;
            }
            if (!(stack.getItem() instanceof MeshItem)) {
                return stack;
            }
            if (sifterTileEntity.hasMeshStack()) {
                return stack;
            }
            ItemStack remainder = stack.copy();
            ItemStack toInsert = remainder.split(1);
            if (!simulate) {
                sifterTileEntity.meshInv().setStackInSlot(0, toInsert);
                sifterTileEntity.setChanged();
                sifterTileEntity.sendData();
            }
            return remainder;
        }
    }
}
