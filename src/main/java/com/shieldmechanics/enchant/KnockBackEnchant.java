package com.shieldmechanics.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;

import static com.shieldmechanics.enchant.Enchants.SHIELD;

/**
 * Enchant for adding extra damage against raiders
 */
public class KnockBackEnchant extends Enchantment
{
    /**
     * Enchant id
     */
    private final       String NAME_ID         = "block_knock_back_enchant";
    public static final int    KOCKBACK_CHANCE = 3;

    public KnockBackEnchant(final Rarity rarity, final EquipmentSlotType[] slotTypes)
    {
        super(rarity, SHIELD, slotTypes);
        setRegistryName(NAME_ID);
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchant)
    {
        return this != enchant && enchant != Enchants.slownessEnchant && enchant != Enchants.blindEnchant;
    }

    @Override
    public boolean canEnchant(ItemStack stack)
    {
        return stack.getItem() instanceof ShieldItem;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
        return stack.getItem() instanceof ShieldItem;
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
