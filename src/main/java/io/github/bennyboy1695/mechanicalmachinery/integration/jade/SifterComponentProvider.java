package io.github.bennyboy1695.mechanicalmachinery.integration.jade;

import io.github.bennyboy1695.mechanicalmachinery.MechanicalMachinery;
import io.github.bennyboy1695.mechanicalmachinery.block.sifter.SifterBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum SifterComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        Component mesh = Component.translatable("mechanicalmachinery.jade.mesh_empty");
        if (blockAccessor.getServerData().getBoolean("hasMesh")) {
            mesh = Component.translatable(ItemStack.of(blockAccessor.getServerData().getCompound("mesh")).getDescriptionId());
        }
        iTooltip.add(1, Component.translatable("mechanicalmachinery.jade.current_mesh").append(": ").append(mesh));
        Component sifting = Component.translatable("mechanicalmachinery.jade.sifting_empty");
        if (blockAccessor.getServerData().getBoolean("isSifting")) {
            sifting = Component.translatable(ItemStack.of(blockAccessor.getServerData().getCompound("sifting")).getDescriptionId());
        }
        iTooltip.add(2, Component.translatable("mechanicalmachinery.jade.currently_sifting").append(": ").append(sifting));
    }

    @Override
    public ResourceLocation getUid() {
        return MechanicalMachinery.rl("jade_provider");
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity block, boolean b) {
        SifterBlockEntity sifterTile = (SifterBlockEntity) block;
        compoundTag.putBoolean("hasMesh", sifterTile.hasMeshStack());
        compoundTag.put("mesh", sifterTile.meshInv().getItem(0).serializeNBT());
        compoundTag.putBoolean("isSifting", !sifterTile.inputInv().isEmpty());
        compoundTag.put("sifting", sifterTile.inputInv().getItem(0).serializeNBT());
    }
}
