package com.shieldmechanics;

import com.shieldmechanics.config.Configuration;
import com.shieldmechanics.event.EventHandler;
import com.shieldmechanics.event.ModEventHandler;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

import static com.shieldmechanics.Shieldmechanics.MODID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MODID)
public class Shieldmechanics
{
    public static final String MODID = "shieldmechanics";

    public static final Random        rand   = new Random();
    public static final Logger        LOGGER = LogManager.getLogger();
    public static       Configuration config = new Configuration();

    public Shieldmechanics()
    {
        ModLoadingContext.get()
          .registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> org.apache.commons.lang3.tuple.Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        Mod.EventBusSubscriber.Bus.MOD.bus().get().register(ModEventHandler.class);
        Mod.EventBusSubscriber.Bus.FORGE.bus().get().register(EventHandler.class);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        ShieldDataGatherer.parseFromConfig();
        ShieldDataGatherer.detectItems();
        LOGGER.info("Shield mechanics initialized");
    }
}
