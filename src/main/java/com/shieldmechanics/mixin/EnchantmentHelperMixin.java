package com.shieldmechanics.mixin;

import com.shieldmechanics.Shieldmechanics;
import com.shieldmechanics.enchant.Enchants;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin
{
    @Inject(method = "getAvailableEnchantmentResults", at = @At("HEAD"), cancellable = true)
    private static void getAvailableShieldEnchants(
      final int cost,
      final ItemStack stack,
      final Stream<Holder<Enchantment>> enchantStream,
      final CallbackInfoReturnable<List<EnchantmentInstance>> cir)
    {
        if (Shieldmechanics.isShield(stack))
        {
            List<EnchantmentInstance> enchants = new ArrayList<>();
            enchantStream.forEach(holder ->
            {
                final Enchantment enchantment = holder.value();
                if (Enchants.ALL.contains(holder.unwrapKey().get()) || enchantment.isPrimaryItem(stack))
                {
                    for (int i = enchantment.getMaxLevel(); i >= enchantment.getMinLevel(); i--)
                    {
                        if (cost >= enchantment.getMinCost(i) && cost <= enchantment.getMaxCost(i))
                        {
                            enchants.add(new EnchantmentInstance(holder, i));
                            break;
                        }
                    }
                }
            });

            cir.setReturnValue(enchants);
        }
    }
}
