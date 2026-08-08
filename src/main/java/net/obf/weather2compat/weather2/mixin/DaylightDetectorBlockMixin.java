package net.obf.weather2compat.weather2.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DaylightDetectorBlock;
import net.obf.weather2compat.weather2.Weather2Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DaylightDetectorBlock.class)
public abstract class DaylightDetectorBlockMixin {
    @Redirect(
            method = "updateSignalStrength",
            require = 1,
            allow = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getSkyDarken()I"
            )
    )
    private static int weather2compat$getSkyDarken(
            Level level,
            @Local(argsOnly = true) BlockPos pos
    ) {
        return Weather2Utils.getSkyDarken(level, pos);
    }
}
