package com.shieldmechanics.event;

import com.shieldmechanics.ShieldDataGatherer;
import com.shieldmechanics.Shieldmechanics;
import com.shieldmechanics.enchant.BlockDamageEnchant;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ClientEventHandler
{
    private static boolean init = false;

    public static void addTooltip(final ItemStack stack, List<Component> toolTips)
    {
        if (!init)
        {
            init = true;
            ShieldDataGatherer.detectItems();
        }

        if (Shieldmechanics.isShield(stack))
        {
            final ShieldDataGatherer.ShieldData data = ShieldDataGatherer.shields.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
            if (data == null)
            {
                toolTips
                  .add(Component.translatable(
                      "shieldmechanics.blockdmgreduct", (ShieldDataGatherer.getDefaultBlockReductionPct(stack)
                                                           + BlockDamageEnchant.getAdditionalBlockChanceFor(stack)) + "%")
                         .setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)));

                toolTips
                  .add(Component.translatable(
                      "shieldmechanics.holddmgreduct", ShieldDataGatherer.getDefaultHoldReductionPct(stack) + "%")
                         .setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)));
                return;
            }

            toolTips
              .add(Component.translatable(
                  "shieldmechanics.blockdmgreduct", (data.onBlockDamageReductionPercent + BlockDamageEnchant.getAdditionalBlockChanceFor(stack)) + "%")
                     .setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)));

            toolTips
              .add(Component.translatable(
                  "shieldmechanics.holddmgreduct", data.onHoldDamageReductionPercent + "%")
                     .setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)));
        }
    }
}
