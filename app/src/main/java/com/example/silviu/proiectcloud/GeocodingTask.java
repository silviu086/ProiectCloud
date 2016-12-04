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

/**
 * Created by Silviu on 02.12.2016.
 */

public class GeocodingTask extends AsyncTask<String, Integer, LatLong> {
    JSONObject jsonObject;
    ProgressDialog progressDialog;
    TaskCompleted taskCompleted;
    private GoogleMap mMap;
    private Context mContext;
    private String mLocatie;

    public GeocodingTask(GoogleMap map, Context context, String locatie, TaskCompleted taskCompleted) {
        this.mMap = map;
        this.mContext = context;
        this.mLocatie = locatie;
        this.taskCompleted = taskCompleted;
    }

    private String buildDirectionsUrl(String adresa) {
        if(adresa.contains(" ")){
            adresa = adresa.replace(" ", "-");
        }
        return "https://maps.googleapis.com/maps/api/geocode/json?" + "address=" + adresa + "&key=AIzaSyB6R_oWfl76AL1qaS1_YyU0KNj1_AKht-4";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("Asteptati...");
        progressDialog.setMessage("Se incarca harta");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(LatLong locatie) {
        super.onPostExecute(locatie);
        if(locatie!=null){
            mMap.clear();
            MarkerOptions markerOptions1 = new MarkerOptions();
            LatLng latlng = new LatLng(locatie.getLatitudine(), locatie.getLongitudine());
            markerOptions1.position(latlng);
            markerOptions1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            mMap.addMarker(markerOptions1).setTitle(mLocatie);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(new LatLng(locatie.getLatitudine()-0.15, locatie.getLongitudine()-0.15));
            builder.include(new LatLng(locatie.getLatitudine()+0.15, locatie.getLongitudine()+0.15));
            final LatLngBounds bounds = builder.build();
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                    mMap.animateCamera(cameraUpdate);
                }
            });
            progressDialog.dismiss();
            taskCompleted.onTaskCompleted(locatie);
        }else{
            progressDialog.dismiss();
            Toast.makeText(mContext, "Locatie invalida!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected LatLong doInBackground(String... params) {
        List<List<HashMap<String, String>>> directionRoutes = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        try {
            String strUrl = buildDirectionsUrl(mLocatie);
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
            String result = jsonObject.getString("status");
            if(result.equals("OK")){
                if(jsonObject!=null){
                    JSONArray ob = jsonObject.getJSONArray("results");
                    JSONObject geometry = ob.getJSONObject(0).getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    double lat = location.getDouble("lat");
                    double lng = location.getDouble("lng");
                    LatLong coor = new LatLong(lat, lng);
                    return coor;
                }
            }
        } catch (Exception e) {
            Log.e("DirectionsMap", e.getMessage());
        }
        return null;
    }
}
