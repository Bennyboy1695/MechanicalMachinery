package io.github.bennyboy1695.mechanicalmachinery.item;

import io.github.bennyboy1695.mechanicalmachinery.block.storage.controller.StorageControllerBlock;
import io.github.bennyboy1695.mechanicalmachinery.block.storage.link.StorageLinkBlock;
import io.github.bennyboy1695.mechanicalmachinery.register.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;

public class LinkingToolItem extends Item {

    public LinkingToolItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() != null && context.getPlayer() instanceof ServerPlayer player && !(player instanceof FakePlayer)) {
            if (player.isCrouching()) {
                BlockState state = context.getLevel().getBlockState(context.getClickedPos());
                if (state.getBlock() instanceof StorageControllerBlock || state.getBlock() instanceof StorageLinkBlock) {
                    if (state.getBlock() instanceof StorageControllerBlock controllerBlock) {
                        CompoundTag coords = context.getItemInHand().getTagElement("link");
                        if (coords != null) {
                            controllerBlock.addLinkToController(context.getLevel(), context.getClickedPos(), new BlockPos(coords.getInt("X"), coords.getInt("Y"), coords.getInt("Z")));
                        }
                        context.getItemInHand().getOrCreateTag().remove("link");
                    } else if (state.getBlock() instanceof StorageLinkBlock) {
                        CompoundTag coords = new CompoundTag();
                        coords.putInt("X", context.getClickedPos().getX());
                        coords.putInt("Y", context.getClickedPos().getY());
                        coords.putInt("Z", context.getClickedPos().getZ());
                        context.getItemInHand().addTagElement("link", coords);
                    }
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return pStack.getTagElement("link") != null;
    }


}
