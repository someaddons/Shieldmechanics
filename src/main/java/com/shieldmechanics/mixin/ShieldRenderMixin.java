package com.shieldmechanics.mixin;

import com.shieldmechanics.Shieldmechanics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemInHandRenderer.class, priority = 10000)
public class ShieldRenderMixin
{
    @Inject(method = "evaluateWhichHandsToRender", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private static void checkHideShield(final LocalPlayer player, final CallbackInfoReturnable<ItemInHandRenderer.HandRenderSelection> cir)
    {
        if (Shieldmechanics.isShield(player.getOffhandItem()) && Shieldmechanics.config.getCommonConfig().hideinactiveshield && !(player.isUsingItem()
            && player.getUsedItemHand() == InteractionHand.OFF_HAND))
        {
            cir.setReturnValue(ItemInHandRenderer.HandRenderSelection.RENDER_MAIN_HAND_ONLY);
        }
    }
}
