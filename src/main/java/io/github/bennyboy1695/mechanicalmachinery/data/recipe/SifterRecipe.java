package io.github.bennyboy1695.mechanicalmachinery.data.recipe;

import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import io.github.bennyboy1695.mechanicalmachinery.register.ModRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class SifterRecipe extends ProcessingRecipe<RecipeWrapper> {

    private int consumesFluid;
    private float addedStress;

    public SifterRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(ModRecipeTypes.SIFTER, params);
        this.consumesFluid = 0;
        this.addedStress = 0f;
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
    public void readAdditional(JsonObject json) {
        super.readAdditional(json);
        this.consumesFluid = GsonHelper.getAsInt(json, "consumesFluid", 0);
        this.addedStress = GsonHelper.getAsFloat(json, "addedStress", 0f);
    }

    @Override
    public void writeAdditional(JsonObject json) {
        super.writeAdditional(json);
        if (consumesFluid != 0)
            json.addProperty("consumesFluid", consumesFluid);
        if (addedStress != 0f)
            json.addProperty("addedStress", addedStress);
    }

    @Override
    public void readAdditional(FriendlyByteBuf buffer) {
        super.readAdditional(buffer);
        consumesFluid = buffer.readInt();
        addedStress = buffer.readFloat();
    }

    @Override
    public void writeAdditional(FriendlyByteBuf buffer) {
        super.writeAdditional(buffer);
        buffer.writeInt(consumesFluid);
        buffer.writeFloat(addedStress);
    }

    @Override
    public boolean matches(RecipeWrapper inv, Level level) {
        return (ingredients.get(0).getItems()[0].sameItem(inv.getItem(0)) || ingredients.get(0).getItems()[0].sameItem(inv.getItem(1))) && (ingredients.get(1).getItems()[0].sameItem(inv.getItem(1)) || ingredients.get(1).getItems()[0].sameItem(inv.getItem(0)));
    }

    public int consumesFluid() {
        return consumesFluid;
    }

    public float addedStress() {
        return addedStress;
    }

    public FluidIngredient requiredFluid() {
        if (fluidIngredients.isEmpty()) {
            return FluidIngredient.EMPTY;
        }
        return fluidIngredients.get(0);
    }
}
