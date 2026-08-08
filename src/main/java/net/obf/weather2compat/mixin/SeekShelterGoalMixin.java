package net.obf.weather2compat.mixin;

import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.Level;
import net.obf.weather2compat.Weather2Utils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.world.entity.animal.Fox$SeekShelterGoal")
public abstract class SeekShelterGoalMixin {
    @Final
    @Shadow
    Fox this$0;

    @Redirect(
            method = "canUse",
            require = 1,
            allow = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isThundering()Z"
            )
    )
    private boolean weather2compat$isThundering(Level level) {
        return Weather2Utils.isStormAbove(
                Weather2Utils.THUNDER_STORM,
                level, this$0.position()
        );
    }
}
