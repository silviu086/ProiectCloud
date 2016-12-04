package com.example.silviu.proiectcloud;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Silviu on 03.12.2016.
 */

public class WeatherTask extends AsyncTask<String, Integer, Weather> {
    JSONObject jsonObject;
    ProgressDialog progressDialog;
    TaskCompletedWeather taskCompleted;
    LatLong coordonate;
    private Context mContext;

    public WeatherTask(LatLong coordonate, Context context, TaskCompletedWeather taskCompleted) {
        this.coordonate = coordonate;
        this.mContext = context;
        this.taskCompleted = taskCompleted;
    }

    private String buildDirectionsUrl() {
        return "http://api.openweathermap.org/data/2.5/weather?" +
                "lat=" + String.valueOf(coordonate.getLatitudine()).substring(0,5) +
                "&lon=" + String.valueOf(coordonate.getLongitudine()).substring(0, 5) +
                "&units=metric" +
                "&cluster=yes" +
                "&lang=ro" +
                "&appid=f3ba43535be08e6082c2154b4243581f";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("Asteptati...");
        progressDialog.setMessage("Se preiau datele");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Weather result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        taskCompleted.onTaskCompleted(result);
    }

    @Override
    protected Weather doInBackground(String... params) {
        List<List<HashMap<String, String>>> directionRoutes = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        Weather weather = new Weather();
        try {
            String strUrl = buildDirectionsUrl();
            URL url = new URL(strUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            urlConnection.disconnect();
            jsonObject = new JSONObject(sb.toString());
            int result = jsonObject.getInt("cod");
            if(result == 200){
                if(jsonObject!=null){
                    JSONObject jsonCoord = jsonObject.getJSONObject("coord");
                    weather.setCoordonate(new LatLong(jsonCoord.getDouble("lat"), jsonCoord.getDouble("lon")));

                    JSONArray jsonWeather = jsonObject.getJSONArray("weather");
                    JSONObject jsonWeatherOb = jsonWeather.getJSONObject(0);
                    weather.setWeatherId(jsonWeatherOb.getInt("id"));
                    weather.setWeatherMain(jsonWeatherOb.getString("main"));
                    weather.setWeatherDescription(jsonWeatherOb.getString("description"));
                    weather.setWeatherIcon(jsonWeatherOb.getString("icon"));

                    JSONObject jsonMain = jsonObject.getJSONObject("main");
                    weather.setMainTemp(jsonMain.getDouble("temp"));
                    weather.setMainPressure(jsonMain.getDouble("pressure"));
                    weather.setMainHumidity(jsonMain.getInt("humidity"));
                    weather.setMainTempMin(jsonMain.getDouble("temp_min"));
                    weather.setMainTempMax(jsonMain.getDouble("temp_max"));

                    JSONObject jsonWind = jsonObject.getJSONObject("wind");
                    weather.setWindSpeed(jsonWind.getDouble("speed"));

                    JSONObject jsonClouds = jsonObject.getJSONObject("clouds");
                    weather.setCloudsAll(jsonClouds.getInt("all"));

                    JSONObject jsonSys = jsonObject.getJSONObject("sys");
                    weather.setSysCountry(jsonSys.getString("country"));
                    weather.setSysSunrise(jsonSys.getLong("sunrise"));
                    weather.setSysSunset(jsonSys.getLong("sunset"));

                    weather.setName(jsonObject.getString("name"));
                    return weather;
                }
            }
        } catch (Exception e) {
            Log.e("DirectionsMap", e.getMessage());
        }
        return null;
    }
}
