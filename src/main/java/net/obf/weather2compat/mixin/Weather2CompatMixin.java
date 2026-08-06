package net.obf.weather2compat.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import weather2.ServerTickHandler;

@Mixin(Level.class)
public class Weather2CompatMixin {
    /**
     * @author ninjaguardian
     * @reason Switched to Weather2's system
     */
    @Overwrite
    public boolean isRainingAt(BlockPos pos) {
        return ServerTickHandler.getWeatherManagerFor((Level) (Object) this).isPrecipitatingAt(pos);
    }
}
