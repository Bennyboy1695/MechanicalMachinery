package io.github.bennyboy1695.mechanicalsifting.integration.jade;

import io.github.bennyboy1695.mechanicalsifting.MechanicalSifting;
import io.github.bennyboy1695.mechanicalsifting.block.sifter.SifterBlock;
import io.github.bennyboy1695.mechanicalsifting.block.sifter.SifterTileEntity;
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
        Component mesh = Component.translatable("mechanicalsifting.jade.mesh_empty");
        if (blockAccessor.getServerData().getBoolean("hasMesh")) {
            mesh = Component.translatable(ItemStack.of(blockAccessor.getServerData().getCompound("mesh")).getDescriptionId());
        }
        iTooltip.add(1, Component.translatable("mechanicalsifting.jade.current_mesh").append(": ").append(mesh));
        Component sifting = Component.translatable("mechanicalsifting.jade.sifting_empty");
        if (blockAccessor.getServerData().getBoolean("isSifting")) {
            sifting = Component.translatable(ItemStack.of(blockAccessor.getServerData().getCompound("sifting")).getDescriptionId());
        }
        iTooltip.add(2, Component.translatable("mechanicalsifting.jade.currently_sifting").append(": ").append(sifting));
    }

    @Override
    public ResourceLocation getUid() {
        return MechanicalSifting.rl("jade_provider");
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity block, boolean b) {
        SifterTileEntity sifterTile = (SifterTileEntity) block;
        compoundTag.putBoolean("hasMesh", sifterTile.hasMeshStack());
        compoundTag.put("mesh", sifterTile.meshInv().getItem(0).serializeNBT());
        compoundTag.putBoolean("isSifting", !sifterTile.inputInv().isEmpty());
        compoundTag.put("sifting", sifterTile.inputInv().getItem(0).serializeNBT());
    }
}
