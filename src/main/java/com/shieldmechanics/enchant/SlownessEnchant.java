package com.shieldmechanics.enchant;

import com.shieldmechanics.Shieldmechanics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import static net.minecraft.world.item.enchantment.EnchantmentCategory.WEARABLE;

public class SlownessEnchant extends Enchantment
{
    /**
     * Enchant id
     */
    public static final String NAME_ID      = "block_slowness_enchant";
    public static final int    APPLY_CHANCE = 5;

    public SlownessEnchant(final Rarity rarity, final EquipmentSlot[] slotTypes)
    {
        super(rarity, WEARABLE, slotTypes);
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchant)
    {
        return this != enchant && enchant != Enchants.knockBackEnchant && enchant != Enchants.blindEnchant;
    }

    @Override
    public boolean canEnchant(ItemStack stack)
    {
        return Shieldmechanics.isShield(stack.getItem());
    }

    @Override
    public int getMaxCost(int i)
    {
        return super.getMinCost(i) + 50;
    }

    @Override
    public int getMinCost(int i)
    {
        return 15 + (i - 1) * 9;
    }

    @Override
    public int getMinLevel()
    {
        return 1;
    }

    @Override
    public int getMaxLevel()
    {
        return 1;
    }
}
