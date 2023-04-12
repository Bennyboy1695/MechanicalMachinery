package io.github.bennyboy1695.mechanicalmachinery.register;

import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.util.entry.ItemEntry;
import io.github.bennyboy1695.mechanicalmachinery.MechanicalMachinery;
import io.github.bennyboy1695.mechanicalmachinery.item.MeshItem;
import io.github.bennyboy1695.mechanicalmachinery.util.ColourUtils;
import io.github.bennyboy1695.mechanicalmachinery.util.ItemColourImpl;

public class ModItems {

    public static final ItemEntry<MeshItem> MESH = MechanicalMachinery.getRegister()
            .item("mesh", MeshItem::new)
            .model(AssetLookup.customGenericItemModel("mesh"))
            .register();
    public static final ItemEntry<MeshItem> TEST_MESH = MechanicalMachinery.getRegister()
            .item("test_mesh", MeshItem::new)
            .color(() -> ItemColourImpl.supplier(ColourUtils.intColor(69, 73, 72), 0))
            .model(AssetLookup.customGenericItemModel("mesh"))
            .register();

    public static void register() {

    }

}
