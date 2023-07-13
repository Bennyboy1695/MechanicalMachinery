package io.github.bennyboy1695.mechanicalmachinery.register;

import com.tterrag.registrate.builders.MenuBuilder;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import io.github.bennyboy1695.mechanicalmachinery.MechanicalMachinery;
import io.github.bennyboy1695.mechanicalmachinery.block.storage.gui.ControllerMenu;
import io.github.bennyboy1695.mechanicalmachinery.block.storage.gui.ControllerScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ModGuis {
    public static MenuEntry<ControllerMenu> CONTROLLER_MENU = register("controller", ControllerMenu::new, () -> ControllerScreen::new);

    private static <C extends AbstractContainerMenu, S extends Screen & MenuAccess<C>> MenuEntry<C> register(
            String name, MenuBuilder.ForgeMenuFactory<C> factory, NonNullSupplier<MenuBuilder.ScreenFactory<C, S>> screenFactory) {
        return MechanicalMachinery.getRegister()
                .menu(name, factory, screenFactory)
                .register();
    }

    public static void register() {}
}
