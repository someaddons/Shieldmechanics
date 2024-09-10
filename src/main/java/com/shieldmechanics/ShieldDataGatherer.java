package com.shieldmechanics;

import com.shieldmechanics.enchant.BlockDamageEnchant;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.*;

/**
 * Represent a random village type
 */
public class ShieldDataGatherer
{
    /**
     * Parsed config list
     */
    public static Map<ResourceLocation, ShieldData> shields = new HashMap<>();

    /**
     * Get the damage reduction for blocking for the given item
     *
     * @param stack item stack
     * @return damage modifier
     */
    public static float getBlockDamageReductionFor(final ItemStack stack)
    {
        final ShieldData data = shields.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (data == null)
        {
            return (100 - getDefaultBlockReductionPct(stack)) / 100f;
        }

        return Math.max(0f, data.onBlockDamageReduction - (BlockDamageEnchant.getAdditionalBlockChanceFor(stack) / 100f));
    }

    /**
     * Get the damage reduction for holding for the given item
     *
     * @param stack item stack
     * @return damage modifier
     */
    public static float getHoldDamageReductionFor(final ItemStack stack)
    {
        final ShieldData data = shields.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (data == null)
        {
            return (100 - getDefaultHoldReductionPct(stack)) / 100f;
        }

        return data.onHoldDamageReduction;
    }

    public static void parseFromConfig()
    {
        shields = new HashMap<>();
        for (final String entry : Shieldmechanics.config.getCommonConfig().shields)
        {
            final String[] splitEntry = entry.split(";");
            if (splitEntry.length != 3)
            {
                Shieldmechanics.LOGGER.error("Config entry could not be parsed, wrong amount of parameters: " + entry);
                continue;
            }

            final ResourceLocation main = ResourceLocation.tryParse(splitEntry[0]);
            if (main == null)
            {
                Shieldmechanics.LOGGER.error("Config entry could not be parsed, not a valid resource location " + splitEntry[0]);
                continue;
            }

            final Item item = BuiltInRegistries.ITEM.get(main);
            if (item == Items.AIR)
            {
                Shieldmechanics.LOGGER.error("Config entry could not be parsed, not a valid item" + splitEntry[0]);
                continue;
            }

            int blockedDamagePercent;
            int blockedPassiveDamagePercent;
            try
            {
                blockedDamagePercent = Integer.parseInt(splitEntry[1]);
                blockedPassiveDamagePercent = Integer.parseInt(splitEntry[2]);
            }
            catch (Exception e)
            {
                Shieldmechanics.LOGGER.error("Config entry could not be parsed, not a number" + splitEntry[1] + splitEntry[2]);
                continue;
            }

            shields.put(main, new ShieldData(blockedDamagePercent, blockedPassiveDamagePercent));
        }
    }

    /**
     * Detects missing items and generates them
     */
    public static void detectItems()
    {
        boolean newEntries = false;
        for (final Map.Entry<ResourceKey<Item>, Item> itemEntry : BuiltInRegistries.ITEM.entrySet())
        {
            if (Shieldmechanics.isShield(itemEntry.getValue()))
            {
                if (!shields.containsKey(BuiltInRegistries.ITEM.getKey(itemEntry.getValue())))
                {
                    shields.put(BuiltInRegistries.ITEM.getKey(itemEntry.getValue()),
                      ShieldData.generateForItem(itemEntry.getValue().getMaxDamage()));
                    Shieldmechanics.LOGGER.info("Found new shield item, adding: " + BuiltInRegistries.ITEM.getKey(itemEntry.getValue()) + " with stats:"
                                                  + " Durability: " + itemEntry.getValue().getMaxDamage() + " BlockDamageReduction: "
                                                  + shields.get(BuiltInRegistries.ITEM.getKey(itemEntry.getValue())).onBlockDamageReductionPercent + " HoldDamageReduction: "
                                                  + shields.get(
                      BuiltInRegistries.ITEM.getKey(itemEntry.getValue())).onHoldDamageReductionPercent);
                    newEntries = true;
                }
            }
        }

        if (newEntries)
        {
            // put into config
            List<String> configList = new ArrayList<>();
            for (Map.Entry<ResourceLocation, ShieldData> entry : shields.entrySet())
            {
                configList.add(entry.getKey() + ";" + entry.getValue().onBlockDamageReductionPercent + ";" + entry.getValue().onHoldDamageReductionPercent);
            }

            Shieldmechanics.config.getCommonConfig().shields = Arrays.asList(configList.toArray(new String[0]));
            Shieldmechanics.config.save();
        }
    }

