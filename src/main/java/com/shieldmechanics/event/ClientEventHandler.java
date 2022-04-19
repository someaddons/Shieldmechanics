package com.shieldmechanics.event;

import com.shieldmechanics.ShieldDataGatherer;
import com.shieldmechanics.Shieldmechanics;
import com.shieldmechanics.enchant.BlockDamageEnchant;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEventHandler
{
    private static boolean init = false;

    @SubscribeEvent
    public static void on(ItemTooltipEvent event)
    {
        if (!init)
        {
            init = true;
            ShieldDataGatherer.detectItems();
        }

        if (Shieldmechanics.isShield(event.getItemStack()))
        {
            final ShieldDataGatherer.ShieldData data = ShieldDataGatherer.shields.get(event.getItemStack().getItem().getRegistryName());
            if (data == null)
            {
                event.getToolTip()
                  .add(new TranslatableComponent(
                    "shieldmechanics.blockdmgreduct", (ShieldDataGatherer.getDefaultBlockReductionPct(event.getItemStack())
                                                         + BlockDamageEnchant.getAdditionalBlockChanceFor(event.getItemStack())) + "%")
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));

                event.getToolTip()
                  .add(new TranslatableComponent(
                    "shieldmechanics.holddmgreduct", ShieldDataGatherer.getDefaultHoldReductionPct(event.getItemStack()) + "%")
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
                return;
            }

            event.getToolTip()
              .add(new TranslatableComponent(
                "shieldmechanics.blockdmgreduct", (data.onBlockDamageReductionPercent + BlockDamageEnchant.getAdditionalBlockChanceFor(event.getItemStack())) + "%")
                .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));

            event.getToolTip()
              .add(new TranslatableComponent(
                "shieldmechanics.holddmgreduct", data.onHoldDamageReductionPercent + "%")
                .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
        }
    }
}
