package com.shieldmechanics.compat;

import com.github.crimsondawn45.fabricshieldlib.lib.object.FabricShield;
import com.shieldmechanics.Shieldmechanics;

public class FabricShieldLibCompat
{
    public static void initCompat()
    {
        Shieldmechanics.shieldItemTypes.add(FabricShield.class);
    }
}