    /**
     * Default value for missing shields, aka dynamically created shield types
     *
     * @param stack
     * @return
     */
    public static int getDefaultBlockReductionPct(final ItemStack stack)
    {
        final int durability = stack.getMaxDamage();
        return (int) Math.min(Shieldmechanics.config.getCommonConfig().maxblockdamagereduction, (durability / ShieldData.DEFAULT_SHIELD_DURABILITY) * 65);
    }

    /**
     * Default value for missing shields, aka dynamically created shield types
     *
     * @param stack
     * @return
     */
    public static int getDefaultHoldReductionPct(final ItemStack stack)
    {
        final int durability = stack.getMaxDamage();
        return (int) Math.min(Shieldmechanics.config.getCommonConfig().maxpassivedamagereduction, (durability / ShieldData.DEFAULT_SHIELD_DURABILITY) * 15);
    }

    /**
     * Holds active and passive damage modifiers
     */
    public static class ShieldData
    {
        /**
         * Vanilla shield durability, used to auto-calculate rough values for modded ones
         */
        private final static float  DEFAULT_SHIELD_DURABILITY      = 336f;
        private final static int    DEFAULT_SHIELD_BLOCK_REDUCTION = 50;
        private final static int    DEFAULT_SHIELD_HOLD_REDUCTION  = 10;
        private final static int    BASE_VARIANCE                  = 20;
        private final static double GAIN_FOR_100PERCENT            = 8.3;

        private float onBlockDamageReduction;
        public  int   onBlockDamageReductionPercent;
        private float onHoldDamageReduction;
        public  int   onHoldDamageReductionPercent;

        private ShieldData(final int onBlockDamageReductionPercent, final int onHoldDamageReductionPercent)
        {
            this.onBlockDamageReductionPercent = Math.min(onBlockDamageReductionPercent, Shieldmechanics.config.getCommonConfig().maxblockdamagereduction);
            this.onBlockDamageReduction = Math.max(0, (100 - onBlockDamageReductionPercent) / 100f);
            this.onHoldDamageReductionPercent = Math.min(onHoldDamageReductionPercent, Shieldmechanics.config.getCommonConfig().maxpassivedamagereduction);
            this.onHoldDamageReduction = Math.max(0, (100 - onHoldDamageReductionPercent) / 100f);
        }

        static ShieldData generateForItem(final int durability)
        {
            // Compare shield value based on durability to have the shield somewhat comepareable
            double durabilityModifier = Math.log(durability / DEFAULT_SHIELD_DURABILITY);
            // +-30% variance
            durabilityModifier += (Shieldmechanics.rand.nextInt((60)) - 30) / 100d;

            durabilityModifier += BASE_VARIANCE / GAIN_FOR_100PERCENT;

            // Distribute points randomly
            double blockPercentBonus = Math.min(Math.max((Shieldmechanics.rand.nextInt(100) / 100.0), (Shieldmechanics.rand.nextInt(100)) / 100.0) * durabilityModifier,
              Math.max(10.0, Shieldmechanics.config.getCommonConfig().maxblockdamagereduction - DEFAULT_SHIELD_BLOCK_REDUCTION) / GAIN_FOR_100PERCENT);
            double holdPercentBonus = Math.min((durabilityModifier - blockPercentBonus) / 2,
              Math.max(1.0, Shieldmechanics.config.getCommonConfig().maxpassivedamagereduction - DEFAULT_SHIELD_HOLD_REDUCTION) / GAIN_FOR_100PERCENT);

            // Re-add remaining points
            blockPercentBonus += durabilityModifier - (holdPercentBonus + blockPercentBonus);
            blockPercentBonus = Math.min(blockPercentBonus,
              Math.max(10.0, Shieldmechanics.config.getCommonConfig().maxblockdamagereduction - DEFAULT_SHIELD_BLOCK_REDUCTION) / GAIN_FOR_100PERCENT);

            // Re-add remaining points
            holdPercentBonus += (durabilityModifier - (holdPercentBonus + blockPercentBonus)) / 2;
            holdPercentBonus = Math.min(holdPercentBonus,
              Math.max(1.0, Shieldmechanics.config.getCommonConfig().maxpassivedamagereduction - DEFAULT_SHIELD_HOLD_REDUCTION) / GAIN_FOR_100PERCENT);

            final int blockReduction = DEFAULT_SHIELD_BLOCK_REDUCTION + (int) (GAIN_FOR_100PERCENT * blockPercentBonus);
            final int holdReduction = DEFAULT_SHIELD_HOLD_REDUCTION + (int) (GAIN_FOR_100PERCENT * holdPercentBonus);

            return new ShieldData(blockReduction, holdReduction);
        }
    }
}
