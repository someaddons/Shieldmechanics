package com.shieldmechanics.config;

import com.cupboard.config.ICommonConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.shieldmechanics.ShieldDataGatherer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommonConfiguration implements ICommonConfig
{
    public List<String> shields                   = Arrays.asList("minecraft:shield;65;15");
    public boolean      playerOnly                = false;
    public int          blockCooldown             = 5;
    public int          maxblockdamagereduction   = 85;
    public int          maxpassivedamagereduction = 25;

    public CommonConfiguration()
    {
    }

    public JsonObject serialize()
    {
        final JsonObject root = new JsonObject();

        final JsonObject entry = new JsonObject();
        entry.addProperty("desc:",
          "List of shield items and their damage values. Start the game once to fill these entries with all modded shields automatically. Format: Shield name id;shield damage reduction on block; shield passive damage reduction when just held."
            + "E.g. default: [\"minecraft:shield;65;15\"] this give the minecraft shield 65% damage reduction on block, and 15% passive damage reduction when not blocking but held."
            + "To put multiple values seperate them by commas like this:  [\"minecraft:shield;65;15\", \"minecraft:shield;65;15\"] ");
        final JsonArray types = new JsonArray();
        for (final String id : shields)
        {
            types.add(id);
        }
        entry.add("shields", types);
        root.add("shields", entry);


        final JsonObject entry2 = new JsonObject();
        entry2.addProperty("desc:", "Should the mechanic changes only affect players, default:false");
        entry2.addProperty("playerOnly", playerOnly);
        root.add("playerOnly", entry2);

        final JsonObject entry3 = new JsonObject();
        entry3.addProperty("desc:", "Shield block cooldown(players only), values are in ticks. 20 ticks = 1 sec, default = 5 ticks(0.25s)");
        entry3.addProperty("blockCooldown", blockCooldown);
        root.add("blockCooldown", entry3);

        final JsonObject entry4 = new JsonObject();
        entry4.addProperty("desc:", "Maximum percent of damage allowed to be blocked. Default:85");
        entry4.addProperty("maxblockdamagereduction", maxblockdamagereduction);
        root.add("maxblockdamagereduction", entry4);

        final JsonObject entry5 = new JsonObject();
        entry5.addProperty("desc:", "Maximum percent of passive damage reduction while holding a shield(not blocking). Default:25");
        entry5.addProperty("maxpassivedamagereduction", maxpassivedamagereduction);
        root.add("maxpassivedamagereduction", entry5);


        return root;
    }

    public void deserialize(JsonObject data)
    {
        playerOnly = data.get("playerOnly").getAsJsonObject().get("playerOnly").getAsBoolean();
        blockCooldown = data.get("blockCooldown").getAsJsonObject().get("blockCooldown").getAsInt();
        maxblockdamagereduction = data.get("maxblockdamagereduction").getAsJsonObject().get("maxblockdamagereduction").getAsInt();
        maxpassivedamagereduction = data.get("maxpassivedamagereduction").getAsJsonObject().get("maxpassivedamagereduction").getAsInt();
        JsonArray villageTypes = data.get("shields").getAsJsonObject().get("shields").getAsJsonArray();
        shields = new ArrayList<>();
        for (final JsonElement entry : villageTypes)
        {
            shields.add(entry.getAsString());
        }

        ShieldDataGatherer.parseFromConfig();
    }
}
