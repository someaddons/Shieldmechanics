package com.shieldmechanics.event;

import com.shieldmechanics.ShieldDataGatherer;
import com.shieldmechanics.Shieldmechanics;
import com.shieldmechanics.enchant.*;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Handler to catch server tick events
 */
public class EventHandler
{
    private static DamageSource source;
    private static float        amount;
    private static Entity       current;

    @SubscribeEvent
    public static void onEntityAttack(final LivingAttackEvent event)
    {
        if (event.getEntityLiving() == event.getSource().getEntity())
        {
            return;
        }

        if ((event.getAmount() > 0.0F && event.getEntityLiving().isDamageSourceBlocked(event.getSource()) && Shieldmechanics.isShield(event.getEntityLiving()
          .getItemInHand(event.getEntityLiving().getUsedItemHand())
          .getItem())))
        {
            source = event.getSource();
            amount = event.getAmount();
            current = event.getEntity();
        }
    }

    @SubscribeEvent
    public static void onEntityAttack(final LivingHurtEvent event)
    {
        if (!(event.getEntity() instanceof Player) && Shieldmechanics.config.getCommonConfig().playerOnly.get())
        {
            return;
        }


        final ItemStack shieldItem = event.getEntityLiving().getItemInHand(event.getEntityLiving().getUsedItemHand());

        //Blocking
        if (event.getEntity() == current && event.getAmount() == 0 && event.getSource() == source)
        {
            // Knockback enchant
            final Entity sourceEntity = event.getSource().getDirectEntity();

            if (sourceEntity instanceof LivingEntity && current != null)
            {
                if (Shieldmechanics.rand.nextInt(KnockBackEnchant.KOCKBACK_CHANCE) == 0
                      && EnchantmentHelper.getItemEnchantmentLevel(Enchants.knockBackEnchant, shieldItem) > 0)
                {
                    ((LivingEntity) sourceEntity).knockback(1.0F, current.getX() - sourceEntity.getX(), current.getZ() - sourceEntity.getZ());
                }

                if (Shieldmechanics.rand.nextInt(SlownessEnchant.APPLY_CHANCE) == 0
                      && EnchantmentHelper.getItemEnchantmentLevel(Enchants.slownessEnchant, shieldItem) > 0)
                {
                    ((LivingEntity) sourceEntity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100));
                }

                if (Shieldmechanics.rand.nextInt(BlindEnchant.APPLY_CHANCE) == 0
                      && EnchantmentHelper.getItemEnchantmentLevel(Enchants.blindEnchant, shieldItem) > 0)
                {
                    ((LivingEntity) sourceEntity).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100));
                }

                if (event.getEntityLiving().getHealth() < 10 && event.getEntityLiving().getAbsorptionAmount() == 0
                      && Shieldmechanics.rand.nextInt(LastResortEnchant.APPLY_CHANCE) == 0
                      && EnchantmentHelper.getItemEnchantmentLevel(Enchants.lastResortEnchant, shieldItem) > 0)
                {
                    event.getEntityLiving().setAbsorptionAmount(6);
                }
            }

            // BLock case
            if (event.getEntityLiving() instanceof Player && Shieldmechanics.config.getCommonConfig().blockCooldown.get() > 0)
            {
                ((Player) event.getEntityLiving()).getCooldowns().addCooldown(shieldItem.getItem(), Shieldmechanics.config.getCommonConfig().blockCooldown.get());
            }

            event.setAmount(amount * ShieldDataGatherer.getBlockDamageReductionFor(shieldItem));
        }
        //Nonblocking
        else if (Shieldmechanics.isShield(shieldItem))
        {
            // No block mainhand
            event.setAmount(event.getAmount() * ShieldDataGatherer.getHoldDamageReductionFor(shieldItem));
        }
    }
}
