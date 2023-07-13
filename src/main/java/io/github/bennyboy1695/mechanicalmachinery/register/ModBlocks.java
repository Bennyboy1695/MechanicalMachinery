package io.github.bennyboy1695.mechanicalmachinery.register;

import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.bennyboy1695.mechanicalmachinery.MechanicalMachinery;
import io.github.bennyboy1695.mechanicalmachinery.block.sifter.SifterBlock;
import io.github.bennyboy1695.mechanicalmachinery.block.storage.controller.StorageControllerBlock;
import io.github.bennyboy1695.mechanicalmachinery.block.storage.link.StorageLinkBlock;
import io.github.bennyboy1695.mechanicalmachinery.util.BlockColourImpl;
import io.github.bennyboy1695.mechanicalmachinery.util.ColourUtils;
import io.github.bennyboy1695.mechanicalmachinery.util.ItemColourImpl;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.client.model.generators.ModelFile;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class ModBlocks {

    public static void register() {
        /*Create.REGISTRATE.addToSection(SIFTER, AllSections.KINETICS);*/
    }

    public static final BlockEntry<SifterBlock> SIFTER = MechanicalMachinery.getRegister().block("sifter", SifterBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.color(MaterialColor.METAL))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(BlockStressDefaults.setImpact(16.0))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.modLoc("sifter"))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<SandBlock> DUST = MechanicalMachinery.getRegister().block("dust", properties -> new SandBlock(ColourUtils.intColor(254,254,213), properties))
            .properties(p -> p.sound(SoundType.SAND))
            .defaultBlockstate()
            .item()
            .model((context, provider) -> provider.modLoc("dust"))
            .transform(customItemModel())
            .register();

    public static final BlockEntry<StorageControllerBlock> STORAGE_CONTROLLER = MechanicalMachinery.getRegister().block("storage_controller", StorageControllerBlock::new)
            .blockstate((context, provider) -> provider.simpleBlock(Blocks.NETHERITE_BLOCK, AssetLookup.standardModel(context, provider)))
            .properties(p -> p.color(MaterialColor.COLOR_ORANGE))
            .transform(BlockStressDefaults.setImpact(32.0))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<StorageLinkBlock> STORAGE_LINK = MechanicalMachinery.getRegister().block("storage_link", StorageLinkBlock::new)
            .properties(p -> p.color(MaterialColor.COLOR_ORANGE))
            .transform(BlockStressDefaults.setImpact(8.0))
            .transform(pickaxeOnly())
            .blockstate((context, provider) -> provider.simpleBlock(Blocks.NETHERITE_BLOCK, AssetLookup.standardModel(context, provider)))
            .item()
            .transform(customItemModel())
            .register();
}
