package net.obf.weather2compat.weather2.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.obf.weather2compat.weather2.Weather2Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    @Redirect(
            method = "tickPrecipitation",
            require = 1,
            allow = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;isRaining()Z"
            )
    )
    private boolean weather2compat$isRaining(
            ServerLevel level,
            @Local(ordinal = 1) BlockPos pos
    ) {
        return Weather2Utils.isStormAbove(
                Weather2Utils.RAIN_STORM,
                level, pos
        );
    }

    /**
     * Weather2 has its own lightning system
     */
    @Redirect(
            method = "tickChunk",
            require = 1,
            allow = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;isRaining()Z"
            )
    )
    private boolean weather2compat$isRaining(ServerLevel level) {
        return false;
    }

    /**
     * Weather2 has its own lightning system
     */
    @Redirect(
            method = "tickChunk",
            require = 1,
            allow = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;isThundering()Z"
            )
    )
    private boolean weather2compat$isThundering(ServerLevel level) {
        return false;
    }
}
