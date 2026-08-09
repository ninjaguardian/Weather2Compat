package net.obf.weather2compat.weather2;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import weather2.ServerTickHandler;
import weather2.config.ConfigMisc;
import weather2.weathersystem.WeatherManagerServer;
import weather2.weathersystem.storm.StormObject;
import weather2.weathersystem.storm.WeatherObject;

import java.util.function.Predicate;

import static weather2.weathersystem.storm.StormObject.STATE_THUNDER;

// TODO: can getWeatherManagerFor return null?
// TODO: why does getStormObjects return WeatherObject if it only has StormObjects?
// TODO: are weather2 storms spherical?
// TODO: should i search at y=static_YPos_layer0?
// TODO: should i include y in weather search?

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
        return isStormAbove(predicate, level, pos.x, pos.z);
    }

    public static boolean isStormAbove(
            @NotNull Predicate<StormObject> predicate,
            @NotNull Level level,
            @NotNull BlockPos pos
    ) {
        return isStormAbove(predicate, level, pos.getX(), pos.getZ());
    }

    private static boolean isStormAbove(
            @NotNull Predicate<StormObject> predicate,
            @NotNull Level level,
            double x, double z
    ) {
        for (WeatherObject wo : ServerTickHandler.getWeatherManagerFor(level).getStormObjects()) {
            StormObject so = (StormObject) wo;

            if (so.isDead || !predicate.test(so))
                continue;

            double dx = so.pos.x - x;
            double dz = so.pos.z - z;

            if (dx * dx + dz * dz < so.size * so.size)
                return true;
        }

        return false;
    }

    private static float getPrecipitationStrength(
            @NotNull Predicate<StormObject> predicate,
            @NotNull WeatherManagerServer manager,
            double x, double z
    ) {
        float combined = 1.0F;

        if (ConfigMisc.overcastMode)
            combined -= manager.vanillaRainAmountOnServer;

        for (WeatherObject wo : manager.getStormObjects()) {
            StormObject so = (StormObject) wo;
            if (so.isDead || !predicate.test(so))
                continue;

            double dx = so.pos.x - x;
            double dz = so.pos.z - z;
            double distance = dx * dx + dz * dz;

            if (distance >= so.size * so.size)
                continue;

            float factor = (float)(Math.sqrt(distance) / so.size);

            if (so.levelCurIntensityStage == StormObject.STATE_NORMAL && factor < 0.7F)
                factor = 0.7F;

            combined *= factor;
        }

        return 1.0F - combined;
    }

    private static int getSkyDarken(
            @NotNull Level level,
            double x, double z
    ) {
        WeatherManagerServer manager = ServerTickHandler.getWeatherManagerFor(level);
        float d0 = 1.0F - getPrecipitationStrength(RAIN_STORM, manager, x, z) * 5.0F / 16.0F;
        float d1 = 1.0F - getPrecipitationStrength(THUNDER_STORM, manager, x, z) * 5.0F / 16.0F;
        float d2 = 0.5F + 2.0F * Mth.clamp(Mth.cos(level.getTimeOfDay(1.0F) * Mth.TWO_PI), -0.25F, 0.25F);
        return Math.round((1.0F - d2 * d0 * d1) * 11.0F);
    }

    public static int getSkyDarken(
            @NotNull Level level,
            @NotNull BlockPos pos
    ) {
        return getSkyDarken(level, pos.getX(), pos.getZ());
    }

    private static boolean isDay(
            @NotNull Level level,
            double x, double z
    ) {
        return !level.dimensionType().hasFixedTime() && getSkyDarken(level, x, z) < 4;
    }

    public static boolean isDay(
            @NotNull Level level,
            @NotNull Vec3 pos
    ) {
        return isDay(level, pos.x, pos.z);
    }

    private static boolean isNight(
            @NotNull Level level,
            double x, double z
    ) {
        return !level.dimensionType().hasFixedTime() && getSkyDarken(level, x, z) >= 4;
    }

    public static boolean isNight(
            @NotNull Level level,
            @NotNull Vec3 pos
    ) {
        return isNight(level, pos.x, pos.z);
    }
}
