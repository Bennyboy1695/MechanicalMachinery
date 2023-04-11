package io.github.bennyboy1695.mechanicalsifting.data.recipe;

import com.simibubi.create.content.contraptions.components.crusher.AbstractCrushingRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder;
import io.github.bennyboy1695.mechanicalsifting.MechanicalSifting;
import io.github.bennyboy1695.mechanicalsifting.register.ModRecipeTypes;
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
        MechanicalSifting.getLogger().info(wrapper.toString());
        return false;
    }
}
