package com.shieldmechanics;

import com.shieldmechanics.config.Configuration;
import com.shieldmechanics.event.ClientEventHandler;
import com.shieldmechanics.event.EventHandler;
import com.shieldmechanics.event.ModEventHandler;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, config.getCommonConfig().ForgeConfigSpecBuilder);
        Mod.EventBusSubscriber.Bus.MOD.bus().get().register(ModEventHandler.class);
        Mod.EventBusSubscriber.Bus.FORGE.bus().get().register(EventHandler.class);
        Mod.EventBusSubscriber.Bus.FORGE.bus().get().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event)
    {
        Mod.EventBusSubscriber.Bus.FORGE.bus().get().register(ClientEventHandler.class);
    }

    private void setup(final ServerStartingEvent event)
    {
        ShieldDataGatherer.parseFromConfig();
        ShieldDataGatherer.detectItems();
        LOGGER.info("Shield mechanics initialized");
    }

    public static boolean isShield(final Item item)
    {
        return item instanceof ShieldItem || item.canPerformAction(item.getDefaultInstance(), net.minecraftforge.common.ToolActions.SHIELD_BLOCK);
    }

    public static boolean isShield(final ItemStack stack)
    {
        return stack.getItem() instanceof ShieldItem || stack.getItem().canPerformAction(stack, net.minecraftforge.common.ToolActions.SHIELD_BLOCK);
    }
}
