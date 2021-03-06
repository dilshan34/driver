package User;

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
import com.android.volley.toolbox.Volley;
import com.example.driver.R;
import com.example.driver.ui.layout.Login;
import com.example.driver.ui.layout.Profile;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Driver.MapsActivity;
import Driver.dashboard;

public class UserDashboard extends AppCompatActivity {
    CardView user,userInfo,userLogout;
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        user = findViewById(R.id.userDashboard);
        userInfo = findViewById(R.id.userInfo);
        userLogout = findViewById(R.id.userLogout);

        String id = getIntent().getStringExtra("nic");
        String lati = getIntent().getStringExtra("lati");
        String longi = getIntent().getStringExtra("longi");
        Log.e("TAG", "longi: "+longi );

        mRequestQue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboard.this, UserMapActivity.class);
                intent.putExtra("lati",lati);
                intent.putExtra("longi",longi);
                startActivity(intent);

            }
        });

        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserDashboard.this, Profile.class);
                intent.putExtra("nic",id);
                startActivity(intent);

            }
        });

        userLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboard.this, Login.class);
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
}