package com.shieldmechanics.event;

import com.shieldmechanics.ShieldDataGatherer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class ModEventHandler
{
    @SubscribeEvent
    public static void onConfigChanged(ModConfigEvent event)
    {
        ShieldDataGatherer.parseFromConfig();
    }
}
