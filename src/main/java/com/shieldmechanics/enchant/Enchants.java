package com.shieldmechanics.enchant;

import com.shieldmechanics.Shieldmechanics;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;

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
        ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
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
    }
}
