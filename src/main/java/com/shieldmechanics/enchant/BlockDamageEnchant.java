package com.shieldmechanics.enchant;

import com.shieldmechanics.Shieldmechanics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import static net.minecraft.world.item.enchantment.EnchantmentCategory.WEARABLE;

/**
 * Enchant for adding extra damage against raiders
 */
public class BlockDamageEnchant extends Enchantment
{
    /**
     * Enchant id
     */
    public static final  String NAME_ID                       = "block_damage_enchant";
    private static final int    REDUCTION_BLOCK_BONUS_ENCHANT = 5;

    public BlockDamageEnchant(final Rarity rarity, final EquipmentSlot[] slotTypes)
    {
        super(rarity, WEARABLE, slotTypes);
    }

    @Override
    public boolean canEnchant(ItemStack stack)
    {
        return Shieldmechanics.isShield(stack.getItem());
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
        return Shieldmechanics.isShield(stack.getItem());
    }

    @Override
    public int getMinLevel()
    {
        return 1;
    }

    @Override
    public int getMaxLevel()
    {
        return 2;
    }

    @Override
    public int getMinCost(int i)
    {
        return 15 + (i - 1) * 9;
    }

    @Override
    public int getMaxCost(int i)
    {
        return super.getMinCost(i) + 50;
    }

    /**
     * Gets the additional block chance for an item
     *
     * @param stack
     * @return
     */
    public static int getAdditionalBlockChanceFor(final ItemStack stack)
    {
        return EnchantmentHelper.getItemEnchantmentLevel(Enchants.blockDamageEnchant, stack) * REDUCTION_BLOCK_BONUS_ENCHANT;
    }
}
