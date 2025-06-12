package com.shieldmechanics.mixin;

import com.shieldmechanics.event.EventHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.class)
public class PlayerHurtMixin
{
    @ModifyVariable(method = "actuallyHurt", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float onHurt(final float dmg, final DamageSource damageSource, final float f)
    {
        return EventHandler.onEntityHurt(damageSource, dmg, (LivingEntity) (Object) this);
    }
}
