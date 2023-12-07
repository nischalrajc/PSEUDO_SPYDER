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

public class Viewrequest extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lv;
    SharedPreferences sh;
    String ip,url;
    ArrayList<String> name,img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewrequest);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lv=(ListView)findViewById(R.id.listview);

        ip = sh.getString("ipaddress", "");

        url = "http://" + ip + ":5000/viewrequest";

        //RequestQueue queue = Volley.newRequestQueue(viewnews.this);
        //String url = "http://"+sh
        RequestQueue queue = Volley.newRequestQueue(Viewrequest.this);


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {

                    JSONArray ar = new JSONArray(response);

                    name = new ArrayList<>();
                    img= new ArrayList<>();
                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        name.add(jo.getString("fname"));
                        img.add(jo.getString("photo"));

                    }
                    lv.setAdapter(new customimage(Viewrequest.this,name,img));
                    lv.setOnItemClickListener(Viewrequest.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());

                    Toast.makeText(getApplicationContext(), "eeeeee"+e, Toast.LENGTH_SHORT).show();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Viewrequest.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("uid",sh.getString("lid",""));



                return params;
            }
        };
        queue.add(stringRequest);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder ald=new AlertDialog.Builder(Viewrequest.this);
        ald.setTitle("Select option")
                .setPositiveButton(" ACCEPT ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        RequestQueue queue = Volley.newRequestQueue(Viewrequest.this);
                        String url ="http://"+ip+":5000/acceptfriendrequest";

                        // Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the response string.
                                Log.d("+++++++++++++++++",response);
                                try {
                                    JSONObject json=new JSONObject(response);
                                    String res=json.getString("task");
                                    if(res.equalsIgnoreCase("success"))

                                    {
                                        Toast.makeText(getApplicationContext(),"Request Accepted",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(),Viewrequest.class));

                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(getApplicationContext(),"Error"+error,Toast.LENGTH_LONG).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams()
                            {
                                Map<String, String>  params = new HashMap<String, String>();

                                params.put("fromid",sh.getString("lid",""));
                                return params;
                            }
                        };
                        // Add the request to the RequestQueue.
                        queue.add(stringRequest);








                    }
                })
                .setNegativeButton("REJECT ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        RequestQueue queue = Volley.newRequestQueue(Viewrequest.this);
                        String url ="http://"+ip+":5000/rejectfriendrequest";

                        // Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the response string.
                                Log.d("+++++++++++++++++",response);
                                try {
                                    JSONObject json=new JSONObject(response);
                                    String res=json.getString("task");
                                    if(res.equalsIgnoreCase("success"))

                                    {
                                        Toast.makeText(getApplicationContext(),"Request Rejected",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(),Viewrequest.class));

                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams()
                            {
                                Map<String, String>  params = new HashMap<String, String>();

                                params.put("fromid",sh.getString("lid",""));
                                return params;
                            }
                        };
                        // Add the request to the RequestQueue.
                        queue.add(stringRequest);

                    }
                });

        AlertDialog al=ald.create();
        al.show();

    }
}