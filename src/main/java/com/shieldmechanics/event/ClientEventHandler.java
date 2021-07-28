package com.shieldmechanics.event;

import com.shieldmechanics.ShieldDataGatherer;
import com.shieldmechanics.Shieldmechanics;
import com.shieldmechanics.enchant.BlockDamageEnchant;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
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
                  .add(new StringTextComponent(
                    "Damage reduction on block: " + (ShieldDataGatherer.getDefaultBlockReductionPct(event.getItemStack())
                                                       + BlockDamageEnchant.getAdditionalBlockChanceFor(event.getItemStack())) + "%")
                         .setStyle(Style.EMPTY.withColor(TextFormatting.GREEN)));

                event.getToolTip()
                  .add(new StringTextComponent(
                    "Damage reduction while holding: " + ShieldDataGatherer.getDefaultHoldReductionPct(event.getItemStack()) + "%")
                         .setStyle(Style.EMPTY.withColor(TextFormatting.GREEN)));
                return;
            }

            event.getToolTip()
              .add(new StringTextComponent(
                "Damage reduction on block: " + (data.onBlockDamageReductionPercent + BlockDamageEnchant.getAdditionalBlockChanceFor(event.getItemStack())) + "%")
                     .setStyle(Style.EMPTY.withColor(TextFormatting.GREEN)));

            event.getToolTip()
              .add(new StringTextComponent(
                "Damage reduction while holding: " + data.onHoldDamageReductionPercent + "%")
                     .setStyle(Style.EMPTY.withColor(TextFormatting.GREEN)));
        }
    }
}
