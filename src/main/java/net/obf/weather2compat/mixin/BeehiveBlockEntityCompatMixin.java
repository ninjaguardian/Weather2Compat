package net.obf.weather2compat.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import weather2.ServerTickHandler;

@Mixin(BeehiveBlockEntity.class)
public abstract class BeehiveBlockEntityCompatMixin {
    @Redirect(
            method = "releaseOccupant",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isRaining()Z"
            )
    )
    private static boolean weather2compat$isRaining(
            Level level,
            @Local(argsOnly = true, ordinal = 0) BlockPos pos
    ) {
        return ServerTickHandler.getWeatherManagerFor(level).isPrecipitatingAt(pos);
    }
}
