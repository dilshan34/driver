package Driver;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.driver.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.driver.databinding.ActivityMapsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.maps.android.SphericalUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import User.UserMapActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ImageButton emergency,btnDistance;
    private DatabaseReference reference;
    private LocationManager manager;
    Marker myMarker;
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";
    private TextView distancePoint;
    LatLng sydney = new LatLng(-34, 151);
    LatLng weerawila = new LatLng(6.255709999999999,81.22725);
    Double distance;

    private final int MIN_TIME = 1000;
    private final int MIN_DISTANCE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        emergency = (ImageButton) findViewById(R.id.emergency);
        btnDistance = (ImageButton) findViewById(R.id.btnDistance);
        distancePoint = findViewById(R.id.distancePoint);


        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        reference= FirebaseDatabase.getInstance().getReference().child("driver");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mRequestQue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic("news");


        getLocationUpdate();
        readChanges();

        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });

        btnDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distance = SphericalUtil.computeDistanceBetween(sydney, weerawila);
                Toast.makeText(MapsActivity.this, "Distance between Sydney and Brisbane is \n " + String.format("%.2f", distance / 1000) + "km", Toast.LENGTH_SHORT).show();
                distancePoint.setText( String.format("%.2f" +"KM", distance / 1000));
            }
        });


    }

    public void sendNotification(){
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to","/topics/"+"news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","Emergency");
            notificationObj.put("body","Vehicle is broken");

            mainObj.put("notification",notificationObj);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    mainObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAArHo2bVo:APA91bH6ezUoeT4mXRjOsTujQnte-K5mWrnXKC8S8GatUJD-RDGJcA9qI64ZCkl-mA2_UsbITWdREMOfCaI5vnud7xo7ls2L07AB-xF8qYtmjU4s13VtQ2XtFdpIoRwtCOeScFoFr0pE");
                    Log.e("TAG", "getHeaders: "+header );
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }

    public void sendDistanceAlert(){
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to","/topics/"+"news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","Driver");
            notificationObj.put("body","Vehicle is 1km ahead");

            mainObj.put("notification",notificationObj);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    mainObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAArHo2bVo:APA91bH6ezUoeT4mXRjOsTujQnte-K5mWrnXKC8S8GatUJD-RDGJcA9qI64ZCkl-mA2_UsbITWdREMOfCaI5vnud7xo7ls2L07AB-xF8qYtmjU4s13VtQ2XtFdpIoRwtCOeScFoFr0pE");
                    Log.e("TAG", "getHeaders: "+header );
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }



    private void readChanges() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    try {
                        driverLocation location= dataSnapshot.getValue(driverLocation.class);
                        //  Log.e("TAG", "driverLocation: ",location );
                        if(location != null){


                            // myMarker.setPosition(new LatLng(6.1429,81.1212));
                            myMarker.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));
//                            Circle circle = mMap.addCircle(new CircleOptions()
//                                    .center(new  LatLng(6.2421,81.2292))
//                                    .radius(1000)
//                                    .strokeColor(Color.RED));
                            distance = SphericalUtil.computeDistanceBetween(new LatLng(location.getLatitude(),location.getLongitude()), weerawila);
                           // Toast.makeText(MapsActivity.this, "Distance between Sydney and Brisbane is \n " + String.format("%.2f", distance / 1000) + "km", Toast.LENGTH_SHORT).show();
                            distancePoint.setText( String.format("%.2f" +"KM", distance / 1000));

                            int value = (int)(distance / 1000);
                            // Toast.makeText(UserMapActivity.this, "Vehicle in 1km ahead \n "+km, Toast.LENGTH_SHORT).show();

                            float y1 = (float) (distance / 1000);
                            DecimalFormat df = new DecimalFormat("#.0");
                            y1 = Float.valueOf(df.format(y1));
                           // Toast.makeText(MapsActivity.this, "Vehicle in 1km ahead \n "+y1, Toast.LENGTH_SHORT).show();

                            if(y1 == 1 ){
                                sendDistanceAlert();
                                //Toast.makeText(MapsActivity.this, "Vehicle in 1km ahead \n ", Toast.LENGTH_SHORT).show();

                            }

                        }
                    }catch (Exception e){
                        Toast.makeText(MapsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 101){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLocationUpdate();
            }else{
                Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
            }
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

        // Add a marker in Sydney and move the camera

        myMarker=  mMap.addMarker(new MarkerOptions().position(sydney).title("Driver"));
        mMap.addMarker(new MarkerOptions().position(weerawila).title("User"));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);





    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (location != null){
            saveLocation(location);

        }
        else{
            Toast.makeText(this, "no locations", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveLocation(Location location) {
        reference.setValue(location);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}