package io.github.bennyboy1695.mechanicalmachinery.block.storage.controller;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import io.github.bennyboy1695.mechanicalmachinery.block.storage.gui.ControllerMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class StorageControllerBlockEntity extends KineticBlockEntity implements MenuProvider {

    private Set<BlockPos> linkedLinks;

    public StorageControllerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        linkedLinks = new HashSet<>();
    }

    @Override
    public void tick() {
        super.tick();
    }

    public boolean addLink(BlockPos pos) {
        if (!linkedLinks.contains(pos)) {
            return linkedLinks.add(pos);
        }
        return false;
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        ListTag links = new ListTag();
        linkedLinks.forEach(pos -> {
            links.add(LongTag.valueOf(pos.asLong()));
        });
        compound.put("links", links);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        Set<BlockPos> links = new HashSet<>();
        compound.getList("links", CompoundTag.TAG_LONG).forEach(tag -> {
            links.add(BlockPos.of(((LongTag)tag).getAsLong()));
        });
        this.linkedLinks = links;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("mechanicalmachinery.menu.storage_controller.display");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        if (isSpeedRequirementFulfilled() && !isOverStressed()) {
            return ControllerMenu.create(id, inventory, this);
        } else {
            player.displayClientMessage(Component.literal(ChatFormatting.RED +  "This controller is overstressed or not getting enough speed"), true);
            return null;
        }
    }
}
