package Driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.driver.R;
import com.example.driver.ui.layout.Login;
import com.example.driver.ui.layout.Profile;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import User.UserMapActivity;
import util.constant;

public class dashboard extends AppCompatActivity {

    CardView user,driver,driverUser,driverLogout;
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";
    String username,password,uname,nic,lati,longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        driver = findViewById(R.id.driver);
        driverUser = findViewById(R.id.driverUser);
        driverLogout = findViewById(R.id.driverLogout);

        mRequestQue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        String id = getIntent().getStringExtra("nic");



        getCordinates();

        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
                Intent intent = new Intent(dashboard.this, MapsActivity.class);
                intent.putExtra("lati",lati);
                intent.putExtra("longi",longi);
                Log.e("TAG", "onClick: driver id"+ longi );
                startActivity(intent);



            }
        });

        driverUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboard.this, Profile.class);
                intent.putExtra("nic",id);
                startActivity(intent);
            }
        });

        driverLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboard.this, Login.class);
                startActivity(intent);
                finish();
            }
        });



    }

    public void sendNotification(){
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to","/topics/"+"news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","Taxi");
            notificationObj.put("body","Vehicle is start from the destination");

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

    public void getCordinates(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                constant.getCordinate,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("response", "onResponse: "+response );
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    lati = jsonObject.getString("latitude");
                    longi = jsonObject.getString("longitude");
                    Log.e("Response", "onResponse: longi"+longi );







                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}