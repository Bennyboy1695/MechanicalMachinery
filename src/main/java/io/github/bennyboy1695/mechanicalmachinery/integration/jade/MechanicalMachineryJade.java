package io.github.bennyboy1695.mechanicalmachinery.integration.jade;

import io.github.bennyboy1695.mechanicalmachinery.block.sifter.SifterBlock;
import io.github.bennyboy1695.mechanicalmachinery.block.sifter.SifterTileEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class MechanicalMachineryJade implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(SifterComponentProvider.INSTANCE, SifterTileEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(SifterComponentProvider.INSTANCE, SifterBlock.class);
    }
}
