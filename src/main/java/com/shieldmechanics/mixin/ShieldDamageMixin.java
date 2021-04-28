package com.shieldmechanics.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public class ShieldDamageMixin
{
  /*  @ModifyVariable(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hurtCurrentlyUsedShield(F)V", shift = At.Shift.BEFORE))
    private int onTick(int i)
    {
        return Math.min(i, ConfigurationCache.scheduledTickMax);
    }*/
}
