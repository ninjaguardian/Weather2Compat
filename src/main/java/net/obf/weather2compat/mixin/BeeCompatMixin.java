package net.obf.weather2compat.mixin;

import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import weather2.ServerTickHandler;

@Mixin(Bee.class)
public abstract class BeeCompatMixin {
    @Redirect(
            method = "wantsToEnterHive",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isRaining()Z"
            )
    )
    private boolean weather2compat$isRaining(Level level) {
        return ServerTickHandler.getWeatherManagerFor(level).isPrecipitatingAt(
                ((Bee) (Object) this).blockPosition()
        );
    }
}
