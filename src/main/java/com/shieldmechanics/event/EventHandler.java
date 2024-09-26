package com.shieldmechanics.event;

import com.shieldmechanics.ShieldDataGatherer;
import com.shieldmechanics.Shieldmechanics;
import com.shieldmechanics.enchant.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
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
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import static com.shieldmechanics.enchant.Enchants.knockBackEnchant;
import static com.shieldmechanics.enchant.Enchants.slownessEnchant;
import static net.minecraft.world.entity.player.Player.DATA_PLAYER_ABSORPTION_ID;

/**
 * Handler to catch server tick events
 */
public class EventHandler
{
    private static DamageSource source;
    private static float        amount;
    private static Entity       current;
    private static AttributeModifier MAX_ABSORB_INC = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Shieldmechanics.MODID, "lastresort"), 6.0f,
      AttributeModifier.Operation.ADD_VALUE);

    @SubscribeEvent
    public static void onEntityAttack(final LivingIncomingDamageEvent event)
    {
        if (event.getEntity() == event.getSource().getEntity())
        {
            return;
        }

        if ((event.getAmount() > 0.0F && event.getEntity().isDamageSourceBlocked(event.getSource()) && Shieldmechanics.isShield(event.getEntity()
                                                                                                                                  .getItemInHand(event.getEntity()
                                                                                                                                                   .getUsedItemHand()))))
        {
            source = event.getSource();
            amount = event.getAmount();
            current = event.getEntity();
        }
    }

    @SubscribeEvent
    public static void onEntityAttack(final LivingDamageEvent.Pre event)
    {
        if (!(event.getEntity() instanceof Player) && Shieldmechanics.config.getCommonConfig().playerOnly)
        {
            return;
        }


        final ItemStack shieldItem = event.getEntity().getItemInHand(event.getEntity().getUsedItemHand());

        //Blocking
        if (event.getEntity() == current && event.getNewDamage() == 0 && event.getSource() == source)
        {
            // Knockback enchant
            final Entity sourceEntity = event.getSource().getDirectEntity();

            if (sourceEntity instanceof LivingEntity && current != null)
            {
                if (Shieldmechanics.rand.nextInt(KnockBackEnchant.KOCKBACK_CHANCE) == 0
                      && EnchantmentHelper.getItemEnchantmentLevel(event.getEntity()
                                                                     .level()
                                                                     .registryAccess()
                                                                     .registry(Registries.ENCHANTMENT)
                                                                     .get()
                                                                     .getHolderOrThrow(knockBackEnchant), shieldItem) > 0)
                {
                    ((LivingEntity) sourceEntity).knockback(1.0F, current.getX() - sourceEntity.getX(), current.getZ() - sourceEntity.getZ());
                }

                if (Shieldmechanics.rand.nextInt(SlownessEnchant.APPLY_CHANCE) == 0
                      &&
                      EnchantmentHelper.getItemEnchantmentLevel(event.getEntity().level().registryAccess().registry(Registries.ENCHANTMENT).get().getHolderOrThrow(slownessEnchant),
                        shieldItem) > 0)
                {
                    ((LivingEntity) sourceEntity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100));
                }

                if (Shieldmechanics.rand.nextInt(BlindEnchant.APPLY_CHANCE) == 0
                      && EnchantmentHelper.getItemEnchantmentLevel(event.getEntity()
                                                                     .level()
                                                                     .registryAccess()
                                                                     .registry(Registries.ENCHANTMENT)
                                                                     .get()
                                                                     .getHolderOrThrow(Enchants.blindEnchant), shieldItem) > 0)
                {
                    ((LivingEntity) sourceEntity).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100));
                }

                if (event.getEntity().getHealth() < 10 && event.getEntity().getAbsorptionAmount() == 0
                      && Shieldmechanics.rand.nextInt(LastResortEnchant.APPLY_CHANCE) == 0
                      && EnchantmentHelper.getItemEnchantmentLevel(event.getEntity()
                                                                     .level()
                                                                     .registryAccess()
                                                                     .registry(Registries.ENCHANTMENT)
                                                                     .get()
                                                                     .getHolderOrThrow(Enchants.lastResortEnchant), shieldItem) > 0)
                {
                    if (!event.getEntity().getAttributes().hasModifier(Attributes.MAX_ABSORPTION, MAX_ABSORB_INC.id()))
                    {
                        event.getEntity().getAttribute(Attributes.MAX_ABSORPTION).addTransientModifier(MAX_ABSORB_INC);
                    }

                    event.getEntity().setAbsorptionAmount(6.0f);
                }
            }

            // BLock case
            if (event.getEntity() instanceof Player && Shieldmechanics.config.getCommonConfig().blockCooldown > 0)
            {
                ((Player) event.getEntity()).getCooldowns().addCooldown(shieldItem.getItem(), Shieldmechanics.config.getCommonConfig().blockCooldown);
            }

            event.setNewDamage(amount * ShieldDataGatherer.getBlockDamageReductionFor(event.getEntity().level(), shieldItem));
        }
        //Nonblocking
        else if (Shieldmechanics.isShield(shieldItem))
        {
            // No block mainhand
            event.setNewDamage(event.getNewDamage() * ShieldDataGatherer.getHoldDamageReductionFor(shieldItem));
        }
    }
}
