package io.github.bennyboy1695.mechanicalsifting.data.recipe.gen;

import com.simibubi.create.content.contraptions.processing.ProcessingRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import com.simibubi.create.foundation.utility.recipe.IRecipeTypeInfo;
import io.github.bennyboy1695.mechanicalsifting.MechanicalSifting;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public abstract class MechanicalSiftingRecipe extends CreateRecipeProvider {


    protected static final List<MechanicalSiftingRecipe> GENERATORS = new ArrayList<>();


    public static void registerAllProcessingProviders(DataGenerator generator) {
        GENERATORS.add(new SifterRecipeGen(generator));

        generator.addProvider(true, new DataProvider() {
            @Override
            public void run(CachedOutput dc) throws IOException {
                GENERATORS.forEach(g -> {
                    try {
                        g.run(dc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public @NotNull String getName() {
                return "MechanicalSifting Processing Recipes";
            }
        });
    }


    public MechanicalSiftingRecipe(DataGenerator generator) {
        super(generator);
    }


    /* Functions from Create's ProcessingRecipeGen.java */

    protected <T extends ProcessingRecipe<?>> GeneratedRecipe create(String name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return create(MechanicalSifting.rl(name), transform);
    }

    protected <T extends ProcessingRecipe<?>> GeneratedRecipe create(ResourceLocation name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {

        return createWithDeferredId(() -> name, transform);
    }

    protected <T extends ProcessingRecipe<?>> GeneratedRecipe createWithDeferredId(Supplier<ResourceLocation> name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        ProcessingRecipeSerializer<T> serializer = getSerializer();
        GeneratedRecipe generatedRecipe =
                c -> transform.apply(new ProcessingRecipeBuilder<>(serializer.getFactory(), name.get()))
                        .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    protected <T extends ProcessingRecipe<?>> ProcessingRecipeSerializer<T> getSerializer() {
        return getRecipeType().getSerializer();
    }

    protected abstract IRecipeTypeInfo getRecipeType();

}
