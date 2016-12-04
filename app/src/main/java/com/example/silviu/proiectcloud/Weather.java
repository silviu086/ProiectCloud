package com.example.silviu.proiectcloud;

import java.io.Serializable;

/**
 * Created by Silviu on 03.12.2016.
 */

public class Weather implements Serializable {
    private LatLong coordonate;
    private int weatherId;
    private String weatherMain;
    private String weatherDescription;
    private String weatherIcon;
    private double mainTemp;
    private double mainPressure;
    private int mainHumidity;
    private double mainTempMin;
    private double mainTempMax;
    private double windSpeed;
    private int cloudsAll;
    private String sysCountry;
    private long sysSunrise;
    private long sysSunset;
    private String name;

    public Weather(LatLong coordonate, int weatherId, String weatherMain, String weatherDescription, String weatherIcon, double mainTemp, double mainPressure, int mainHumidity, double mainTempMin, double mainTempMax, double mainSeaLevel, double mainGrndLevel, double windSpeed, int cloudsAll, String sysCountry, long sysSunrise, long sysSunset, String name) {
        this.coordonate = coordonate;
        this.weatherId = weatherId;
        this.weatherMain = weatherMain;
        this.weatherDescription = weatherDescription;
        this.weatherIcon = weatherIcon;
        this.mainTemp = mainTemp;
        this.mainPressure = mainPressure;
        this.mainHumidity = mainHumidity;
        this.mainTempMin = mainTempMin;
        this.mainTempMax = mainTempMax;
        this.windSpeed = windSpeed;
        this.cloudsAll = cloudsAll;
        this.sysCountry = sysCountry;
        this.sysSunrise = sysSunrise;
        this.sysSunset = sysSunset;
        this.name = name;
    }

    public Weather() {
    }

    public LatLong getCoordonate() {
        return coordonate;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public double getMainTemp() {
        return mainTemp;
    }

    public double getMainPressure() {
        return mainPressure;
    }

    public int getMainHumidity() {
        return mainHumidity;
    }

    public double getMainTempMin() {
        return mainTempMin;
    }

    public double getMainTempMax() {
        return mainTempMax;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public int getCloudsAll() {
        return cloudsAll;
    }

    public String getSysCountry() {
        return sysCountry;
    }

    public long getSysSunrise() {
        return sysSunrise;
    }

    public long getSysSunset() {
        return sysSunset;
    }

    public String getName() {
        return name;
    }

    public void setCoordonate(LatLong coordonate) {
        this.coordonate = coordonate;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public void setWeatherMain(String weatherMain) {
        this.weatherMain = weatherMain;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public void setMainTemp(double mainTemp) {
        this.mainTemp = mainTemp;
    }

    public void setMainPressure(double mainPressure) {
        this.mainPressure = mainPressure;
    }

    public void setMainHumidity(int mainHumidity) {
        this.mainHumidity = mainHumidity;
    }

    public void setMainTempMin(double mainTempMin) {
        this.mainTempMin = mainTempMin;
    }

    public void setMainTempMax(double mainTempMax) {
        this.mainTempMax = mainTempMax;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setCloudsAll(int cloudsAll) {
        this.cloudsAll = cloudsAll;
    }

    public void setSysCountry(String sysCountry) {
        this.sysCountry = sysCountry;
    }

    public void setSysSunrise(long sysSunrise) {
        this.sysSunrise = sysSunrise;
    }

    public void setSysSunset(long sysSunset) {
        this.sysSunset = sysSunset;
    }

    public void setName(String name) {
        this.name = name;
    }
}
