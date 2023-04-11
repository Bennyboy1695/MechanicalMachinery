package io.github.bennyboy1695.mechanicalsifting.register;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import io.github.bennyboy1695.mechanicalsifting.MechanicalSifting;
import io.github.bennyboy1695.mechanicalsifting.block.sifter.SifterTileEntity;
import io.github.bennyboy1695.mechanicalsifting.block.sifter.client.SifterInstance;
import io.github.bennyboy1695.mechanicalsifting.block.sifter.client.SifterRenderer;

public class ModTiles {

    public static final BlockEntityEntry<SifterTileEntity> SIFTER = MechanicalSifting.getRegister()
            .tileEntity("sifter", SifterTileEntity::new)
            .instance(() -> SifterInstance::new)
            .renderer(() -> SifterRenderer::new)
            .validBlocks(ModBlocks.SIFTER)
            .register();

    public static void register() {

    }

/*    public static final BlockEntityEntry<SifterTileEntity> SIFTER = MechanicalSifting.getRegister()
            .tileEntity("sifter", SifterTileEntity::new)
            .validBlocks(ModBlocks.SIFTER::get)
            .register();*/
}
