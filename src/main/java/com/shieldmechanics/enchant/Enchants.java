package com.shieldmechanics.enchant;

import com.shieldmechanics.Shieldmechanics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.world.item.CreativeModeTab.TAB_COMBAT;

@Mod.EventBusSubscriber(modid = Shieldmechanics.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Enchants
{
    public static EnchantmentCategory SHIELD;
    public static BlockDamageEnchant  blockDamageEnchant;
    public static KnockBackEnchant   knockBackEnchant;
    public static SlownessEnchant    slownessEnchant;
    public static BlindEnchant       blindEnchant;
    public static LastResortEnchant  lastResortEnchant;

    @SubscribeEvent
    public static void registerEnchants(final RegistryEvent.Register<Enchantment> event)
    {
        SHIELD = EnchantmentCategory.create("shield", Shieldmechanics::isShield);

        blockDamageEnchant = new BlockDamageEnchant(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[] {EquipmentSlot.OFFHAND});
        event.getRegistry().register(blockDamageEnchant);
        knockBackEnchant = new KnockBackEnchant(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[] {EquipmentSlot.OFFHAND});
        event.getRegistry().register(knockBackEnchant);
        slownessEnchant = new SlownessEnchant(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[] {EquipmentSlot.OFFHAND});
        event.getRegistry().register(slownessEnchant);
        blindEnchant = new BlindEnchant(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[] {EquipmentSlot.OFFHAND});
        event.getRegistry().register(blindEnchant);
        lastResortEnchant = new LastResortEnchant(Enchantment.Rarity.RARE, new EquipmentSlot[] {EquipmentSlot.OFFHAND});
        event.getRegistry().register(lastResortEnchant);

        final List<EnchantmentCategory> combatTypes = new ArrayList<>(Arrays.asList(TAB_COMBAT.getEnchantmentCategories()));
        combatTypes.add(SHIELD);
        TAB_COMBAT.setEnchantmentCategories(combatTypes.toArray(new EnchantmentCategory[0]));
    }
}
