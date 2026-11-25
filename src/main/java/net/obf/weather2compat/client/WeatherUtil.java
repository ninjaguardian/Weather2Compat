package net.obf.weather2compat.client;

import weather2.ClientTickHandler;
import weather2.weathersystem.WeatherManager;

public final class WeatherUtil {
    private WeatherUtil() {}

    public static WeatherManager getClientWeather() {
        return ClientTickHandler.getClientWeather();
    }
}
