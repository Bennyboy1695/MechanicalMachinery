package io.github.bennyboy1695.mechanicalmachinery.block.storage.gui;

import com.simibubi.create.content.equipment.toolbox.ToolboxSlot;
import com.simibubi.create.foundation.gui.menu.MenuBase;
import com.tom.storagemod.gui.CraftingTerminalMenu;
import io.github.bennyboy1695.mechanicalmachinery.block.storage.controller.StorageControllerBlockEntity;
import io.github.bennyboy1695.mechanicalmachinery.register.ModGuis;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import static com.simibubi.create.content.equipment.toolbox.ToolboxInventory.STACKS_PER_COMPARTMENT;

public class ControllerMenu extends MenuBase<StorageControllerBlockEntity> {

    private CraftingContainer craftingGrid;
    private ResultContainer craftingResult;

    public ControllerMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public ControllerMenu(MenuType<?> type, int id, Inventory inv, StorageControllerBlockEntity contentHolder) {
        super(type, id, inv, contentHolder);
    }

    public static ControllerMenu create(int id, Inventory inv, StorageControllerBlockEntity be) {
        return new ControllerMenu(ModGuis.CONTROLLER_MENU.get(), id, inv, be);
    }

    @Override
    protected StorageControllerBlockEntity createOnClient(FriendlyByteBuf extraData) {
        return null;
    }

    @Override
    protected void initAndReadInventory(StorageControllerBlockEntity contentHolder) {

    }

    @Override
    protected void addSlots() {
        craftingGrid = new CraftingContainer(this, 3, 3);
        craftingResult = new ResultContainer();

        int x = -17;
        int y = 113;

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                this.addSlot(new Slot(craftingGrid, j + i * 3, x + 30 + j * 18, y + 17 + i * 18));
            }
        }
        this.addSlot(new ResultSlot(player, craftingGrid, craftingResult, 0, 103, 148));

        int terminalX = -5;
        int terminalY = 24;

        for (int i = 0;i < 5;++i) {
            for (int j = 0;j < 9;++j) {
                addSlot(new SlotItemHandler(new ItemStackHandler(), 0, terminalX + j * 18, terminalY + i * 18) );
            }
        }

        addPlayerSlots(-22, 203);
    }

    @Override
    protected void saveData(StorageControllerBlockEntity contentHolder) {

    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }
}
