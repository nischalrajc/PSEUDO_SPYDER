package com.example.pseudospyder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class postnews extends AppCompatActivity {
    EditText e1,e2;
    Button b;
    String head,cnt,ip;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postnews);

        e1=(EditText)findViewById(R.id.editTextTextPersonName2);
        e2=(EditText)findViewById(R.id.editTextTextPersonName9);
        b=(Button)findViewById(R.id.button11);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sp.getString("ipaddress","");


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                head=e1.getText().toString();
                if(head.equals(""))
                {
                    e1.setError("Enter a valid news heading");
                }
                else {
                    cnt = e2.getText().toString();
                    if (cnt.equals("")) {
                        e1.setError("Enter a valid news content");
                    }

                    //Intent i=new Intent(getApplicationContext(),viewnews.class);
                    //startActivity(i);

                    RequestQueue queue = Volley.newRequestQueue(postnews.this);
                    String url = "http://" + ip + ":5000/newspost";

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.
                            Log.d("+++++++++++++++++", response);
                            try {
                                JSONObject json = new JSONObject(response);
                                String res = json.getString("task");
                                if (res.equalsIgnoreCase("success")) {
                                    Toast.makeText(getApplicationContext(), "news posted successfully", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(), viewnews.class));

                                } else {
                                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("head", head);
                            params.put("cnt", cnt);
                            params.put("uid", sp.getString("lid", ""));

                            return params;
                        }
                    };
                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);


                }
            }
        });


    }
}