package net.obf.weather2compat.mixin;

import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.Level;
import net.obf.weather2compat.CompatUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Fox.class)
public abstract class FoxCompatMixin {
    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isThundering()Z"
            )
    )
    private boolean weather2compat$isThundering(Level level) {
        return CompatUtils.isStormAbove(
                CompatUtils.THUNDER_STORM, level,
                ((Fox) (Object) this).position()
        );
    }
}
