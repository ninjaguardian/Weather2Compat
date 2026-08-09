package net.obf.weather2compat;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
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

    /**
     * See {@link #isDaytime}
     */
    private static final double NIGHTTIME_START = Math.acos(3.0 / 44.0) / Math.TAU;

    /**
     * See {@link #isDaytime}
     */
    private static final double NIGHTTIME_END = 1.0 - NIGHTTIME_START;

    /**
     * {@link Level#isDay} looks like:
     * <pre>{@code
     * return !this.dimensionType().hasFixedTime() && this.getSkyDarken() < 4;
     * }</pre>
     * {@link net.minecraft.world.level.dimension.DimensionType#hasFixedTime} can be ignored for now.<br>
     * {@link Level#getSkyDarken} comes from {@link Level#updateSkyBrightness}.<br>
     * Ignoring rain and thunder for {@link Level#updateSkyBrightness}, the {@link Level#isDay} calculation looks roughly like:
     * <pre>{@code
     * var time = this.getTimeOfDay(1);
     * var d2 = clamp(cos(time * TAU), -0.25, 0.25);
     * d2 = 0.5 + 2 * d2;
     * return (int)((1 - d2) * 11) < 4;
     * }</pre>
     * To solve the inequality for {@code time}, ignore the {@code (int)} and divide by {@code 11}:
     * <pre>{@code
     * var d2 = clamp(cos(time * TAU), -0.25, 0.25);
     * d2 = 0.5 + 2 * d2;
     * return 1 - d2 < 4 / 11;
     * }</pre>
     * Subtract {@code 1} from both sides:
     * <pre>{@code
     * var d2 = clamp(cos(time * TAU), -0.25, 0.25);
     * d2 = 0.5 + 2 * d2;
     * return -d2 < -(7 / 11);
     * }</pre>
     * Multiply by {@code -1} (and flip the inequality):
     * <pre>{@code
     * var d2 = clamp(cos(time * TAU), -0.25, 0.25);
     * d2 = 0.5 + 2 * d2;
     * return d2 > 7 / 11;
     * }</pre>
     * Substitute {@code d2}:
     * <pre>{@code
     * var d2 = clamp(cos(time * TAU), -0.25, 0.25);
     * return 0.5 + 2 * d2 > 7 / 11;
     * }</pre>
     * Subtract {@code 0.5} and divide by {@code 2}:
     * <pre>{@code
     * var d2 = clamp(cos(time * TAU), -0.25, 0.25);
     * return d2 > 3 / 44;
     * }</pre>
     * Ignore the {@code clamp} because the threshold is within its range:
     * {@snippet :
     * -0.25 < 3/44 < 0.25
     * }
     * So at the points where the threshold is crossed:
     * <pre>{@code
     * var d2 = cos(time * TAU);
     * return d2 > 3 / 44;
     * }</pre>
     * Since cosine is greater than {@code 3 / 44} on both sides of the 0 / &tau; boundary:
     * <pre>{@code
     * return time * TAU < acos(3 / 44) || time * TAU > TAU - acos(3 / 44);
     * }</pre>
     * Divide by &tau;:
     * <pre>{@code
     * return time < acos(3 / 44) / TAU || time > 1 - acos(3 / 44) / TAU;
     * }</pre>
     * Extract a variable {@link #NIGHTTIME_START}:
     * <pre>{@code
     * return time < NIGHTTIME_START || time > 1 - NIGHTTIME_START;
     * }</pre>
     * And also {@link #NIGHTTIME_END}
     * <pre>{@code
     * return time < NIGHTTIME_START || time > NIGHTTIME_END;
     * }</pre>
     * Lastly, to match this method, add back the {@link net.minecraft.world.level.dimension.DimensionType#hasFixedTime} check:
     * <pre>{@code
     * float time = this.getTimeOfDay(1);
     * return !this.dimensionType().hasFixedTime() && (time < NIGHTTIME_START || time > NIGHTTIME_END);
     * }</pre>
     */
    public static boolean isDaytime(@NotNull Level level) {
        if (level.dimensionType().hasFixedTime())
            return false;

        float time = level.getTimeOfDay(1.0F);
        return time < NIGHTTIME_START || time > NIGHTTIME_END;
    }

    /**
     * See {@link #isDaytime}
     */
    public static boolean isNighttime(@NotNull Level level) {
        if (level.dimensionType().hasFixedTime())
            return false;

        float time = level.getTimeOfDay(1.0F);
        return time >= NIGHTTIME_START && time <= NIGHTTIME_END;
    }
}
