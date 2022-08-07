package com.shieldmechanics.enchant;

import com.shieldmechanics.Shieldmechanics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.world.item.CreativeModeTab.TAB_COMBAT;

public class Enchants
{
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Shieldmechanics.MODID);

    public static EnchantmentCategory SHIELD;
    public static BlockDamageEnchant  blockDamageEnchant;
    public static KnockBackEnchant    knockBackEnchant;
    public static SlownessEnchant     slownessEnchant;
    public static BlindEnchant        blindEnchant;
    public static LastResortEnchant   lastResortEnchant;

    public static void init()
    {
        SHIELD = EnchantmentCategory.create("shield", Shieldmechanics::isShield);

        blockDamageEnchant = new BlockDamageEnchant(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[] {EquipmentSlot.OFFHAND});
        ENCHANTMENTS.register(BlockDamageEnchant.NAME_ID, () -> blockDamageEnchant);
        knockBackEnchant = new KnockBackEnchant(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[] {EquipmentSlot.OFFHAND});
        ENCHANTMENTS.register(KnockBackEnchant.NAME_ID, () -> knockBackEnchant);
        slownessEnchant = new SlownessEnchant(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[] {EquipmentSlot.OFFHAND});
        ENCHANTMENTS.register(SlownessEnchant.NAME_ID, () -> slownessEnchant);
        blindEnchant = new BlindEnchant(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[] {EquipmentSlot.OFFHAND});
        ENCHANTMENTS.register(BlindEnchant.NAME_ID, () -> blindEnchant);
        lastResortEnchant = new LastResortEnchant(Enchantment.Rarity.RARE, new EquipmentSlot[] {EquipmentSlot.OFFHAND});
        ENCHANTMENTS.register(LastResortEnchant.NAME_ID, () -> lastResortEnchant);

        final List<EnchantmentCategory> combatTypes = new ArrayList<>(Arrays.asList(TAB_COMBAT.getEnchantmentCategories()));
        combatTypes.add(SHIELD);
        TAB_COMBAT.setEnchantmentCategories(combatTypes.toArray(new EnchantmentCategory[0]));
    }
}
