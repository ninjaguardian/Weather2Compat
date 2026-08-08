package net.obf.weather2compat.weather2.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.level.Level;
import net.obf.weather2compat.weather2.Weather2Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WanderingTrader.class)
public abstract class WanderingTraderMixin {
    @Redirect(
            method = {
                    "lambda$registerGoals$0",
                    "lambda$registerGoals$1"
            },
            require = 1,
            allow = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isDay()Z"
            )
    )
    private boolean weather2compat$isDay(
            Level level,
            @Local(argsOnly = true) WanderingTrader trader
    ) {
        return Weather2Utils.isDay(
                level,
                trader.position()
        );
    }

    @Redirect(
            method = {
                    "lambda$registerGoals$0",
                    "lambda$registerGoals$1"
            },
            require = 1,
            allow = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isNight()Z"
            )
    )
    private boolean weather2compat$isNight(
            Level level,
            @Local(argsOnly = true) WanderingTrader trader
    ) {
        return Weather2Utils.isNight(
                level,
                trader.position()
        );
    }
}
