package net.obf.weather2compat.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.obf.weather2compat.CompatUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Level.class)
public abstract class LevelCompatMixin {
    @Redirect(
            method = "isRainingAt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isRaining()Z"
            )
    )
    private boolean weather2compat$isRaining(
            Level level,
            @Local(argsOnly = true) BlockPos pos
    ) {
        return CompatUtils.isStormAbove(
                CompatUtils.RAIN_STORM,
                level, pos
        );
    }
}
