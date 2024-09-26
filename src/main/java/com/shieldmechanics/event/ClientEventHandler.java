package com.shieldmechanics.event;

import com.shieldmechanics.ShieldDataGatherer;
import com.shieldmechanics.Shieldmechanics;
import com.shieldmechanics.enchant.BlockDamageEnchant;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

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
            final ShieldDataGatherer.ShieldData data = ShieldDataGatherer.shields.get(BuiltInRegistries.ITEM.getKey(event.getItemStack().getItem()));
            if (data == null)
            {
                event.getToolTip()
                  .add(Component.translatable(
                      "shieldmechanics.blockdmgreduct", (ShieldDataGatherer.getDefaultBlockReductionPct(event.getItemStack())
                                                           + BlockDamageEnchant.getAdditionalBlockChanceFor(event.getContext().registries().lookup(Registries.ENCHANTMENT).get(),
                        event.getItemStack())) + "%")
                         .setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)));

                event.getToolTip()
                  .add(Component.translatable(
                      "shieldmechanics.holddmgreduct", ShieldDataGatherer.getDefaultHoldReductionPct(event.getItemStack()) + "%")
                         .setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)));
                return;
            }

            event.getToolTip()
              .add(Component.translatable(
                  "shieldmechanics.blockdmgreduct",
                  (data.onBlockDamageReductionPercent + BlockDamageEnchant.getAdditionalBlockChanceFor(event.getContext().registries().lookup(Registries.ENCHANTMENT).get(),
                    event.getItemStack())) + "%")
                     .setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)));

            event.getToolTip()
              .add(Component.translatable(
                  "shieldmechanics.holddmgreduct", data.onHoldDamageReductionPercent + "%")
                     .setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)));
        }
    }
}
