package com.shieldmechanics.enchant;

import com.shieldmechanics.Shieldmechanics;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.HashSet;
import java.util.Set;

public class Enchants
{
    public static final ResourceKey<Enchantment> blockDamageEnchant =
      ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Shieldmechanics.MODID, "block_damage_enchant"));
    public static final ResourceKey<Enchantment> knockBackEnchant   =
      ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Shieldmechanics.MODID, "block_knock_back_enchant"));
    public static final ResourceKey<Enchantment> slownessEnchant    =
      ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Shieldmechanics.MODID, "block_slowness_enchant"));
    public static final ResourceKey<Enchantment> blindEnchant       =
      ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Shieldmechanics.MODID, "block_blind_enchant"));
    public static final ResourceKey<Enchantment> lastResortEnchant  =
      ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Shieldmechanics.MODID, "block_last_resort_enchant"));

    public static       Set<ResourceKey<Enchantment>> ALL             = new HashSet<>();
    public static final TagKey<Item>                  SHIELD_ITEM_TAG = ItemTags.create(ResourceLocation.fromNamespaceAndPath(Shieldmechanics.MODID, "shields"));

    public static void init()
    {
        ALL.add(blockDamageEnchant);
        ALL.add(knockBackEnchant);
        ALL.add(slownessEnchant);
        ALL.add(blindEnchant);
        ALL.add(lastResortEnchant);
    }
}
