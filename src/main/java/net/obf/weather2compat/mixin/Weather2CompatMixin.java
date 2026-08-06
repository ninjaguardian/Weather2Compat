package net.obf.weather2compat.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import weather2.ServerTickHandler;

@Mixin(Level.class)
public class Weather2CompatMixin {
    @Redirect(
            method = "isRainingAt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isRaining()Z"
            )
    )
    private boolean isRaining(Level instance, @Local(argsOnly = true) BlockPos pos) {
        return ServerTickHandler.getWeatherManagerFor(instance).isPrecipitatingAt(pos);
    }
}
