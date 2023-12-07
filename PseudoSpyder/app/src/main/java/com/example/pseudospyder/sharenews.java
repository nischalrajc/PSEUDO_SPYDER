package com.example.pseudospyder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class sharenews extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView v;
    ArrayList<String> date, heading, content, status, nid;
    SharedPreferences sh;
    String url = "", ip = "",cnt,stat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewnews);
        v = (ListView) findViewById(R.id.listview);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        v.setOnItemClickListener(this);

        ip = sh.getString("ipaddress", "");

        url = "http://" + ip + ":5000/viewnews";

        //RequestQueue queue = Volley.newRequestQueue(viewnews.this);
        //String url = "http://"+sh

        RequestQueue queue = Volley.newRequestQueue(sharenews.this);


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {

                    JSONArray ar = new JSONArray(response);

                    date = new ArrayList<>();
                    heading = new ArrayList<>();
                    content = new ArrayList<>();
                    status = new ArrayList<>();
                    nid = new ArrayList<>();
                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        date.add(jo.getString("date"));
                        heading.add(jo.getString("heading"));
                        content.add(jo.getString("content"));
                        status.add(jo.getString("status"));
                        nid.add(jo.getString("nid"));


                    }
                    v.setAdapter(new Custom(sharenews.this, date, heading, content, status));


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "E" + e, Toast.LENGTH_LONG).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        cnt=content.get(position);
        stat=status.get(position);
        SharedPreferences.Editor ed=sh.edit();
        ed.putString("content",cnt);
        ed.putString("status",stat);
        ed.commit();
        AlertDialog.Builder ald=new AlertDialog.Builder(sharenews.this);
        ald.setTitle("Select option")
                .setPositiveButton(" SHARE NEWS ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {




                        Intent i=new Intent(getApplicationContext(),chatlist.class);
                        startActivity(i);






                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent i=new Intent(getApplicationContext(),sharenews.class);
                        startActivity(i);



                    }
                });

        AlertDialog al=ald.create();
        al.show();



    }
}