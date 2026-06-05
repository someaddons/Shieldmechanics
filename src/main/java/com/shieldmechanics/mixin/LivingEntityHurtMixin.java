package com.shieldmechanics.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.shieldmechanics.event.EventHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.component.BlocksAttacks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityHurtMixin
{
    @Unique
    private boolean didBlock = false;

    @Inject(method = "hurtServer", at = @At("HEAD"))
    private void resetBlock(final ServerLevel level, final DamageSource source, final float damage, final CallbackInfoReturnable<Boolean> cir)
    {
        didBlock = false;
    }

    @WrapOperation(method = "applyItemBlocking", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/BlocksAttacks;resolveBlockedDamage(Lnet/minecraft/world/damagesource/DamageSource;FD)F"))
    private float calculateBlockDamage(final BlocksAttacks instance, final DamageSource source, final float dealtDamage, final double angle, final Operation<Float> original)
    {
        final float blockedDamage = original.call(instance, source, dealtDamage, angle);

        if (blockedDamage > 0)
        {
            didBlock = true;
            float percent = EventHandler.onBLock(source, dealtDamage, (LivingEntity) (Object) this) / dealtDamage;
            return blockedDamage * percent;
        }
        return blockedDamage;
    }

    @Inject(method = "applyItemBlocking", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/BlocksAttacks;hurtBlockingItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;F)V"))
    private void afterBlock(final ServerLevel level, final DamageSource source, final float damage, final CallbackInfoReturnable<Float> cir)
    {
        EventHandler.afterBlock(source, (LivingEntity) (Object) this);
    }

    @ModifyReturnValue(method = "getDamageAfterMagicAbsorb", at = @At(value = "RETURN"))
    private float adjustDamageTaken(final float original, final DamageSource damageSource, float damage)
    {
        if (!didBlock)
        {
            return EventHandler.calculatePassiveShieldDamage(damageSource, original, (LivingEntity) (Object) this);
        }

        return original;
    }
}
