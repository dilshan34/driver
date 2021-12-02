package com.example.driver.ui.layout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.driver.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Driver.dashboard;
import User.UserDashboard;
import util.constant;

public class Profile extends AppCompatActivity {

    String id ;
    private TextView name,address,userNic,type,head;
    Button profileLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

         id = getIntent().getStringExtra("nic");
        Log.e("TAG", "onCreate id: "+id );

        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        userNic = findViewById(R.id.userNic);
        type = findViewById(R.id.type);
        head = findViewById(R.id.head);
        profileLogout = findViewById(R.id.profilelogout);

        getUserDetails();

        profileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this,Login.class);
                startActivity(intent);
                finish();
            }
        });


    }



    public void getUserDetails(){


        Log.e("TAG", "getUserDetails: "+id );
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                constant.getUserDetails_url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("response", "onResponse: "+response );
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String uname=jsonObject.getString("name");
                    String uaddress = jsonObject.getString("address");
                    String unic = jsonObject.getString("nic");
                    String utype = jsonObject.getString("type");

                    Log.e("Response", "yupe======: "+utype );

                    name.setText(uname);
                    address.setText(uaddress);
                    userNic.setText(unic);
                    type.setText(utype);
                    head.setText(uname);



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            @Override
            protected Map<String, String> getParams()  {
                Map<String,String> params = new HashMap<String,String>();

                params.put("id",id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}