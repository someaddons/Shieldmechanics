package com.shieldmechanics.enchant;

import com.shieldmechanics.Shieldmechanics;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ShieldItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = Shieldmechanics.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Enchants
{
    public static EnchantmentType    SHIELD;
    public static BlockDamageEnchant blockDamageEnchant;
    public static KnockBackEnchant   knockBackEnchant;
    public static SlownessEnchant    slownessEnchant;
    public static BlindEnchant       blindEnchant;
    public static LastResortEnchant  lastResortEnchant;

    @SubscribeEvent
    public static void registerEnchants(final RegistryEvent.Register<Enchantment> event)
    {
        SHIELD = EnchantmentType.create("shield", s -> s instanceof ShieldItem);

        blockDamageEnchant = new BlockDamageEnchant(Enchantment.Rarity.VERY_RARE, new EquipmentSlotType[] {EquipmentSlotType.OFFHAND});
        event.getRegistry().register(blockDamageEnchant);
        knockBackEnchant = new KnockBackEnchant(Enchantment.Rarity.VERY_RARE, new EquipmentSlotType[] {EquipmentSlotType.OFFHAND});
        event.getRegistry().register(knockBackEnchant);
        slownessEnchant = new SlownessEnchant(Enchantment.Rarity.VERY_RARE, new EquipmentSlotType[] {EquipmentSlotType.OFFHAND});
        event.getRegistry().register(slownessEnchant);
        blindEnchant = new BlindEnchant(Enchantment.Rarity.VERY_RARE, new EquipmentSlotType[] {EquipmentSlotType.OFFHAND});
        event.getRegistry().register(blindEnchant);
        lastResortEnchant = new LastResortEnchant(Enchantment.Rarity.VERY_RARE, new EquipmentSlotType[] {EquipmentSlotType.OFFHAND});
        event.getRegistry().register(lastResortEnchant);

        final List<EnchantmentType> combatTypes = new ArrayList<>(Arrays.asList(ItemGroup.TAB_COMBAT.getEnchantmentCategories()));
        combatTypes.add(SHIELD);
        ItemGroup.TAB_COMBAT.setEnchantmentCategories(combatTypes.toArray(new EnchantmentType[0]));
    }
}
