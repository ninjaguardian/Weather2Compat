package net.obf.weather2compat.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.obf.weather2compat.Weather2Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BeehiveBlockEntity.class)
public abstract class BeehiveBlockEntityMixin {
    @Redirect(
            method = "releaseOccupant",
            require = 1,
            allow = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isRaining()Z"
            )
    )
    private static boolean weather2compat$isRaining(
            Level level,
            @Local(argsOnly = true, ordinal = 0) BlockPos pos
    ) {
        return Weather2Utils.isStormAbove(
                Weather2Utils.RAIN_STORM,
                level, pos
        );
    }
}
