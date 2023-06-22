package com.shieldmechanics.event;

import com.shieldmechanics.ShieldDataGatherer;
import com.shieldmechanics.Shieldmechanics;
import com.shieldmechanics.enchant.BlockDamageEnchant;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import static com.shieldmechanics.enchant.Enchants.*;
import static com.shieldmechanics.enchant.Enchants.lastResortEnchant;

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
            final ShieldDataGatherer.ShieldData data = ShieldDataGatherer.shields.get(ForgeRegistries.ITEMS.getKey(event.getItemStack().getItem()));
            if (data == null)
            {
                event.getToolTip()
                  .add(Component.translatable(
                      "shieldmechanics.blockdmgreduct", (ShieldDataGatherer.getDefaultBlockReductionPct(event.getItemStack())
                                                           + BlockDamageEnchant.getAdditionalBlockChanceFor(event.getItemStack())) + "%")
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)));

                event.getToolTip()
                  .add(Component.translatable(
                      "shieldmechanics.holddmgreduct", ShieldDataGatherer.getDefaultHoldReductionPct(event.getItemStack()) + "%")
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)));
                return;
            }

            event.getToolTip()
              .add(Component.translatable(
                  "shieldmechanics.blockdmgreduct", (data.onBlockDamageReductionPercent + BlockDamageEnchant.getAdditionalBlockChanceFor(event.getItemStack())) + "%")
                .setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)));

            event.getToolTip()
              .add(Component.translatable(
                  "shieldmechanics.holddmgreduct", data.onHoldDamageReductionPercent + "%")
                .setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)));
        }
    }
}
