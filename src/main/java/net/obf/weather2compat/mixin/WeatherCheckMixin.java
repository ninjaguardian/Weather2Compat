package net.obf.weather2compat.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.WeatherCheck;
import net.minecraft.world.phys.Vec3;
import net.obf.weather2compat.Weather2Utils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WeatherCheck.class)
public abstract class WeatherCheckMixin {
    @WrapOperation(
            method = "test(Lnet/minecraft/world/level/storage/loot/LootContext;)Z",
            require = 1,
            allow = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;isRaining()Z"
            )
    )
    private boolean weather2compat$isRaining(
            ServerLevel level,
            Operation<Boolean> original,
            @Local(argsOnly = true) LootContext context
    ) {
        @Nullable Vec3 pos = context.getParamOrNull(LootContextParams.ORIGIN);

        if (pos == null)
            return original.call(level);

        return Weather2Utils.isStormAbove(
                Weather2Utils.RAIN_STORM,
                level, pos
        );
    }

    @WrapOperation(
            method = "test(Lnet/minecraft/world/level/storage/loot/LootContext;)Z",
            require = 1,
            allow = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;isThundering()Z"
            )
    )
    private boolean weather2compat$isThundering(
            ServerLevel level,
            Operation<Boolean> original,
            @Local(argsOnly = true) LootContext context
    ) {
        @Nullable Vec3 pos = context.getParamOrNull(LootContextParams.ORIGIN);

        if (pos == null)
            return original.call(level);

        return Weather2Utils.isStormAbove(
                Weather2Utils.THUNDER_STORM,
                level, pos
        );
    }
}
