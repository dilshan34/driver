package User;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.driver.R;
import com.example.driver.databinding.ActivityMapsBinding;

import Driver.MapsActivity;
import Driver.driverLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.maps.android.SphericalUtil;

import java.text.DecimalFormat;

public class UserMapActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Button startbtn,stop;
    private DatabaseReference reference;
    private LocationManager manager;
    Marker myMarker;
    ImageButton chat;
    private TextView userDistance;
    Double distance;
    LatLng weerawila = new LatLng(6.255709999999999,81.22725);

    private final int MIN_TIME = 1000;
    private final int MIN_DISTANCE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_map);

        chat = findViewById(R.id.chat);

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        userDistance = findViewById(R.id.userDistance);

        reference= FirebaseDatabase.getInstance().getReference().child("driver");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.usermap);
        mapFragment.getMapAsync(this);

        FirebaseMessaging.getInstance().subscribeToTopic("news");


        getLocationUpdate();
        readChanges();

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMapActivity.this,EmergencyActivity.class);
                startActivity(intent);
            }
        });
    }

    private void readChanges() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    try {
                        driverLocation location= dataSnapshot.getValue(driverLocation.class);
                        //  Log.e("TAG", "driverLocation: ",location );
                        if(location != null){


                            // myMarker.setPosition(new LatLng(6.1429,81.1212));
                            myMarker.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(weerawila));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(weerawila, 12.0f));
                            Circle circle = mMap.addCircle(new CircleOptions()
                                    .center(weerawila)
                                    .radius(1000)
                                    .strokeColor(Color.RED));
                            distance = SphericalUtil.computeDistanceBetween(new LatLng(location.getLatitude(),location.getLongitude()), weerawila);
                            // Toast.makeText(MapsActivity.this, "Distance between Sydney and Brisbane is \n " + String.format("%.2f", distance / 1000) + "km", Toast.LENGTH_SHORT).show();

                           


                            userDistance.setText(String.format("%.2f" +"KM", distance / 1000) );

                            float y1 = (float) (distance / 1000);
                            DecimalFormat df = new DecimalFormat("#.0");
                            y1 = Float.valueOf(df.format(y1));
                            // Toast.makeText(MapsActivity.this, "Vehicle in 1km ahead \n "+y1, Toast.LENGTH_SHORT).show();

                            if(y1 == 1 ){

                                Toast.makeText(UserMapActivity.this, "Vehicle in 1km ahead \n ", Toast.LENGTH_SHORT).show();

                            }

                        }
                    }catch (Exception e){
                        Toast.makeText(UserMapActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getLocationUpdate() {
        if(manager != null){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                } else {
                    Toast.makeText(this, "No provider Enabled", Toast.LENGTH_SHORT).show();
                }
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},101);
            }

        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
       // LatLng weerawila = new LatLng(6.255709999999999,81.22725);
        myMarker=  mMap.addMarker(new MarkerOptions().position(sydney).title("Driver"));
        mMap.addMarker(new MarkerOptions().position(weerawila).title("User"));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}