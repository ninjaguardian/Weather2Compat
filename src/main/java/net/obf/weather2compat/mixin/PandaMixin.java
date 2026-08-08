package net.obf.weather2compat.mixin;

import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.level.Level;
import net.obf.weather2compat.Weather2Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Panda.class)
public abstract class PandaMixin {
    @Redirect(
            method = {
                    "tick",
                    "isScared"
            },
            require = 2,
            allow = 2,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isThundering()Z"
            )
    )
    private boolean weather2compat$isThundering(Level level) {
        return Weather2Utils.isStormAbove(
                Weather2Utils.THUNDER_STORM, level,
                ((Panda) (Object) this).position()
        );
    }
}
