package com.shieldmechanics.enchant;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/**
 * Enchant for adding extra damage against raiders
 */
public class BlockDamageEnchant
{
    /**
     * Enchant id
     */
    public static final  String NAME_ID                       = "block_damage_enchant";
    private static final int    REDUCTION_BLOCK_BONUS_ENCHANT = 5;

    /**
     * Gets the additional block chance for an item
     *
     * @param stack
     * @return
     */
    public static int getAdditionalBlockChanceFor(final Registry<Enchantment> registry, final ItemStack stack)
    {
        return EnchantmentHelper.getItemEnchantmentLevel(registry.getHolderOrThrow(Enchants.blockDamageEnchant), stack) * REDUCTION_BLOCK_BONUS_ENCHANT;
    }

    public static int getAdditionalBlockChanceFor(final HolderLookup.RegistryLookup<Enchantment> holderLookup, final ItemStack stack)
    {
        return EnchantmentHelper.getItemEnchantmentLevel(holderLookup.getOrThrow(Enchants.blockDamageEnchant), stack) * REDUCTION_BLOCK_BONUS_ENCHANT;
    }
}
