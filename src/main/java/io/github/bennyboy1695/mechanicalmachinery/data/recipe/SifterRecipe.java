package io.github.bennyboy1695.mechanicalmachinery.data.recipe;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import io.github.bennyboy1695.mechanicalmachinery.register.ModRecipeTypes;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class SifterRecipe extends ProcessingRecipe<RecipeWrapper> {

    public SifterRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(ModRecipeTypes.SIFTER, params);
    }

    @Override
    protected int getMaxInputCount() {
        return 2;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 47;
    }

    @Override
    public boolean matches(RecipeWrapper inv, Level level) {
        return (ingredients.get(0).getItems()[0].sameItem(inv.getItem(0)) || ingredients.get(0).getItems()[0].sameItem(inv.getItem(1))) && (ingredients.get(1).getItems()[0].sameItem(inv.getItem(1)) || ingredients.get(1).getItems()[0].sameItem(inv.getItem(0)));
    }

    public FluidIngredient requiredFluid() {
        if (fluidIngredients.isEmpty()) {
            return FluidIngredient.EMPTY;
        }
        return fluidIngredients.get(0);
    }
}
