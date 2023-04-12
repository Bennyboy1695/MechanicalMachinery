package io.github.bennyboy1695.mechanicalsifting.integration.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import io.github.bennyboy1695.mechanicalsifting.item.MeshItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class MechanicalSiftingKubePlugin extends KubeJSPlugin {

    @Override
    public void init() {
        RegistryObjectBuilderTypes.ITEM.addType("mechanicalsifting:mesh", MeshBuilder.class, MeshBuilder::new);
    }



    private static class MeshBuilder extends ItemBuilder {
        public MeshBuilder(ResourceLocation resourceLocation) {
            super(resourceLocation);
            parentModel("mechanicalsifting:item/mesh");
        }

        @Override
        public Item createObject() {
            return new MeshItem(createItemProperties());
        }
    }
}
