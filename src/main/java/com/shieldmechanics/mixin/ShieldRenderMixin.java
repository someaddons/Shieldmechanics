package com.shieldmechanics.mixin;

import com.shieldmechanics.Shieldmechanics;
import net.minecraft.client.player.FirstPersonHandsAndItems;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FirstPersonHandsAndItems.class, priority = 10000)
public class ShieldRenderMixin
{
    @Inject(method = "evaluateWhichHandsToRender", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private static void checkHideShield(final LocalPlayer player, final CallbackInfoReturnable<FirstPersonHandsAndItemsRenderState.HandRenderSelection> cir)
    {
        if ((cir.getReturnValue() == FirstPersonHandsAndItemsRenderState.HandRenderSelection.RENDER_BOTH_HANDS
            || cir.getReturnValue() == FirstPersonHandsAndItemsRenderState.HandRenderSelection.RENDER_OFF_HAND_ONLY)
            && Shieldmechanics.isShield(player.getOffhandItem()) && Shieldmechanics.config.getCommonConfig().hideinactiveshield && !(player.isUsingItem()
            && player.getUsedItemHand() == InteractionHand.OFF_HAND))
        {
            cir.setReturnValue(FirstPersonHandsAndItemsRenderState.HandRenderSelection.RENDER_MAIN_HAND_ONLY);
        }
    }
}
