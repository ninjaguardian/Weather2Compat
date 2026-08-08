package net.obf.weather2compat.weather2.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import net.obf.weather2compat.weather2.Weather2Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin {
    /**
     * Always make it check if it can spawn phantoms.
     * Sky darkness is checked later once BlockPos is available (in {@link #weather2compat$shouldSpawnPhantoms}).
     */
    @Redirect(
            method = "tick",
            require = 1,
            allow = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;getSkyDarken()I"
            )
    )
    private int weather2compat$getSkyDarken(ServerLevel level) {
        return 5;
    }

    // TODO: change this to not need neoforge
    @ModifyExpressionValue(
            method = "tick",
            require = 1,
            allow = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/neoforge/event/entity/player/PlayerSpawnPhantomsEvent;shouldSpawnPhantoms(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)Z"
            )
    )
    private boolean weather2compat$shouldSpawnPhantoms(
            boolean original,
            @Local(argsOnly = true) ServerLevel level,
            @Local BlockPos pos
    ) {
        return Weather2Utils.getSkyDarken(level, pos) >= 5 && original;
    }
}
