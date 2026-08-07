package net.obf.weather2compat;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import weather2.ServerTickHandler;
import weather2.weathersystem.storm.StormObject;
import weather2.weathersystem.storm.WeatherObject;

public final class CompatUtils {
    private CompatUtils() {}

    public static boolean isRainAbove(@NotNull Level level, @NotNull Vec3 pos) {
        return isRainAbove(level, pos.x, pos.y, pos.z);
    }

    public static boolean isRainAbove(@NotNull Level level, @NotNull BlockPos pos) {
        return isRainAbove(level, pos.getX(), pos.getY(), pos.getZ());
    }

    public static boolean isRainAbove(@NotNull Level level, double x, double y, double z) {
        // TODO: can getWeatherManagerFor return null?
        // TODO: why does getStormObjects return WeatherObject if it only has StormObjects?
        for (WeatherObject wo : ServerTickHandler.getWeatherManagerFor(level).getStormObjects()) {
            StormObject so = (StormObject) wo;

            if (so.isDead || !so.attrib_precipitation)
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
