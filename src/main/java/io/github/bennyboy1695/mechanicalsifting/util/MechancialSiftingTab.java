package io.github.bennyboy1695.mechanicalsifting.util;

import io.github.bennyboy1695.mechanicalsifting.MechanicalSifting;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MechancialSiftingTab extends CreativeModeTab {
    private MechancialSiftingTab(String label) {
        super(label);
    }

    public static void register() {
        MechanicalSifting.getRegister().creativeModeTab(() -> new MechancialSiftingTab(MechanicalSifting.MOD_ID));
    }

    @Override
    public ItemStack makeIcon() {
        return Items.DRAGON_HEAD.getDefaultInstance();
    }
}
