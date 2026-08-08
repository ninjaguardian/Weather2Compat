package net.obf.weather2compat.weather2.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.animal.Fox;
import net.obf.weather2compat.weather2.Weather2Utils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.entity.animal.Fox$SleepGoal")
public abstract class SleepGoalMixin {
    @Final
    @Shadow
    Fox this$0;

    @ModifyExpressionValue(
            method = "canSleep",
            require = 1,
            allow = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isDay()Z"
            )
    )
    private boolean weather2compat$canSleep(boolean isDay) {
        return isDay && !Weather2Utils.isStormAbove(
                Weather2Utils.THUNDER_STORM,
                this$0.level(), this$0.position()
        );
    }
}
