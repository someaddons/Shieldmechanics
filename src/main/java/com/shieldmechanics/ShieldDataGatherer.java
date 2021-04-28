package com.shieldmechanics;

import com.shieldmechanics.enchant.BlockDamageEnchant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

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
        final ShieldData data = shields.get(stack.getItem().getRegistryName());
        if (data == null)
        {
            return 0f;
        }

        return Math.max(0, data.onBlockDamageReduction - (BlockDamageEnchant.getAdditionalBlockChanceFor(stack) / 100f));
    }

    /**
     * Get the damage reduction for holding for the given item
     *
     * @param stack item stack
     * @return damage modifier
     */
    public static float getHoldDamageReductionFor(final ItemStack stack)
    {
        final ShieldData data = shields.get(stack.getItem().getRegistryName());
        if (data == null)
        {
            return 1f;
        }

        return data.onHoldDamageReduction;
    }

    public static void parseFromConfig()
    {
        //possibleMonsters = new ArrayList<>();
        shields = new HashMap<>();
        for (final String entry : Shieldmechanics.config.getCommonConfig().shields.get())
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

            final Item item = ForgeRegistries.ITEMS.getValue(main);
            if (item == null)
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
        for (final Map.Entry<RegistryKey<Item>, Item> itemEntry : ForgeRegistries.ITEMS.getEntries())
        {
            if (itemEntry.getValue() instanceof ShieldItem)
            {
                if (!shields.containsKey(itemEntry.getValue().getRegistryName()))
                {
                    Shieldmechanics.LOGGER.info("Found new shield item, adding: " + itemEntry.getValue().getRegistryName());
                    shields.put(itemEntry.getValue().getRegistryName(), ShieldData.generateForItem(itemEntry.getValue()));
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

            Shieldmechanics.config.getCommonConfig().shields.set(Arrays.asList(configList.toArray(new String[0])));
        }
    }

    /**
     * Holds active and passive damage modifiers
     */
    public static class ShieldData
    {
        /**
         * Vanilla shield durability, used to auto-calculate rough values for modded ones
         */
        private final static float DEFAULT_SHIELD_DURABILITY      = 336f;
        private final static int   DEFAULT_SHIELD_BLOCK_REDUCTION = 65;
        private final static int   DEFAULT_SHIELD_HOLD_REDUCTION  = 15;

        private float onBlockDamageReduction;
        public  int   onBlockDamageReductionPercent;
        private float onHoldDamageReduction;
        public  int   onHoldDamageReductionPercent;

        private ShieldData(final int onBlockDamageReductionPercent, final int onHoldDamageReductionPercent)
        {
            this.onBlockDamageReductionPercent = Math.min(onBlockDamageReductionPercent, Shieldmechanics.config.getCommonConfig().maxblockdamagereduction.get());
            this.onBlockDamageReduction = Math.max(0, (100 - onBlockDamageReductionPercent) / 100f);
            this.onHoldDamageReductionPercent = Math.min(onHoldDamageReductionPercent, Shieldmechanics.config.getCommonConfig().maxpassivedamagereduction.get());
            this.onHoldDamageReduction = Math.max(0, (100 - onHoldDamageReductionPercent) / 100f);
        }

        static ShieldData generateForItem(final Item item)
        {
            // Compare shield value based on durability to have the shield somewhat comepareable
            final double durabilityModifier = (item.getMaxDamage() / DEFAULT_SHIELD_DURABILITY) - 1;

            final int blockReduction = (int) (DEFAULT_SHIELD_BLOCK_REDUCTION + Math.min(5 * (durabilityModifier / 0.75f), 25));
            final int holdReduction = (int) (DEFAULT_SHIELD_HOLD_REDUCTION + Math.min(3 * (durabilityModifier / 0.75f), 15));

            return new ShieldData(blockReduction, holdReduction);
        }
    }
}
