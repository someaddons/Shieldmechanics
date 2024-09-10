package com.shieldmechanics.enchant;

import com.shieldmechanics.Shieldmechanics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import static com.shieldmechanics.enchant.Enchants.SHIELD;

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
        super(rarity, SHIELD, slotTypes);
    }

    @Override
    public boolean canEnchant(ItemStack stack)
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

    public int getMinCost(int p_77321_1_)
    {
        return 100 + p_77321_1_ * 10;
    }

    public int getMaxCost(int p_223551_1_)
    {
        return this.getMinCost(p_223551_1_) + 5;
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
