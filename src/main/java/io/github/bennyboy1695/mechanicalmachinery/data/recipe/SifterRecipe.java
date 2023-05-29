package io.github.bennyboy1695.mechanicalmachinery.data.recipe;

import com.simibubi.create.content.kinetics.crusher.AbstractCrushingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import io.github.bennyboy1695.mechanicalmachinery.register.ModRecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class SifterRecipe extends AbstractCrushingRecipe {

    private ItemStack mesh;
    private ItemStack siftingIngredient;
    private FluidStack fluidRequirement;

    public SifterRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(ModRecipeTypes.SIFTER, params);
    }

    @Override
    protected int getMaxInputCount() {
        return 3;
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
    public boolean matches(RecipeWrapper wrapper, Level level) {
        return true;
    }
}
