package io.github.bennyboy1695.mechanicalmachinery.integration.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import io.github.bennyboy1695.mechanicalmachinery.MechanicalMachinery;
import io.github.bennyboy1695.mechanicalmachinery.data.recipe.SifterRecipe;
import io.github.bennyboy1695.mechanicalmachinery.register.ModGUITextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SifterCategory extends CreateRecipeCategory<SifterRecipe> {

    public SifterCategory(CreateRecipeCategory.Info<SifterRecipe> info) {
        super(info);
    }


    public void setRecipe(IRecipeLayoutBuilder builder, SifterRecipe recipe, IFocusGroup focuses) {
        /*Ingredient siftable = recipe.getSiftableIngredient();*/
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 45).setBackground(getRenderedSlot(), -1, -1).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 62).setBackground(getRenderedSlot(), -1, -1).addIngredients(recipe.getIngredients().get(1));

        if (!recipe.getFluidIngredients().isEmpty()) {
            IRecipeSlotBuilder fluidSlot = builder.addSlot(RecipeIngredientRole.CATALYST, 10, 81).setBackground(getRenderedSlot(), -1, -1).addFluidStack(recipe.getFluidIngredients().get(0).getMatchingFluidStacks().get(0).getFluid(), 1000)
                    .addTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(1, Component.literal(ChatFormatting.RED + "Recipe requires this fluid to be present to work")));
            if (recipe.consumesFluid() != 0) {
                fluidSlot.addTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(2, Component.literal(ChatFormatting.RED + "Consumes " + ChatFormatting.AQUA + recipe.consumesFluid() + "mb")));
            }
        }

   /*     if (!recipe.getMeshIngredient().isEmpty())
            builder.addSlot(RecipeIngredientRole.CATALYST, 15, 24).setBackground(getRenderedSlot(), -1, -1).addIngredients(recipe.getMeshIngredient());
*/
        List<ProcessingOutput> results = recipe.getRollableResults();
        boolean single = results.size() == 1;
        int i = 0;
        for (Iterator<ProcessingOutput> var7 = results.iterator(); var7.hasNext() && i < 48; ++i) {
            ProcessingOutput output = var7.next();
            int xOffset = i % 7 * 19;
            int yOffset = i / 7 * 19;
            (builder.addSlot(RecipeIngredientRole.OUTPUT, single ? 139 : 45 + xOffset, 2 + yOffset)
                    .setBackground(getRenderedSlot(output), -1, -1)
                    .addItemStack(output.getStack()))
                    .addTooltipCallback(addStochasticTooltip(output));
        }
        if (results.size() > 47) {
            MechanicalMachinery.getLogger().warn("Recipe({}) is larger than 47 items output. This means its not going to render well", recipe.getId().toString());
        }

    }

    private List<List<ItemStack>> compressOutput(List<ProcessingOutput> processingOutputs) {
        List<List<ItemStack>> result = new ArrayList<>();
        int index = 0;
        boolean firstPass = true;
        for (ProcessingOutput output : processingOutputs) {
            if (firstPass)
                result.add(new ArrayList<>());
            result.get(index).add(output.getStack());
            index++;
            if (index >= 47) {
                index = 0;
                firstPass = false;
            }
        }
        return result;
    }


    public void draw(SifterRecipe recipe, IRecipeSlotsView iRecipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        List<ProcessingOutput> results = recipe.getRollableResults();
        boolean single = results.size() == 1;

        ModGUITextures.JEI_SMALL_ARROW.render(matrixStack, 30, 63); //Output arrow
    }

}
