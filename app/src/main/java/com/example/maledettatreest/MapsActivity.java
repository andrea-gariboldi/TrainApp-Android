package com.example.maledettatreest;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.maledettatreest.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    double currentlat = 0, currentlong=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          // permessi non ancora concessi
        Log.d("PERMESSI", "permesso non concesso");
          final int REQUEST_LOCATION = 2;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
          // permessi concessi
            Log.d("PERMESSI", "permesso  concesso");
            /*
            Task<Location> locationResult = LocationServices
                    .getFusedLocationProviderClient(this)
                    .getLastLocation()
                    .addC
            locationResult.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    currentlat = location.getLatitude();
                    currentlong = location.getLongitude();
                }
            });

             */
            
         }


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setMinZoomPreference(12.0f);


        Model m = Model.getInstance();

        CommunicationController cc = new CommunicationController(this);

        Response.Listener<JSONObject> responseListenerStations = response -> {
            Log.d("StazioniMappa", response.toString());
            try {
                m.stazioniMappa = new ArrayList<>();
                JSONArray jA = response.getJSONArray("stations");
                for (int i = 0; i < jA.length(); i++) {
                    m.stazioniMappa.add(new StazioneMappa(jA.getJSONObject(i).getString("sname"), jA.getJSONObject(i).getString("lat"), jA.getJSONObject(i).getString("lon")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //linea tra i punti sulla mappa
            PolylineOptions polOpt = new PolylineOptions();

            for (int i = 0; i < m.stazioniMappa.size(); i++) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(m.stazioniMappa.get(i).lat) , Double.parseDouble(m.stazioniMappa.get(i).lon))).title(m.stazioniMappa.get(i).sname));
                polOpt.add(new LatLng(Double.parseDouble(m.stazioniMappa.get(i).lat) , Double.parseDouble(m.stazioniMappa.get(i).lon)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(m.stazioniMappa.get(i).lat) , Double.parseDouble(m.stazioniMappa.get(i).lon))));
            }
            mMap.addMarker(new MarkerOptions().position(new LatLng( currentlat, currentlong)));
            polOpt.clickable(true);
            Polyline pol = mMap.addPolyline(polOpt);
        };
        cc.getStations("YpbgbyAdECHkJfLR", m.nomeTrattaSelezionata.s1.terminus_did, responseListenerStations);

        Log.d("StazioniModelMappa", String.valueOf(m.stazioniMappa.size()));

        // Add a marker in Milano and move the camera
       /* LatLng milano = new LatLng(45, 9);
        mMap.addMarker(new MarkerOptions().position(milano).title("Marker in Milano"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(milano));*/

    }
}