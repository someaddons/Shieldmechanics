package com.shieldmechanics.event;

import com.shieldmechanics.Shieldmechanics;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import static com.shieldmechanics.enchant.Enchants.*;

public class ModEventHandler
{

    public static final ResourceKey<CreativeModeTab> CREATIVE_TAB_ID = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Shieldmechanics.id("item_group"));
    public static final CreativeModeTab              CREATIVE_TAB    = FabricItemGroup.builder()
                                                                         .icon(() -> new ItemStack(Items.SHIELD))
                                                                         .title(Component.literal("Shield Mechanics"))
                                                                         .build();

    public static void fillCreativeTab()
    {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CREATIVE_TAB_ID, CREATIVE_TAB);

        ItemGroupEvents.modifyEntriesEvent(CREATIVE_TAB_ID).register(itemGroup -> {
            itemGroup.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(itemGroup.getContext().holders().lookup(Registries.ENCHANTMENT)
                                                                                              .get()
                                                                                              .getOrThrow(blockDamageEnchant), 2)));
            itemGroup.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(itemGroup.getContext().holders().lookup(Registries.ENCHANTMENT)
                                                                                              .get()
                                                                                              .getOrThrow(knockBackEnchant), 1)));
            itemGroup.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(itemGroup.getContext().holders().lookup(Registries.ENCHANTMENT)
                                                                                              .get()
                                                                                              .getOrThrow(slownessEnchant), 1)));
            itemGroup.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(itemGroup.getContext().holders().lookup(Registries.ENCHANTMENT)
                                                                                              .get()
                                                                                              .getOrThrow(blindEnchant), 1)));
            itemGroup.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(itemGroup.getContext().holders().lookup(Registries.ENCHANTMENT)
                                                                                              .get()
                                                                                              .getOrThrow(lastResortEnchant), 1)));
        });
    }
}
