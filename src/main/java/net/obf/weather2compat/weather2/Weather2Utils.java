package net.obf.weather2compat.weather2;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import weather2.ServerTickHandler;
import weather2.weathersystem.storm.StormObject;
import weather2.weathersystem.storm.WeatherObject;

import java.util.function.Predicate;

import static weather2.weathersystem.storm.StormObject.STATE_THUNDER;

// TODO: can getWeatherManagerFor return null?
// TODO: why does getStormObjects return WeatherObject if it only has StormObjects?
// TODO: are weather2 storms spherical?

public final class Weather2Utils {
    private Weather2Utils() {}

    public static final Predicate<StormObject> RAIN_STORM =
            so -> so.attrib_precipitation; // TODO: check specifics

    public static final Predicate<StormObject> THUNDER_STORM =
            so -> so.levelCurIntensityStage >= STATE_THUNDER
                    && !so.isBaby()
                    && !so.isPet();

    public static boolean isStormAbove(
            @NotNull Predicate<StormObject> predicate,
            @NotNull Level level,
            @NotNull Vec3 pos
    ) {
        return isStormAbove(predicate, level, pos.x, pos.y, pos.z);
    }

    public static boolean isStormAbove(
            @NotNull Predicate<StormObject> predicate,
            @NotNull Level level,
            @NotNull BlockPos pos
    ) {
        return isStormAbove(predicate, level, pos.getX(), pos.getY(), pos.getZ());
    }

    private static boolean isStormAbove(
            @NotNull Predicate<StormObject> predicate,
            @NotNull Level level,
            double x,
            double y,
            double z
    ) {
        for (WeatherObject wo : ServerTickHandler.getWeatherManagerFor(level).getStormObjects()) {
            StormObject so = (StormObject) wo;

            if (so.isDead || !predicate.test(so))
                continue;

            double dx = so.pos.x - x;
            double dy = so.pos.y - y;
            double dz = so.pos.z - z;

            if (dx * dx + dy * dy + dz * dz < so.size * so.size)
                return true;
        }

        return false;
    }
}
