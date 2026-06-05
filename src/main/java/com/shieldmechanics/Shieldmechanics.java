package com.shieldmechanics;

import com.cupboard.config.CupboardConfig;
import com.cupboard.util.ResourceLocation;
import com.shieldmechanics.compat.FabricShieldLibCompat;
import com.shieldmechanics.enchant.Enchants;
import com.shieldmechanics.event.ModEventHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.Enchantable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class Shieldmechanics implements ModInitializer
{
    public static final String MODID = "shieldmechanics";

    public static final Random                                                         rand   = new Random();
    public static final Logger                                                         LOGGER = LogManager.getLogger();
    public static       CupboardConfig<com.shieldmechanics.config.CommonConfiguration> config =
        new CupboardConfig<>("shieldmechanics", new com.shieldmechanics.config.CommonConfiguration());

    private void modifyDefaultComponents(DefaultItemComponentEvents.ModifyContext context)
    {
        context.modify(item -> {
            final DataComponentMap c = item.components();
            return c.has(DataComponents.BLOCKS_ATTACKS) && (c.has(DataComponents.ENCHANTABLE) && c.get(DataComponents.ENCHANTABLE).value() < 14
                || !c.has(DataComponents.ENCHANTABLE));
        }, (builder, item) -> builder.set(DataComponents.ENCHANTABLE, new Enchantable(14)));
    }

    public static boolean isShield(final ItemStack stack)
    {
        return !stack.isEmpty() && (stack.getItem() instanceof ShieldItem || stack.has(DataComponents.BLOCKS_ATTACKS));
    }

    @Override
    public void onInitialize()
    {
        if (FabricLoader.getInstance().isModLoaded("fabricshieldlib"))
        {
            FabricShieldLibCompat.initCompat();
            LOGGER.info("Shield lib compat init");
        }

        ServerLifecycleEvents.SERVER_STARTED.register(a -> ShieldDataGatherer.detectItems());
        DefaultItemComponentEvents.MODIFY.register(this::modifyDefaultComponents);
        Enchants.init();
        LOGGER.info("Shield mechanics initialized");
        ModEventHandler.fillCreativeTab();
    }

    public static ResourceLocation id(String name)
    {
        return ResourceLocation.fromNamespaceAndPath(MODID, name);
    }
}
