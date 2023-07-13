package io.github.bennyboy1695.mechanicalmachinery.register;

import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.util.entry.ItemEntry;
import io.github.bennyboy1695.mechanicalmachinery.MechanicalMachinery;
import io.github.bennyboy1695.mechanicalmachinery.item.LinkingToolItem;
import io.github.bennyboy1695.mechanicalmachinery.item.MeshItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModItems {
    public static final ItemEntry<MeshItem> MESH = MechanicalMachinery.getRegister()
            .item("mesh", MeshItem::new)
            .model(AssetLookup.customGenericItemModel("mesh"))
            .register();

    public static final ItemEntry<LinkingToolItem> LINKING_TOOL = MechanicalMachinery.getRegister()
            .item("linking_tool", LinkingToolItem::new)
            .model(AssetLookup.existingItemModel())
            .register();

    public static void register() {
        createSiftedOres();
    }

    private static void createSiftedOres() {
        var mcSiftedOres = Arrays.asList("copper", "iron", "gold", "diamond", "emerald", "nether_quartz", "ancient_debris", "lapis_lazuli");
        mcSiftedOres.forEach(name -> {
            MechanicalMachinery.getRegister()
                    .item(name + "_sifted_ore", Item::new)
                    .model(AssetLookup.itemModel("sifted_ore"))
                    .register();
        });
        if (ModList.get().isLoaded("create")) {
            var createSiftedOres = Arrays.asList("zinc");
            createSiftedOres.forEach(name -> {
                MechanicalMachinery.getRegister()
                        .item(name + "_sifted_ore", Item::new)
                        .model(AssetLookup.itemModel("sifted_ore"))
                        .register();
            });
        }
        if (ModList.get().isLoaded("mekanism")) {
            var mekanismSiftedOres = Arrays.asList("osmium");
            mekanismSiftedOres.forEach(name -> {
                MechanicalMachinery.getRegister()
                        .item(name + "_sifted_ore", Item::new)
                        .model(AssetLookup.itemModel("sifted_ore"))
                        .register();
            });
        }
    }
}
