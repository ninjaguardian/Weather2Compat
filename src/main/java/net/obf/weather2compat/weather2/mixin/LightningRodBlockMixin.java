package net.obf.weather2compat.weather2.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LightningRodBlock;
import net.obf.weather2compat.weather2.Weather2Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightningRodBlock.class)
public abstract class LightningRodBlockMixin {
    @Redirect(
            method = "animateTick",
            require = 1,
            allow = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isThundering()Z"
            )
    )
    private boolean weather2compat$isThundering(
            Level level,
            @Local(argsOnly = true) BlockPos pos
    ) {
        return Weather2Utils.isStormAbove(
                Weather2Utils.THUNDER_STORM,
                level, pos
        );
    }
}
