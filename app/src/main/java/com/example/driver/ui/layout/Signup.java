package com.example.driver.ui.layout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

import util.constant;

public class Signup extends AppCompatActivity {


    EditText username,nic;
    public String uName,uNic,sessionId;
    ImageButton btnsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        username = findViewById(R.id.userName);
        nic = findViewById(R.id.nic);
        btnsignup =(ImageButton) findViewById(R.id.btnsignup);



        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(username.getText().toString())){
                    Toast.makeText(Signup.this,"Enter the Name",Toast.LENGTH_SHORT).show();

                }
                else if(TextUtils.isEmpty(nic.getText().toString())){
                    Toast.makeText(Signup.this,"Enter the NIC number",Toast.LENGTH_SHORT).show();

                }
                else{
                    checkCustomer();

                }

            }
        });
    }


    public void checkCustomer(){

        uNic = nic.getText().toString();
        Log.e("Response", "nic: "+uNic );

        StringRequest stringRequest =new StringRequest(Request.Method.POST,
                constant.checkNICr_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("response", "onResponse: "+response );
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                            String code=jsonObject.getString("code");







                            if(code.equals("false"))
                            {
                                Log.e("response", "if " );
                                Toast.makeText(Signup.this,"Registered",Toast.LENGTH_SHORT).show();


                                uploadata();

                                Intent intent = new Intent(Signup.this,Login.class);
                                startActivity(intent);

                            }
                            else {
                                Log.e("response", "else " );
                                Toast.makeText(Signup.this,"Already Registered",Toast.LENGTH_SHORT).show();




                            }
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

                params.put("password",uNic);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }

    public void uploadata(){

        uName = username.getText().toString();
        uNic = nic.getText().toString();

        StringRequest stringRequest =new StringRequest(Request.Method.POST,
                constant.user_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("response", "onResponse: "+response );
                            JSONArray jsonArray = new JSONArray(response);

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

                params.put("name",uName);
                params.put("nic",uNic);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);




    }
}