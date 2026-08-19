package net.obf.weather2compat.weather2.sereneseasons.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.obf.weather2compat.weather2.Weather2Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sereneseasons.season.SeasonHooks;

@Mixin(SeasonHooks.class)
public abstract class SeasonHooksMixin {
    @Redirect(
            method = "isRainingAtHook",
            require = 1,
            allow = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isRaining()Z"
            )
    )
    private static boolean weather2compat$isRaining(
            Level level,
            @Local(argsOnly = true) BlockPos pos
    ) {
        return Weather2Utils.isStormAbove(
                Weather2Utils.RAIN_STORM,
                level, pos
        );
    }
}
