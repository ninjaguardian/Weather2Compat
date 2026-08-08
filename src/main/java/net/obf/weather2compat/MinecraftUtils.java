package net.obf.weather2compat;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MinecraftUtils {
    private MinecraftUtils() {}

    @Nullable
    public static Vec3 getPos(@NotNull LootContext context) {
        @Nullable Vec3 pos = context.getParamOrNull(LootContextParams.ORIGIN);
        if (pos != null)
            return pos;

        @Nullable BlockEntity blockEntity = context.getParamOrNull(LootContextParams.BLOCK_ENTITY);
        if (blockEntity != null)
            return blockEntity.getBlockPos().getCenter();

        @Nullable Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity != null)
            return entity.position();

        return null;
    }
}
