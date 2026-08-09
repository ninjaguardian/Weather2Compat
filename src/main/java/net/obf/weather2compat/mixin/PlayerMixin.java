package net.obf.weather2compat.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.obf.weather2compat.MinecraftUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public abstract class PlayerMixin {
    // TODO: is this neoforge only?
    @Redirect(
            method = "tick",
            require = 1,
            allow = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isDay()Z"
            )
    )
    private boolean weather2compat$isDay(Level level) {
        return MinecraftUtils.isDaytime(level);
    }
}
