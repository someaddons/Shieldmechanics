package com.shieldmechanics.mixin;

import com.shieldmechanics.event.EventHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.class)
public abstract class PlayerHurtMixin extends LivingEntity
{
    protected PlayerHurtMixin(final EntityType<? extends LivingEntity> p_20966_, final Level p_20967_)
    {
        super(p_20966_, p_20967_);
    }

    @ModifyVariable(method = "actuallyHurt", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float onHurt(final float dmg, final DamageSource damageSource, final float f)
    {
        final float newDamage = EventHandler.onEntityHurt(damageSource, damageContainers.peek().getNewDamage(), (LivingEntity) (Object) this);
        damageContainers.peek().setNewDamage(newDamage);
        return newDamage;
    }
}
