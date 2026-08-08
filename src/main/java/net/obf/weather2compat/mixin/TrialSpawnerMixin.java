package net.obf.weather2compat.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TrialSpawner.class)
public abstract class TrialSpawnerMixin {
    @WrapOperation(
            method = "ejectReward",
            require = 1,
            allow = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/storage/loot/LootParams$Builder;create(Lnet/minecraft/world/level/storage/loot/parameters/LootContextParamSet;)Lnet/minecraft/world/level/storage/loot/LootParams;"
            )
    )
    private LootParams weather2compat$addOrigin(
            LootParams.Builder builder,
            LootContextParamSet params,
            Operation<LootParams> original,
            @Local(argsOnly = true) BlockPos pos
    ) {
        builder.withParameter(LootContextParams.ORIGIN, pos.getCenter());
        return original.call(builder, params);
    }
}
