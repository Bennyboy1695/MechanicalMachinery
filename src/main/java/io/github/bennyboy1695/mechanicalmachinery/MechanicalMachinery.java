package io.github.bennyboy1695.mechanicalmachinery;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import io.github.bennyboy1695.mechanicalmachinery.block.storage.capability.ILinkCapability;
import io.github.bennyboy1695.mechanicalmachinery.data.recipe.gen.MechanicalMachineryRecipe;
import io.github.bennyboy1695.mechanicalmachinery.integration.create.SifterArmInteraction;
import io.github.bennyboy1695.mechanicalmachinery.register.*;
import io.github.bennyboy1695.mechanicalmachinery.util.MechancialMachineryTab;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(MechanicalMachinery.MOD_ID)
public class MechanicalMachinery {

    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "mechanicalmachinery";
    private static final CreateRegistrate registrate = CreateRegistrate.create(MOD_ID);
    public MechanicalMachinery() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        registrate.registerEventListeners(modEventBus);

        MechancialMachineryTab.register();

        //Init of mod stuffs
        ModBlocks.register();
        ModItems.register();
        ModBlockEntities.register();
        ModGuis.register();

        //Load Recipe Types
        ModRecipeTypes.register(modEventBus);

        //Load Arm Interactions
        SifterArmInteraction.register();

        ModCapabilities.register();
        modEventBus.addListener(EventPriority.LOWEST, this::gatherData);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MechanicalMachineryClient.onClient(modEventBus, forgeEventBus));
    }

    private void gatherData(GatherDataEvent event) {
        if (event.includeServer()) {
            MechanicalMachineryRecipe.registerAllProcessingProviders(event.getGenerator());
        }
    }

    public static CreateRegistrate getRegister() {
        return registrate;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
