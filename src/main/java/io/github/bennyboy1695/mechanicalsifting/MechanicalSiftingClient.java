package io.github.bennyboy1695.mechanicalsifting;

import io.github.bennyboy1695.mechanicalsifting.register.ModPartials;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class MechanicalSiftingClient {

    public static void onClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(MechanicalSiftingClient::registerRenderers);
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ModPartials.init();
    }
}
