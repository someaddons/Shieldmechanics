package com.shieldmechanics;

import com.cupboard.config.CupboardConfig;
import com.shieldmechanics.config.CommonConfiguration;
import com.shieldmechanics.enchant.Enchants;
import com.shieldmechanics.event.ClientEventHandler;
import com.shieldmechanics.event.EventHandler;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.Enchantable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

import static com.shieldmechanics.Shieldmechanics.MODID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MODID)
public class Shieldmechanics
{
    public static final String MODID = "shieldmechanics";

    public static final Random rand = new Random();
    public static final Logger                              LOGGER = LogManager.getLogger();
    public static       CupboardConfig<CommonConfiguration> config = new CupboardConfig<>("shieldmechanics", new CommonConfiguration());

    public Shieldmechanics(IEventBus modEventBus, ModContainer modContainer)
    {
        NeoForge.EVENT_BUS.addListener(this::setup);
        NeoForge.EVENT_BUS.register(EventHandler.class);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::modifyDefaultComponents);
        Enchants.init();
    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event)
    {
        NeoForge.EVENT_BUS.register(ClientEventHandler.class);
    }

    private void setup(final ServerStartingEvent event)
    {
        config.getCommonConfig();
        ShieldDataGatherer.detectItems();
        LOGGER.info("Shield mechanics initialized");
    }

    private void modifyDefaultComponents(final ModifyDefaultComponentsEvent event)
    {
        event.modifyMatching((item, c) -> c.has(DataComponents.BLOCKS_ATTACKS) && (c.has(DataComponents.ENCHANTABLE) && c.get(DataComponents.ENCHANTABLE).value() < 14 || !c.has(
            DataComponents.ENCHANTABLE)), builder -> builder.set(DataComponents.ENCHANTABLE, new Enchantable(14)));
    }

    public static boolean isShield(final ItemStack stack)
    {
        return !stack.isEmpty()
            && (stack.getItem() instanceof ShieldItem
            || stack.has(DataComponents.BLOCKS_ATTACKS));
    }
}
