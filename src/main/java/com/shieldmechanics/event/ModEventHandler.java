package com.shieldmechanics.event;

import com.shieldmechanics.ShieldDataGatherer;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import static com.shieldmechanics.enchant.Enchants.*;
import static com.shieldmechanics.enchant.Enchants.lastResortEnchant;

public class ModEventHandler
{
    @SubscribeEvent
    public static void onConfigChanged(ModConfigEvent event)
    {
        ShieldDataGatherer.parseFromConfig();
    }

    @SubscribeEvent
    public static void fillCreativeTab(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey().equals(CreativeModeTabs.COMBAT))
        {
            event.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(blockDamageEnchant, blockDamageEnchant.getMaxLevel())));
            event.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(knockBackEnchant, knockBackEnchant.getMaxLevel())));
            event.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(slownessEnchant, slownessEnchant.getMaxLevel())));
            event.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(blindEnchant, blindEnchant.getMaxLevel())));
            event.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(lastResortEnchant, lastResortEnchant.getMaxLevel())));
        }
    }
}
