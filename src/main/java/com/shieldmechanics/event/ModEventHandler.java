package com.shieldmechanics.event;

import com.shieldmechanics.Shieldmechanics;
import com.shieldmechanics.enchant.Enchants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import static com.shieldmechanics.enchant.Enchants.*;
import static com.shieldmechanics.enchant.Enchants.lastResortEnchant;

public class ModEventHandler
{
    @SubscribeEvent
    public static void fillCreativeTab(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey().equals(CreativeModeTabs.COMBAT))
        {
            event.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(event.getParameters().holders().lookup(Registries.ENCHANTMENT).get().getOrThrow(
              blockDamageEnchant), 2)));
            event.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(event.getParameters().holders().lookup(Registries.ENCHANTMENT)
                                                                                          .get()
                                                                                          .getOrThrow(knockBackEnchant), 1)));
            event.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(event.getParameters().holders().lookup(Registries.ENCHANTMENT)
                                                                                          .get()
                                                                                          .getOrThrow(slownessEnchant), 1)));
            event.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(event.getParameters().holders().lookup(Registries.ENCHANTMENT)
                                                                                          .get()
                                                                                          .getOrThrow(blindEnchant), 1)));
            event.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(event.getParameters().holders().lookup(Registries.ENCHANTMENT)
                                                                                          .get()
                                                                                          .getOrThrow(lastResortEnchant), 1)));
        }
    }
}
