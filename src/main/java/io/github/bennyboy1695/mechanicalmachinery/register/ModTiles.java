package io.github.bennyboy1695.mechanicalmachinery.register;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import io.github.bennyboy1695.mechanicalmachinery.MechanicalMachinery;
import io.github.bennyboy1695.mechanicalmachinery.block.sifter.SifterTileEntity;
import io.github.bennyboy1695.mechanicalmachinery.block.sifter.client.SifterInstance;
import io.github.bennyboy1695.mechanicalmachinery.block.sifter.client.SifterRenderer;

public class ModTiles {

    public static final BlockEntityEntry<SifterTileEntity> SIFTER = MechanicalMachinery.getRegister()
            .tileEntity("sifter", SifterTileEntity::new)
            .instance(() -> SifterInstance::new)
            .renderer(() -> SifterRenderer::new)
            .validBlocks(ModBlocks.SIFTER)
            .register();

    public static void register() {

    }

/*    public static final BlockEntityEntry<SifterTileEntity> SIFTER = mechanicalmachinery.getRegister()
            .tileEntity("sifter", SifterTileEntity::new)
            .validBlocks(ModBlocks.SIFTER::get)
            .register();*/
}
