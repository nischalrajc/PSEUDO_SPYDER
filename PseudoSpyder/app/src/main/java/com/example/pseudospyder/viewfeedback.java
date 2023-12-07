package com.example.pseudospyder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class viewfeedback extends AppCompatActivity {
    String ip,url;
    SharedPreferences sp;
    ListView v;
    ArrayList<String> feedback,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewfeedback);

        v = (ListView) findViewById(R.id.listview);

        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sp.getString("ipaddress","");

        url = "http://" + ip + ":5000/viewfeedback";

        //RequestQueue queue = Volley.newRequestQueue(viewnews.this);
        //String url = "http://"+sh

        RequestQueue queue = Volley.newRequestQueue(viewfeedback.this);


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {

                    JSONArray ar = new JSONArray(response);

                    feedback = new ArrayList<>();
                    date = new ArrayList<>();

                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);

                        feedback.add(jo.getString("feedback"));
                        date.add(jo.getString("date"));



                    }
                    v.setAdapter(new custom2(viewfeedback.this,feedback,date));


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "E"+e, Toast.LENGTH_LONG).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Error"+error, Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("uid",sp.getString("lid",""));



                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}