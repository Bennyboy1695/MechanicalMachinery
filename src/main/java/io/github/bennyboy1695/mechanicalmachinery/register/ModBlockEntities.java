package io.github.bennyboy1695.mechanicalmachinery.register;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import io.github.bennyboy1695.mechanicalmachinery.MechanicalMachinery;
import io.github.bennyboy1695.mechanicalmachinery.block.sifter.SifterBlockEntity;
import io.github.bennyboy1695.mechanicalmachinery.block.sifter.client.SifterInstance;
import io.github.bennyboy1695.mechanicalmachinery.block.storage.controller.StorageControllerBlockEntity;
import io.github.bennyboy1695.mechanicalmachinery.block.storage.link.StorageLinkBlockEntity;

public class ModBlockEntities {

    public static final BlockEntityEntry<SifterBlockEntity> SIFTER = MechanicalMachinery.getRegister()
            .blockEntity("sifter", SifterBlockEntity::new)
            .instance(() -> SifterInstance::new)
            .validBlocks(ModBlocks.SIFTER)
/*            .renderer(() -> SifterRenderer::new)*/
            .register();

    public static final BlockEntityEntry<StorageControllerBlockEntity> STORAGE_CONTROLLER = MechanicalMachinery.getRegister()
            .blockEntity("storage_controller", StorageControllerBlockEntity::new)
            .validBlocks(ModBlocks.STORAGE_CONTROLLER)
            .register();

    public static final BlockEntityEntry<StorageLinkBlockEntity> STORAGE_LINK = MechanicalMachinery.getRegister()
            .blockEntity("storage_link", StorageLinkBlockEntity::new)
            .validBlocks(ModBlocks.STORAGE_LINK)
            .register();

    public static void register() {

    }
}
