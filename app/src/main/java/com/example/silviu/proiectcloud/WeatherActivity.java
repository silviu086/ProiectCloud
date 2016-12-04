package com.example.silviu.proiectcloud;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Weather weather;
    private ImageView imageViewWeather;
    private TextView textViewLocatie;
    private TextView textViewWeatherDescription;
    private Button buttonClose;
    private TextView textViewGrade;
    private TextView textViewLat;
    private TextView textViewLon;
    private TextView textViewPresiune;
    private TextView texttextViewUmiditate;
    private TextView textViewVant;
    private TextView textViewNori;
    private TextView textViewTara;
    private TextView textViewRasarit;
    private TextView textViewApus;
    private ImageView imageViewMap;
    private GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        weather = (Weather) getIntent().getExtras().getSerializable("weather");

        imageViewWeather = (ImageView) findViewById(R.id.imageViewWeather);
        textViewLocatie = (TextView) findViewById(R.id.textViewLocatie);
        textViewWeatherDescription = (TextView) findViewById(R.id.textViewWeatherDescription);
        buttonClose = (Button) findViewById(R.id.buttonClose);
        textViewGrade = (TextView) findViewById(R.id.textViewGrade);
        textViewLat = (TextView) findViewById(R.id.textViewLat);
        textViewLon = (TextView) findViewById(R.id.textViewLon);
        textViewPresiune = (TextView) findViewById(R.id.textViewPresiuneValue);
        texttextViewUmiditate = (TextView) findViewById(R.id.textViewUmiditateValue);
        textViewVant = (TextView) findViewById(R.id.textViewVantValue);
        textViewNori = (TextView) findViewById(R.id.textViewNoriValue);
        textViewTara = (TextView) findViewById(R.id.textViewTaraValue);
        textViewRasarit = (TextView) findViewById(R.id.textViewRasaritValue);
        textViewApus = (TextView) findViewById(R.id.textViewApusValue);
        imageViewMap = (ImageView) findViewById(R.id.imageViewMap);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Drawable imageWeather = null;
        switch(weather.getWeatherIcon()){
            case "01d":
                imageWeather = getResources().getDrawable(R.drawable.a01d);
                break;
            case "01n":
                imageWeather = getResources().getDrawable(R.drawable.a01n);
                break;
            case "02d":
                imageWeather = getResources().getDrawable(R.drawable.a02d);
                break;
            case "02n":
                imageWeather = getResources().getDrawable(R.drawable.a02n);
                break;
            case "03d":
                imageWeather = getResources().getDrawable(R.drawable.a03d);
                break;
            case "03n":
                imageWeather = getResources().getDrawable(R.drawable.a03n);
                break;
            case "04d":
                imageWeather = getResources().getDrawable(R.drawable.a04d);
                break;
            case "04n":
                imageWeather = getResources().getDrawable(R.drawable.a04n);
                break;
            case "09d":
                imageWeather = getResources().getDrawable(R.drawable.a09d);
                break;
            case "09n":
                imageWeather = getResources().getDrawable(R.drawable.a09n);
                break;
            case "10d":
                imageWeather = getResources().getDrawable(R.drawable.a10d);
                break;
            case "10n":
                imageWeather = getResources().getDrawable(R.drawable.a10n);
                break;
            case "11d":
                imageWeather = getResources().getDrawable(R.drawable.a11d);
                break;
            case "11n":
                imageWeather = getResources().getDrawable(R.drawable.a11n);
                break;
            case "13d":
                imageWeather = getResources().getDrawable(R.drawable.a13d);
                break;
            case "13n":
                imageWeather = getResources().getDrawable(R.drawable.a13n);
                break;
            case "50d":
                imageWeather = getResources().getDrawable(R.drawable.a50d);
                break;
            case "50n":
                imageWeather = getResources().getDrawable(R.drawable.a50n);
                break;
        }
        if(imageWeather != null){
            imageViewWeather.setImageDrawable(imageWeather);
        }
        textViewLocatie.setText(getIntent().getExtras().getString("locatie"));
        textViewWeatherDescription.setText(weather.getWeatherDescription());
        textViewGrade.setText(String.valueOf(weather.getMainTemp()) + " °C (Max:" + String.valueOf(weather.getMainTempMax()) + " Min:" + String.valueOf(weather.getMainTempMin()) + ")");
        textViewLat.setText("lat. " + String.valueOf(weather.getCoordonate().getLatitudine()));
        textViewLon.setText("lon. " + String.valueOf(weather.getCoordonate().getLongitudine()));
        textViewPresiune.setText(String.valueOf(weather.getMainPressure()) + " hPa");
        texttextViewUmiditate.setText(String.valueOf(weather.getMainHumidity()) + "%");
        textViewVant.setText(String.valueOf(weather.getWindSpeed()) + " km/h");
        textViewNori.setText(String.valueOf(weather.getCloudsAll()) + "%");
        textViewTara.setText(weather.getSysCountry());

        long unixSeconds = weather.getSysSunrise();
        Date date = new Date(unixSeconds*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String formattedDate = sdf.format(date);
        textViewRasarit.setText(formattedDate);

        unixSeconds = weather.getSysSunset();
        date = new Date(unixSeconds*1000L);
        formattedDate = sdf.format(date);
        textViewApus.setText(formattedDate);

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        imageViewMap.setVisibility(View.GONE);
        map = googleMap;
        map.clear();
        MarkerOptions markerOptions1 = new MarkerOptions();
        LatLng latlng = new LatLng(weather.getCoordonate().getLatitudine(), weather.getCoordonate().getLongitudine());
        markerOptions1.position(latlng);
        markerOptions1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        map.addMarker(markerOptions1).setTitle(getIntent().getExtras().getString("locatie"));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(weather.getCoordonate().getLatitudine()-0.15, weather.getCoordonate().getLongitudine()-0.15));
        builder.include(new LatLng(weather.getCoordonate().getLatitudine()+0.15, weather.getCoordonate().getLongitudine()+0.15));
        final LatLngBounds bounds = builder.build();
        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                map.animateCamera(cameraUpdate);
            }
        });
    }
}
