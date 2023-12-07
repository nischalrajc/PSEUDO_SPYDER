package com.example.pseudospyder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class chatlist extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView v;
    SharedPreferences sh;
    String ip,url;
    ArrayList<String> name,img,toid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);
        v = (ListView) findViewById(R.id.listview2);
        v.setOnItemClickListener(this);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip = sh.getString("ipaddress", "");

        url = "http://" + ip + ":5000/viewfriend";

        //RequestQueue queue = Volley.newRequestQueue(viewnews.this);
        //String url = "http://"+sh

        RequestQueue queue = Volley.newRequestQueue(chatlist.this);


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {

                    JSONArray ar = new JSONArray(response);

                    name = new ArrayList<>();
                    img = new ArrayList<>();
                    toid = new ArrayList<>();
                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        name.add(jo.getString("fname"));
                        img.add(jo.getString("photo"));
                        toid.add(jo.getString("uid"));



                    }
                    v.setAdapter(new customimage(chatlist.this,name,img));


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
                params.put("uid",sh.getString("lid",""));



                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent ik=new Intent(getApplicationContext(),chat.class);
        ik.putExtra("fid",toid.get(position));
        startActivity(ik);
    }
}