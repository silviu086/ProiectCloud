package com.example.silviu.proiectcloud;

import java.io.Serializable;

/**
 * Created by Silviu on 02.12.2016.
 */
public class LatLong implements Serializable {
    private double latitudine;
    private double longitudine;

    public LatLong(double latitudine, double longitudine) {
        this.latitudine = latitudine;
        this.longitudine = longitudine;
    }

    public LatLong(){

    }

    public double getLatitudine() {
        return latitudine;
    }

    public double getLongitudine() {
        return longitudine;
    }

    public void setLatitudine(double latitudine) {
        this.latitudine = latitudine;
    }

    public void setLongitudine(double longitudine) {
        this.longitudine = longitudine;
    }
}
