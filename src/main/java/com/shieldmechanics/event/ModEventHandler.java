package com.shieldmechanics.event;

import com.shieldmechanics.ShieldDataGatherer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

public class ModEventHandler
{
    @SubscribeEvent
    public static void onConfigChanged(ModConfig.ModConfigEvent event)
    {
        ShieldDataGatherer.parseFromConfig();
    }
}
