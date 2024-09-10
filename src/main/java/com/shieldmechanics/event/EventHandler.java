package com.shieldmechanics.event;

import com.shieldmechanics.ShieldDataGatherer;
import com.shieldmechanics.Shieldmechanics;
import com.shieldmechanics.enchant.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import static com.shieldmechanics.enchant.Enchants.knockBackEnchant;
import static com.shieldmechanics.enchant.Enchants.slownessEnchant;

/**
 * Handler to catch server tick events
 */
public class EventHandler
{
    private static DamageSource source;
    private static float        amount;
    private static Entity       current;

    public static void onEntityAttack(final DamageSource source, final float amount, final LivingEntity entity)
    {
        if (entity == source.getEntity())
        {
            return;
        }

        if ((amount > 0.0F && entity.isDamageSourceBlocked(source) && Shieldmechanics.isShield(entity
                                                                                                 .getItemInHand(entity.getUsedItemHand())
                                                                                                 .getItem())))
        {
            EventHandler.source = source;
            EventHandler.amount = amount;
            EventHandler.current = entity;
        }
    }

    public static float onEntityHurt(final DamageSource hurtsource, final float hurtamount, final LivingEntity entity)
    {
        if (!(entity instanceof Player) && Shieldmechanics.config.getCommonConfig().playerOnly)
        {
            return hurtamount;
        }

        final ItemStack shieldItem = entity.getItemInHand(entity.getUsedItemHand());

        //Blocking
        if (entity == current && hurtamount == 0 && hurtsource == source)
        {
            // Knockback enchant
            final Entity sourceEntity = hurtsource.getDirectEntity();

            if (sourceEntity instanceof LivingEntity && current != null)
            {
                if (Shieldmechanics.rand.nextInt(KnockBackEnchant.KOCKBACK_CHANCE) == 0
                      && EnchantmentHelper.getItemEnchantmentLevel(knockBackEnchant, shieldItem) > 0)
                {
                    ((LivingEntity) sourceEntity).knockback(1.0F, current.getX() - sourceEntity.getX(), current.getZ() - sourceEntity.getZ());
                }

                if (Shieldmechanics.rand.nextInt(SlownessEnchant.APPLY_CHANCE) == 0
                      && EnchantmentHelper.getItemEnchantmentLevel(slownessEnchant, shieldItem) > 0)
                {
                    ((LivingEntity) sourceEntity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100));
                }

                if (Shieldmechanics.rand.nextInt(BlindEnchant.APPLY_CHANCE) == 0
                      && EnchantmentHelper.getItemEnchantmentLevel(Enchants.blindEnchant, shieldItem) > 0)
                {
                    ((LivingEntity) sourceEntity).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100));
                }

                if (entity.getHealth() < 10 && entity.getAbsorptionAmount() == 0
                      && Shieldmechanics.rand.nextInt(LastResortEnchant.APPLY_CHANCE) == 0
                      && EnchantmentHelper.getItemEnchantmentLevel(Enchants.lastResortEnchant, shieldItem) > 0)
                {
                    entity.setAbsorptionAmount(6);
                }
            }

            // BLock case
            if (entity instanceof Player && Shieldmechanics.config.getCommonConfig().blockCooldown > 0)
            {
                ((Player) entity).getCooldowns().addCooldown(shieldItem.getItem(), Shieldmechanics.config.getCommonConfig().blockCooldown);
            }

            return amount * ShieldDataGatherer.getBlockDamageReductionFor(shieldItem);
        }
        //Nonblocking
        else if (Shieldmechanics.isShield(shieldItem))
        {
            // No block mainhand
            return hurtamount * ShieldDataGatherer.getHoldDamageReductionFor(shieldItem);
        }

        return hurtamount;
    }
}
