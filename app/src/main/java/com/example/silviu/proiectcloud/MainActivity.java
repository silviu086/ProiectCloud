package com.example.silviu.proiectcloud;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText editTextLocatie;
    private ImageButton imageButtonMyLocation;
    private TextView textViewLat;
    private TextView textViewLong;
    private Button buttonCauta;

    private ImageView imageViewMap;
    private Context mContext;
    private GoogleMap map;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient = null;
    private LatLong coordonate = new LatLong(-1, -1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, GPSService.class));
        mContext = this;
        imageViewMap = (ImageView) findViewById(R.id.imageViewMap);
        editTextLocatie = (EditText) findViewById(R.id.editTextLocatie);
        imageButtonMyLocation = (ImageButton) findViewById(R.id.imageButtonMyLocation);
        textViewLat = (TextView) findViewById(R.id.textViewLat);
        textViewLong = (TextView) findViewById(R.id.textViewLong);
        buttonCauta = (Button) findViewById(R.id.buttonCauta);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        buttonCauta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((coordonate != null) || (coordonate.getLongitudine() != -1 && coordonate.getLatitudine() != -1)){
                    WeatherTask task = new WeatherTask(coordonate, mContext, new TaskCompletedWeather() {
                        @Override
                        public void onTaskCompleted(Weather result) {
                            if(result != null){
                                Intent myIntent = new Intent(MainActivity.this, WeatherActivity.class);
                                myIntent.putExtra("weather", result);
                                myIntent.putExtra("locatie", editTextLocatie.getText().toString());
                                MainActivity.this.startActivity(myIntent);
                            }else{
                                Toast.makeText(mContext, "Locatie invalida", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    task.execute();
                }else{
                    Toast.makeText(mContext, "Locatie invalida", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        editTextLocatie.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                    GeocodingTask task = new GeocodingTask(map, mContext, editTextLocatie.getText().toString(), new TaskCompleted() {
                        @Override
                        public void onTaskCompleted(LatLong result) {
                            if(result != null){
                                imageViewMap.setVisibility(View.GONE);
                                coordonate = result;
                                textViewLat.setText(String.valueOf(result.getLatitudine()));
                                textViewLong.setText(String.valueOf(result.getLongitudine()));
                            }
                        }
                    });
                    task.execute();
                    return true;
                }
                return false;
            }
        });

        imageButtonMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coordonate.setLatitudine(GPSService.lat);
                coordonate.setLongitudine(GPSService.lng);

                if(coordonate.getLatitudine() == -1 && coordonate.getLongitudine() == -1){
                    Toast.makeText(mContext, "Locatie necunoscuta", Toast.LENGTH_SHORT).show();
                }else{
                    textViewLat.setText(String.valueOf(coordonate.getLatitudine()));
                    textViewLong.setText(String.valueOf(coordonate.getLongitudine()));
                    WeatherTask task = new WeatherTask(coordonate, mContext, new TaskCompletedWeather() {
                        @Override
                        public void onTaskCompleted(Weather result) {
                            if(result != null){
                                editTextLocatie.setText(result.getName());
                                imageViewMap.setVisibility(View.GONE);
                                map.clear();
                                MarkerOptions markerOptions1 = new MarkerOptions();
                                LatLng latlng = new LatLng(coordonate.getLatitudine(), coordonate.getLongitudine());
                                markerOptions1.position(latlng);
                                markerOptions1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                                map.addMarker(markerOptions1).setTitle(editTextLocatie.getText().toString());

                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                builder.include(new LatLng(coordonate.getLatitudine()-0.15, coordonate.getLongitudine()-0.15));
                                builder.include(new LatLng(coordonate.getLatitudine()+0.15, coordonate.getLongitudine()+0.15));
                                final LatLngBounds bounds = builder.build();
                                map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                    @Override
                                    public void onMapLoaded() {
                                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                                        map.animateCamera(cameraUpdate);
                                    }
                                });
                            }else{
                                Toast.makeText(mContext, "A aparut o eroare", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    task.execute();
                }
            }
        });
    }
}
