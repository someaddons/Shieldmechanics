package com.shieldmechanics.event;

import com.shieldmechanics.ShieldDataGatherer;
import com.shieldmechanics.Shieldmechanics;
import com.shieldmechanics.enchant.BlockDamageEnchant;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
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

        if (Shieldmechanics.isShield(event.getItemStack().getItem()))
        {
            final ShieldDataGatherer.ShieldData data = ShieldDataGatherer.shields.get(event.getItemStack().getItem().getRegistryName());
            if (data == null)
            {
                return;
            }

            event.getToolTip()
              .add(new TextComponent(
                "Damage reduction on block: " + (data.onBlockDamageReductionPercent + BlockDamageEnchant.getAdditionalBlockChanceFor(event.getItemStack())) + "%")
                .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));

            event.getToolTip()
              .add(new TextComponent(
                "Damage reduction while holding: " + data.onHoldDamageReductionPercent + "%")
                .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
        }
    }
}
