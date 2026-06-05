package com.shieldmechanics.event;

import com.shieldmechanics.Shieldmechanics;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTabOutput;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import static com.shieldmechanics.enchant.Enchants.*;

public class ModEventHandler
{

    public static final ResourceKey<CreativeModeTab> CREATIVE_TAB_ID = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Shieldmechanics.id("item_group"));
    public static final CreativeModeTab CREATIVE_TAB = FabricCreativeModeTab.builder()
        .icon(() -> new ItemStack(Items.SHIELD))
        .title(Component.literal("Shield Mechanics"))
        .build();

    public static void fillCreativeTab()
    {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CREATIVE_TAB_ID, CREATIVE_TAB);

        CreativeModeTabEvents.modifyOutputEvent(CREATIVE_TAB_ID).register(itemGroup -> {
            createForEnchantment(itemGroup, blockDamageEnchant, 2);
            createForEnchantment(itemGroup, knockBackEnchant, 1);
            createForEnchantment(itemGroup, slownessEnchant, 1);
            createForEnchantment(itemGroup, blindEnchant, 1);
            createForEnchantment(itemGroup, lastResortEnchant, 1);
        });
    }

    private static ItemStack createForEnchantment(final FabricCreativeModeTabOutput context, final ResourceKey<Enchantment> enchantment, final int level)
    {
        final Holder.Reference<Enchantment> enchantmentHolder = context.getContext().holders()
            .lookup(Registries.ENCHANTMENT)
            .orElseThrow()
            .getOrThrow(enchantment);

        final ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);

        var enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        enchantments.set(enchantmentHolder, level);

        book.set(DataComponents.STORED_ENCHANTMENTS, enchantments.toImmutable());
        return book;
    }
}
