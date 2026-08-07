package net.obf.weather2compat.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.obf.weather2compat.CompatUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerLevel.class)
public abstract class ServerLevelCompatMixin {
    @Redirect(
            method = "tickPrecipitation",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;isRaining()Z"
            )
    )
    private boolean weather2compat$isRaining(
            ServerLevel level,
            @Local(ordinal = 1) BlockPos pos
    ) {
        return CompatUtils.isStormAbove(
                CompatUtils.RAIN_STORM,
                level, pos
        );
    }
}
