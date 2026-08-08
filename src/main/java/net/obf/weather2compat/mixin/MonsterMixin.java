package net.obf.weather2compat.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.Monster;
import net.obf.weather2compat.Weather2Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Monster.class)
public abstract class MonsterMixin {
    @Redirect(
            method = "isDarkEnoughToSpawn",
            require = 1,
            allow = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;isThundering()Z"
            )
    )
    private static boolean weather2compat$isThundering(
            ServerLevel level,
            @Local(argsOnly = true) BlockPos pos
    ) {
        return Weather2Utils.isStormAbove(
                Weather2Utils.THUNDER_STORM,
                level, pos
        );
    }
}
