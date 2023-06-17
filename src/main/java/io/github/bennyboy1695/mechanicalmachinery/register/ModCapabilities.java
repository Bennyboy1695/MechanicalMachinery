package io.github.bennyboy1695.mechanicalmachinery.register;

import io.github.bennyboy1695.mechanicalmachinery.block.storage.capability.ILinkCapability;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class ModCapabilities {

    public static final Capability<ILinkCapability> LINK_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    public static void register() {
        MinecraftForge.EVENT_BUS.addListener(ModCapabilities::registerCapabilities);
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(ILinkCapability.class);
    }
}
