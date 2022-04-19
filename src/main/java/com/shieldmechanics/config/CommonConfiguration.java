package com.shieldmechanics.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class CommonConfiguration
{
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> shields;
    public final ForgeConfigSpec.ConfigValue<Boolean>                playerOnly;
    public final ForgeConfigSpec.ConfigValue<Integer>                blockCooldown;
    public final ForgeConfigSpec.ConfigValue<Integer>                maxblockdamagereduction;
    public final ForgeConfigSpec.ConfigValue<Integer>                maxpassivedamagereduction;
    public final ForgeConfigSpec                                     ForgeConfigSpecBuilder;

    protected CommonConfiguration(final ForgeConfigSpec.Builder builder)
    {
        builder.push("Shield mechanics settings");
        //COnfig: affect players or all entities

        builder.comment(
          "List of shield items and their damage values. Start the game once to fill these entries with all modded shields automatically. Format: Shield name id;shield damage reduction on block; shield passive damage reduction when just held."
            + "E.g. default: [\"minecraft:shield;65;15\"] this give the minecraft shield 65% damage reduction on block, and 15% passive damage reduction when not blocking but held."
            + "To put multiple values seperate them by commas like this:  [\"minecraft:shield;65;15\", \"minecraft:shield;65;15\"] ");
        shields = builder.defineList("shields",
          Arrays.asList(
            "minecraft:shield;65;15")
          , e -> e instanceof String && ((String) e).contains(":"));

        builder.comment("Should the mechanic changes only affect players");
        playerOnly = builder.define("playerOnly", false);

        builder.comment("Should blocking with a shield set it on cooldown(players only), values are in ticks. 20 ticks = 1 sec");
        blockCooldown = builder.defineInRange("blockCooldown", 5, 0, 100);

        builder.comment("Maximum percent of damage allowed to be blocked. Default:85");
        maxblockdamagereduction = builder.defineInRange("maxblockdamagereduction", 85, 0, 100);

        builder.comment("Maximum percent of damage reduction while holding a shield(not blocking). Default:25");
        maxpassivedamagereduction = builder.defineInRange("maxpassivedamagereduction", 25, 0, 100);

        // Escapes the current category level
        builder.pop();
        ForgeConfigSpecBuilder = builder.build();
    }
}
