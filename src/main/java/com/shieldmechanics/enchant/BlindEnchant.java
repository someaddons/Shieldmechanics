package com.shieldmechanics.enchant;

import com.shieldmechanics.Shieldmechanics;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

import static com.shieldmechanics.enchant.Enchants.SHIELD;

public class BlindEnchant extends Enchantment
{
    /**
     * Enchant id
     */
    private final       String NAME_ID      = "block_blind_enchant";
    public static final int    APPLY_CHANCE = 5;

    public BlindEnchant(final Rarity rarity, final EquipmentSlotType[] slotTypes)
    {
        super(rarity, SHIELD, slotTypes);
        setRegistryName(NAME_ID);
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchant)
    {
        return this != enchant && enchant != Enchants.slownessEnchant && enchant != Enchants.knockBackEnchant;
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
        return 1;
    }
}
