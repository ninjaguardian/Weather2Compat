package net.obf.weather2compat.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.obf.weather2compat.MinecraftUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @Redirect(
            method = {
                    "lambda$startSleepInBed$12",
                    "lambda$startSleepInBed$13",
                    "lambda$startSleepInBed$14"
            },
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
