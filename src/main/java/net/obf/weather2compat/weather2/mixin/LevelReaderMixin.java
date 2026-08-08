package net.obf.weather2compat.weather2.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.obf.weather2compat.weather2.Weather2Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelReader.class)
public interface LevelReaderMixin {
    @WrapOperation(
            method = "getMaxLocalRawBrightness(Lnet/minecraft/core/BlockPos;)I",
            require = 1,
            allow = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/LevelReader;getSkyDarken()I"
            )
    )
    private int weather2compat$getSkyDarken(
            LevelReader levelReader,
            Operation<Integer> original,
            @Local(argsOnly = true) BlockPos pos
    ) {
        if (levelReader instanceof Level level)
            return Weather2Utils.getSkyDarken(level, pos);

        return original.call(levelReader);
    }
}
