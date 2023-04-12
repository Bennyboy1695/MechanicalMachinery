package io.github.bennyboy1695.mechanicalmachinery.util;

import io.github.bennyboy1695.mechanicalmachinery.MechanicalMachinery;
import io.github.bennyboy1695.mechanicalmachinery.register.ModBlocks;
import io.github.bennyboy1695.mechanicalmachinery.register.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MechancialMachineryTab extends CreativeModeTab {
    private MechancialMachineryTab(String label) {
        super(label);
    }

    public static void register() {
        MechanicalMachinery.getRegister().creativeModeTab(() -> new MechancialMachineryTab(MechanicalMachinery.MOD_ID));
    }

    @Override
    public ItemStack makeIcon() {
        return ModBlocks.SIFTER.asStack();
    }
}
