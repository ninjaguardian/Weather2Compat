package net.obf.weather2compat.weather2.mixin;

import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import net.obf.weather2compat.weather2.Weather2Utils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.world.entity.animal.Bee$BeePollinateGoal")
public abstract class BeePollinateGoalMixin {
    @Final
    @Shadow
    Bee this$0;

    @Redirect(
            method = {
                    "canBeeUse",
                    "canBeeContinueToUse"
            },
            require = 2,
            allow = 2,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isRaining()Z"
            )
    )
    private boolean weather2compat$isRaining(Level level) {
        return Weather2Utils.isStormAbove(
                Weather2Utils.RAIN_STORM,
                level, this$0.position()
        );
    }
}
