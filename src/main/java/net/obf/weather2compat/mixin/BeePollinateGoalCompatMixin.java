package net.obf.weather2compat.mixin;

import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import net.obf.weather2compat.CompatUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.world.entity.animal.Bee$BeePollinateGoal")
public abstract class BeePollinateGoalCompatMixin {
    @Final
    @Shadow
    Bee this$0;

    @Redirect(
            method = {
                    "canBeeUse",
                    "canBeeContinueToUse"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isRaining()Z"
            )
    )
    private boolean weather2compat$isRaining(Level level) {
        return CompatUtils.isStormAbove(
                CompatUtils.RAIN_STORM,
                level, this$0.position()
        );
    }
}
