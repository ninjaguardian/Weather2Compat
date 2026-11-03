package net.obf.weather2compat.weather2.mixin;

import net.minecraft.world.phys.Vec3;
import net.obf.weather2compat.weather2.Weather2Utils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import weather2.weathersystem.WeatherManager;
import weather2.weathersystem.storm.WeatherObject;

import java.util.List;

@Mixin(WeatherManager.class)
public abstract class WeatherManagerMixin {
    @Shadow
    public abstract List<WeatherObject> getStormObjects();

    /**
     * @author ninjaguardian
     * @reason The original can cause a {@link ClassCastException} and treated storms as spheres instead of cylinders
     */
    @Overwrite
    public boolean isPrecipitatingAt(@NotNull Vec3 parPos) {
        return Weather2Utils.isStormAbove(
                Weather2Utils.RAIN_STORM,
                getStormObjects(),
                parPos.x, parPos.z
        );
    }
}
