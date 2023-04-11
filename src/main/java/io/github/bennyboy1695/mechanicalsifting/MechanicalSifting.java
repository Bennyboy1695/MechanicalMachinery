package io.github.bennyboy1695.mechanicalsifting;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import io.github.bennyboy1695.mechanicalsifting.register.ModTiles;
import io.github.bennyboy1695.mechanicalsifting.register.ModBlocks;
import io.github.bennyboy1695.mechanicalsifting.register.ModItems;
import io.github.bennyboy1695.mechanicalsifting.util.MechancialSiftingTab;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(MechanicalSifting.MOD_ID)
public class MechanicalSifting {

    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "mechanicalsifting";
    private static final NonNullSupplier<CreateRegistrate> registrate = CreateRegistrate.lazy(MOD_ID);
    public MechanicalSifting() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        MechancialSiftingTab.register();

        //Init of mod stuffs
        ModBlocks.register();
        ModItems.register();
        ModTiles.register();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MechanicalSiftingClient.onClient(modEventBus, forgeEventBus));
    }

    public static CreateRegistrate getRegister() {
        return registrate.get();
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
