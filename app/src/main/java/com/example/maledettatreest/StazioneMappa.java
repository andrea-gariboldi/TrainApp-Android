package com.example.maledettatreest;

import org.json.JSONObject;

public class StazioneMappa {
    String sname, lat, lon;

    public StazioneMappa(String sname, String lat, String lon) {
        this.sname = sname;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "StazioneMappa{" +
                "sname='" + sname + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                '}';
    }
}
