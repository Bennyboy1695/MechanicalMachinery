package io.github.bennyboy1695.mechanicalmachinery;

import io.github.bennyboy1695.mechanicalmachinery.block.sifter.client.SifterRenderer;
import io.github.bennyboy1695.mechanicalmachinery.register.ModPartials;
import io.github.bennyboy1695.mechanicalmachinery.register.ModBlockEntities;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class MechanicalMachineryClient {

    public static void onClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(MechanicalMachineryClient::registerRenderers);
        modEventBus.addListener(MechanicalMachineryClient::setupEvent);
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ModPartials.init();
    }

    private static void setupEvent(FMLClientSetupEvent event) {
       /*TODO: Find out why the builder in ModTiles wont accept the renderer*/
        BlockEntityRenderers.register(ModBlockEntities.SIFTER.get(), SifterRenderer::new);
    }
}
