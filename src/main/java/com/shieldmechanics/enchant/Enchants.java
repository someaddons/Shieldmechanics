package com.shieldmechanics.enchant;

import com.shieldmechanics.Shieldmechanics;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class Enchants
{
    public static BlockDamageEnchant  blockDamageEnchant;
    public static KnockBackEnchant    knockBackEnchant;
    public static SlownessEnchant     slownessEnchant;
    public static BlindEnchant        blindEnchant;
    public static LastResortEnchant   lastResortEnchant;

    public static void init()
    {
        blockDamageEnchant = new BlockDamageEnchant(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[] {EquipmentSlot.OFFHAND});
        Registry.register(BuiltInRegistries.ENCHANTMENT, Shieldmechanics.id(BlockDamageEnchant.NAME_ID), blockDamageEnchant);
        knockBackEnchant = new KnockBackEnchant(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[] {EquipmentSlot.OFFHAND});
        Registry.register(BuiltInRegistries.ENCHANTMENT, Shieldmechanics.id(KnockBackEnchant.NAME_ID), knockBackEnchant);
        slownessEnchant = new SlownessEnchant(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[] {EquipmentSlot.OFFHAND});
        Registry.register(BuiltInRegistries.ENCHANTMENT, Shieldmechanics.id(SlownessEnchant.NAME_ID), slownessEnchant);
        blindEnchant = new BlindEnchant(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[] {EquipmentSlot.OFFHAND});
        Registry.register(BuiltInRegistries.ENCHANTMENT, Shieldmechanics.id(BlindEnchant.NAME_ID), blindEnchant);
        lastResortEnchant = new LastResortEnchant(Enchantment.Rarity.RARE, new EquipmentSlot[] {EquipmentSlot.OFFHAND});
        Registry.register(BuiltInRegistries.ENCHANTMENT, Shieldmechanics.id(LastResortEnchant.NAME_ID), lastResortEnchant);
    }
}
