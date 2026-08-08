package net.obf.weather2compat.weather2.mixin;

import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.Level;
import net.obf.weather2compat.weather2.Weather2Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Fox.class)
public abstract class FoxMixin {
    // TODO: some foxes freeze when woken up by thunder

    @Redirect(
            method = "tick",
            require = 1,
            allow = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isThundering()Z"
            )
    )
    private boolean weather2compat$isThundering(Level level) {
        return Weather2Utils.isStormAbove(
                Weather2Utils.THUNDER_STORM, level,
                ((Fox) (Object) this).position()
        );
    }
}
