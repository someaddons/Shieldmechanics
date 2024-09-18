package com.shieldmechanics.mixin;

import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ShieldItem.class, priority = 187)
public abstract class ShieldItemMixin extends Item implements Equipable
{
    public ShieldItemMixin(final Properties properties)
    {
        super(properties);
    }

    @Override
    public int getEnchantmentValue()
    {
        return 14;
    }
}
