package io.github.bennyboy1695.mechanicalmachinery;

import io.github.bennyboy1695.mechanicalmachinery.register.ModPartials;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class MechanicalMachineryClient {

    public static void onClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(MechanicalMachineryClient::registerRenderers);
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ModPartials.init();
    }
}
