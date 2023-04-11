package io.github.bennyboy1695.mechanicalsifting.register;

import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.util.entry.ItemEntry;
import io.github.bennyboy1695.mechanicalsifting.MechanicalSifting;
import io.github.bennyboy1695.mechanicalsifting.item.MeshItem;
import io.github.bennyboy1695.mechanicalsifting.util.ColourUtils;
import io.github.bennyboy1695.mechanicalsifting.util.ItemColourImpl;

public class ModItems {

    public static final ItemEntry<MeshItem> MESH = MechanicalSifting.getRegister()
            .item("mesh", MeshItem::new)
            .model(AssetLookup.customGenericItemModel("mesh"))
            .register();
    public static final ItemEntry<MeshItem> TEST_MESH = MechanicalSifting.getRegister()
            .item("test_mesh", MeshItem::new)
            .color(() -> ItemColourImpl.supplier(ColourUtils.intColor(69, 73, 72), 0))
            .model(AssetLookup.customGenericItemModel("mesh"))
            .register();

    public static void register() {

    }

}
