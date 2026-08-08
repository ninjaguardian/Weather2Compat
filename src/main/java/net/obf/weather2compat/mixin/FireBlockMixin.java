package net.obf.weather2compat.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.FireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FireBlock.class)
public abstract class FireBlockMixin {
    /**
     * Both calls check {@code isNearRain}, which checks {@link ServerLevel#isRainingAt}
     */
    @Redirect(
            method = "tick",
            require = 2,
            allow = 2,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;isRaining()Z"
            )
    )
    private boolean weather2compat$isRaining(ServerLevel level) {
        return true;
    }
}
