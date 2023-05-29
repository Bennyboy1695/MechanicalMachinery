package io.github.bennyboy1695.mechanicalmachinery.data.recipe.gen;

import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.bennyboy1695.mechanicalmachinery.register.ModItems;
import io.github.bennyboy1695.mechanicalmachinery.register.ModRecipeTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

public class SifterRecipeGen extends MechanicalMachineryRecipe {

    GeneratedRecipe TEST_RECIPE = create("test_recipe", builder -> builder.duration(300)
            .require(ModItems.TEST_MESH.get())
            .require(Blocks.GRAVEL)
            .require(Fluids.WATER.getSource(), 1000)
            .output(.8f, Items.IRON_NUGGET)
    );


    public SifterRecipeGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return ModRecipeTypes.SIFTER;
    }
}
