package io.github.bennyboy1695.mechanicalmachinery.integration.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import io.github.bennyboy1695.mechanicalmachinery.item.MeshItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class MechanicalMachineryKubePlugin extends KubeJSPlugin {

    @Override
    public void init() {
        RegistryObjectBuilderTypes.ITEM.addType("mechanicalmachinery:mesh", MeshBuilder.class, MeshBuilder::new);
    }



    private static class MeshBuilder extends ItemBuilder {
        public MeshBuilder(ResourceLocation resourceLocation) {
            super(resourceLocation);
            parentModel("mechanicalmachinery:item/mesh");
        }

        @Override
        public Item createObject() {
            return new MeshItem(createItemProperties());
        }
    }
}
