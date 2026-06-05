package com.shieldmechanics.event;

import com.cupboard.util.RegistryLookup;
import com.cupboard.util.ResourceLocation;
import com.shieldmechanics.ShieldDataGatherer;
import com.shieldmechanics.Shieldmechanics;
import com.shieldmechanics.enchant.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

import static com.shieldmechanics.enchant.Enchants.knockBackEnchant;
import static com.shieldmechanics.enchant.Enchants.slownessEnchant;

/**
 * Handler to catch server tick events
 */
public class EventHandler
{
    private static final AttributeModifier MAX_ABSORB_INC = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Shieldmechanics.MODID, "lastresort"), 6.0f,
        AttributeModifier.Operation.ADD_VALUE);

    @SubscribeEvent()
    public static void onShieldBLockEvent(LivingShieldBlockEvent event)
    {
        event.setBlockedDamage(onBLock(event.getDamageSource(), event.getBlockedDamage(), event.getEntity()));
    }

    @SubscribeEvent()
    public static void onDamage(LivingDamageEvent.Pre event)
    {
        if (!(event.getContainer().getBlockedDamage() > 0))
        {
            event.setNewDamage(calculatePassiveShieldDamage(event.getSource(), event.getNewDamage(), event.getEntity()));
        }
    }

    @SubscribeEvent()
    public static void onPostDamage(LivingDamageEvent.Post event)
    {
        if (event.getBlockedDamage() > 0)
        {
            afterBlock(event.getSource(), event.getEntity());
        }
    }

    /**
     * Block callback
     *
     * @param source
     * @param amount
     * @param entity
     * @param event
     * @return
     */
    public static float onBLock(final DamageSource source, final float amount, final LivingEntity entity)
    {
        if (entity == source.getEntity())
        {
            return amount;
        }

        final ItemStack usedItem = entity
            .getItemInHand(entity.getUsedItemHand());
        if (amount > 0.0F && Shieldmechanics.isShield(usedItem))
        {
            float damage = calculateBlockedDamage(amount, entity);
            if (damage < 3)
            {
                usedItem.hurtAndBreak(1, entity, entity.getUsedItemHand().asEquipmentSlot());
            }
            return damage;
        }

        return amount;
    }

    /**
     * Calculates blocked damage
     *
     * @param hurtamount
     * @param entity
     * @return
     */
    private static float calculateBlockedDamage(final float hurtamount, final LivingEntity entity)
    {
        if (!(entity instanceof Player) && Shieldmechanics.config.getCommonConfig().playerOnly)
        {
            return hurtamount;
        }

        final ItemStack shieldItem = entity.getItemInHand(entity.getUsedItemHand());

        // BLock case
        if (entity instanceof Player player && Shieldmechanics.config.getCommonConfig().blockCooldown > 0)
        {
            player.getCooldowns().addCooldown(shieldItem, Shieldmechanics.config.getCommonConfig().blockCooldown);
            player.stopUsingItem();
        }

        if (Shieldmechanics.config.getCommonConfig().debugLogging)
        {
            Shieldmechanics.LOGGER.warn("entity:" + entity.getDisplayName().getString() + " Shield block damage reduction to:" + ShieldDataGatherer.getBlockDamageReductionFor(
                entity.level(),
                shieldItem) +
                " prevDamage:" + hurtamount + " postDamage:" + (hurtamount * ShieldDataGatherer.getBlockDamageReductionFor(entity.level(), shieldItem)));
        }

        return hurtamount * ShieldDataGatherer.getBlockDamageReductionFor(entity.level(), shieldItem);
    }

    private static float calculatePassiveShieldDamage(final DamageSource source, final float damage, final LivingEntity entity)
    {
        if (source.getEntity() == null || !(entity instanceof Player) && Shieldmechanics.config.getCommonConfig().playerOnly)
        {
            return damage;
        }

        final ItemStack shieldItem = entity.getItemInHand(entity.getUsedItemHand());

        //Nonblocking
        if (Shieldmechanics.isShield(shieldItem))
        {
            // No block mainhand
            if (Shieldmechanics.config.getCommonConfig().debugLogging)
            {
                Shieldmechanics.LOGGER.warn(
                    "entity:" + entity.getDisplayName().getString() + " Shield block passive damage reduction to:" + ShieldDataGatherer.getHoldDamageReductionFor(shieldItem) +
                        " prevDamage:" + damage + " postDamage:" + (damage * ShieldDataGatherer.getHoldDamageReductionFor(shieldItem)));
            }
            return damage * ShieldDataGatherer.getHoldDamageReductionFor(shieldItem);
        }

        return damage;
    }

    private static void afterBlock(final DamageSource source, final LivingEntity entity)
    {
        if (!(entity instanceof Player) && Shieldmechanics.config.getCommonConfig().playerOnly)
        {
            return;
        }

        final ItemStack shieldItem = entity.getItemInHand(entity.getUsedItemHand());
        if (!Shieldmechanics.isShield(shieldItem))
        {
            return;
        }

        // Attackees effects
        if (entity.getHealth() < 10 && entity.getAbsorptionAmount() == 0
            && Shieldmechanics.rand.nextInt(LastResortEnchant.APPLY_CHANCE) == 0
            && EnchantmentHelper.getItemEnchantmentLevel(RegistryLookup.getHolder(entity.level(), Registries.ENCHANTMENT, Enchants.lastResortEnchant), shieldItem) > 0)
        {
            if (!entity.getAttributes().hasModifier(Attributes.MAX_ABSORPTION, MAX_ABSORB_INC.id()))
            {
                entity.getAttribute(Attributes.MAX_ABSORPTION).addTransientModifier(MAX_ABSORB_INC);
            }

            entity.setAbsorptionAmount(6.0f);
        }

        // Effects on the attacker
        final Entity sourceEntity = source.getEntity();
        if (sourceEntity instanceof LivingEntity)
        {
            if (Shieldmechanics.rand.nextInt(KnockBackEnchant.KOCKBACK_CHANCE) == 0
                && EnchantmentHelper.getItemEnchantmentLevel(RegistryLookup.getHolder(entity.level(), Registries.ENCHANTMENT, knockBackEnchant.identifier()), shieldItem) > 0)
            {
                ((LivingEntity) sourceEntity).knockback(1.0F, entity.getX() - sourceEntity.getX(), entity.getZ() - sourceEntity.getZ());
            }

            if (Shieldmechanics.rand.nextInt(SlownessEnchant.APPLY_CHANCE) == 0
                &&
                EnchantmentHelper.getItemEnchantmentLevel(RegistryLookup.getHolder(entity.level(), Registries.ENCHANTMENT, slownessEnchant),
                    shieldItem) > 0)
            {
                ((LivingEntity) sourceEntity).addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 100));
            }

            if (Shieldmechanics.rand.nextInt(BlindEnchant.APPLY_CHANCE) == 0
                && EnchantmentHelper.getItemEnchantmentLevel(RegistryLookup.getHolder(entity.level(), Registries.ENCHANTMENT, Enchants.blindEnchant), shieldItem) > 0)
            {
                ((LivingEntity) sourceEntity).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100));
            }
        }
    }
}
