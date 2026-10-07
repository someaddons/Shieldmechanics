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
            final int enchantmentValue = (event.getContext().registries() == null
                ? 0
                : BlockDamageEnchant.getAdditionalBlockChanceFor(event.getContext().registries().lookup(Registries.ENCHANTMENT).get(), event.getItemStack()));
            if (data == null)
            {
                event.getToolTip()
                  .add(Component.translatable(
                      "shieldmechanics.blockdmgreduct", (ShieldDataGatherer.getDefaultBlockReductionPct(event.getItemStack())
                              + enchantmentValue + "%"))
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
                      (data.onBlockDamageReductionPercent
                          + enchantmentValue + "%"))
                  .setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)));

            event.getToolTip()
              .add(Component.translatable(
                  "shieldmechanics.holddmgreduct", data.onHoldDamageReductionPercent + "%")
                     .setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)));
        }
    }
}
