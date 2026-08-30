package net.obf.weather2compat.weather2;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import weather2.ClientTickHandler;
import weather2.ServerTickHandler;
import weather2.config.ConfigMisc;
import weather2.weathersystem.WeatherManager;
import weather2.weathersystem.storm.StormObject;
import weather2.weathersystem.storm.WeatherObject;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

import static weather2.weathersystem.storm.StormObject.STATE_THUNDER;

@ParametersAreNonnullByDefault
public final class Weather2Utils {
    private Weather2Utils() {}

    public static final Predicate<StormObject> RAIN_STORM =
            so -> so.attrib_precipitation;

    public static final Predicate<StormObject> THUNDER_STORM =
            so -> so.levelCurIntensityStage >= STATE_THUNDER
                    && !so.isBaby()
                    && !so.isPet();

    @Nullable
    private static WeatherManager getWeatherManager(Level level) {
        return level.isClientSide() ? ClientTickHandler.getClientWeather() : ServerTickHandler.getWeatherManagerFor(level);
    }

    public static boolean isStormAbove(Predicate<StormObject> predicate, Level level, Vec3 pos) {
        return isStormAbove(predicate, level, pos.x, pos.z);
    }

    public static boolean isStormAbove(Predicate<StormObject> predicate, Level level, BlockPos pos) {
        return isStormAbove(predicate, level, pos.getX(), pos.getZ());
    }

    public static boolean isStormAbove(Predicate<StormObject> predicate, Level level, double x, double z) {
        WeatherManager manager = getWeatherManager(level);
        return manager != null && containsStorm(manager, predicate, x, z);
    }

    private static boolean containsStorm(WeatherManager manager, Predicate<StormObject> predicate, double x, double z) {
        for (WeatherObject wo : manager.getStormObjects()) {
            if (!(wo instanceof StormObject so))
                continue;

            if (so.isDead || !predicate.test(so))
                continue;

            double dx = so.pos.x - x;
            double dz = so.pos.z - z;

            if (dx * dx + dz * dz < (double) so.size * so.size)
                return true;
        }

        return false;
    }

    public static float getPrecipitationStrength(Predicate<StormObject> predicate, WeatherManager manager, double x, double z) {
        float combined = 1.0F;

        if (ConfigMisc.overcastMode)
            combined -= manager.vanillaRainAmountOnServer;

        for (WeatherObject wo : manager.getStormObjects()) {
            if (!(wo instanceof StormObject so))
                continue;

            if (so.isDead || !predicate.test(so))
                continue;

            double dx = so.pos.x - x;
            double dz = so.pos.z - z;
            double distance = dx * dx + dz * dz;
            double size = so.size;

            if (distance >= size * size)
                continue;

            float factor = (float) (Math.sqrt(distance) / size);
            factor = Math.max(factor * 1.1F - 0.1F, 0.0F);

            if (so.levelCurIntensityStage == StormObject.STATE_NORMAL && factor < 0.7F)
                factor = 0.7F;

            combined *= factor;
        }

        return 1.0F - combined;
    }

    public static int getSkyDarken(Level level, double x, double z) {
        WeatherManager manager = getWeatherManager(level);

        float rainLevel = 0.0F;
        float thunderLevel = 0.0F;
        if (manager != null) {
            rainLevel = getPrecipitationStrength(RAIN_STORM, manager, x, z);
            thunderLevel = getPrecipitationStrength(THUNDER_STORM, manager, x, z);
        }

        float d0 = 1.0F - rainLevel * 5.0F / 16.0F;
        float d1 = 1.0F - thunderLevel * 5.0F / 16.0F;
        float d2 = 0.5F + 2.0F * Mth.clamp(Mth.cos(level.getTimeOfDay(1.0F) * Mth.TWO_PI), -0.25F, 0.25F);
        return Math.round((1.0F - d2 * d0 * d1) * 11.0F);
    }

    public static int getSkyDarken(Level level, BlockPos pos) {
        return getSkyDarken(level, pos.getX(), pos.getZ());
    }

    public static boolean isDay(Level level, double x, double z) {
        return !level.dimensionType().hasFixedTime() && getSkyDarken(level, x, z) < 4;
    }

    public static boolean isDay(Level level, Vec3 pos) {
        return isDay(level, pos.x, pos.z);
    }

    public static boolean isNight(Level level, double x, double z) {
        return !level.dimensionType().hasFixedTime() && getSkyDarken(level, x, z) >= 4;
    }

    public static boolean isNight(Level level, Vec3 pos) {
        return isNight(level, pos.x, pos.z);
    }
}