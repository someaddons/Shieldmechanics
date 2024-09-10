package com.shieldmechanics;

import com.cupboard.config.CupboardConfig;
import com.shieldmechanics.compat.FabricShieldLibCompat;
import com.shieldmechanics.enchant.Enchants;
import com.shieldmechanics.event.ModEventHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Shieldmechanics implements ModInitializer
{
    public static final String MODID = "shieldmechanics";

    public static final Random                                                         rand   = new Random();
    public static final Logger                                                         LOGGER = LogManager.getLogger();
    public static       CupboardConfig<com.shieldmechanics.config.CommonConfiguration> config =
      new CupboardConfig<>("shieldmechanics", new com.shieldmechanics.config.CommonConfiguration());

    public static List<Class> shieldItemTypes = new ArrayList<>();

    public static boolean isShield(final Item item)
    {
        for (final Class clazz : shieldItemTypes)
        {
            if (clazz.isInstance(item))
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isShield(final ItemStack stack)
    {
        return isShield(stack.getItem());
    }

    @Override
    public void onInitialize()
    {
        shieldItemTypes.add(ShieldItem.class);

        if (FabricLoader.getInstance().isModLoaded("fabricshieldlib"))
        {
            FabricShieldLibCompat.initCompat();
            LOGGER.info("Shield lib compat init");
        }

        Enchants.init();
        config.getCommonConfig();
        ShieldDataGatherer.detectItems();
        LOGGER.info("Shield mechanics initialized");
        ModEventHandler.fillCreativeTab();
    }

    public static ResourceLocation id(String name)
    {
        return new ResourceLocation(MODID, name);
    }
}
