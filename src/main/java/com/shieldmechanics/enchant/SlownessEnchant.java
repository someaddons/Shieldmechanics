package com.shieldmechanics.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;

import static com.shieldmechanics.enchant.Enchants.SHIELD;

public class SlownessEnchant extends Enchantment
{
    /**
     * Enchant id
     */
    private final       String NAME_ID      = "block_slowness_enchant";
    public static final int    APPLY_CHANCE = 5;

    public SlownessEnchant(final Enchantment.Rarity rarity, final EquipmentSlotType[] slotTypes)
    {
        super(rarity, SHIELD, slotTypes);
        setRegistryName(NAME_ID);
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchant)
    {
        return this != enchant && enchant != Enchants.knockBackEnchant && enchant != Enchants.blindEnchant;
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
