package io.github.bennyboy1695.mechanicalmachinery.register;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import io.github.bennyboy1695.mechanicalmachinery.MechanicalMachinery;
import io.github.bennyboy1695.mechanicalmachinery.block.sifter.SifterBlockEntity;
import io.github.bennyboy1695.mechanicalmachinery.block.sifter.client.SifterInstance;

public class ModBlockEntities {

    public static final BlockEntityEntry<SifterBlockEntity> SIFTER = MechanicalMachinery.getRegister()
            .blockEntity("sifter", SifterBlockEntity::new)
            .instance(() -> SifterInstance::new)
            .validBlocks(ModBlocks.SIFTER)
/*            .renderer(() -> SifterRenderer::new)*/
            .register();

    public static void register() {

    }
}
