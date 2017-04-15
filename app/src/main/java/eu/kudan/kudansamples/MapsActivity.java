package eu.kudan.kudansamples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat", 12.894702);
//        lat = intent.getDoubleExtra("lat", 0);
        lon = intent.getDoubleExtra("lon", 77.675716);
//        lon = intent.getDoubleExtra("lon", 0);
        Log.d("TAG", "maps: "+lat+": "+lon);

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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Your Flower"));

        LatLng other = new LatLng(12.887405, 77.641357);
        mMap.addMarker(new MarkerOptions().position(other).title("Other Flower"));

        LatLng other1 = new LatLng(12.8874752,77.6397863);
        mMap.addMarker(new MarkerOptions().position(other1).title("Other Flower"));
        LatLng other2 = new LatLng(12.9553526,77.5855199);
        mMap.addMarker(new MarkerOptions().position(other2).title("Other Flower"));
        LatLng other3 = new LatLng(12.9709207,77.5853267);
        mMap.addMarker(new MarkerOptions().position(other3).title("Other Flower"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
