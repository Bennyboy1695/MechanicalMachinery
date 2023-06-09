package io.github.bennyboy1695.mechanicalmachinery.data.recipe.gen;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.bennyboy1695.mechanicalmachinery.MechanicalMachinery;
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

public abstract class MechanicalMachineryRecipe extends CreateRecipeProvider {


    protected static final List<SifterRecipeGen> GENERATORS = new ArrayList<>();


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
                return "MechanicalMachinery Processing Recipes";
            }
        });
    }


    public MechanicalMachineryRecipe(DataGenerator generator) {
        super(generator);
    }


    /* Functions from Create's ProcessingRecipeGen.java */

    protected <T extends ProcessingRecipe<?>> GeneratedRecipe create(String name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return create(MechanicalMachinery.rl(name), transform);
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
