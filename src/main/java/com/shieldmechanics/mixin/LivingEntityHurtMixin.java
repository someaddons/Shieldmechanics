package com.shieldmechanics.mixin;

import com.shieldmechanics.event.EventHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Stack;

@Mixin(LivingEntity.class)
public class LivingEntityHurtMixin
{
    @Shadow
    @Nullable
    protected Stack<DamageContainer> damageContainers;

    @Inject(method = "hurt", at = @At("HEAD"))
    private void onHurt(final DamageSource damageSource, final float f, final CallbackInfoReturnable<Boolean> cir)
    {
        EventHandler.onEntityAttack(damageSource, f, (LivingEntity) (Object) this);
    }

    // TODO: Also in serverplayer
    @ModifyVariable(method = "actuallyHurt", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float onHurt(final float dmg, final DamageSource damageSource, final float f)
    {
        final float newDamage = EventHandler.onEntityHurt(damageSource, damageContainers.peek().getNewDamage(), (LivingEntity) (Object) this);
        damageContainers.peek().setNewDamage(newDamage);
        return newDamage;
    }
}
