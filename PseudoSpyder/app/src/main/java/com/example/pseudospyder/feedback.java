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

public class feedback extends AppCompatActivity {
    EditText e1;
    Button b,b1;
    String fb,ip;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        e1=(EditText)findViewById(R.id.editTextTextMultiLine);
        b=(Button)findViewById(R.id.button13);
        b1=(Button)findViewById(R.id.button16);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sp.getString("ipaddress","");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),viewfeedback.class);
                startActivity(i);
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fb=e1.getText().toString();

                //Intent i=new Intent(getApplicationContext(),viewnews.class);
                //startActivity(i);

                RequestQueue queue = Volley.newRequestQueue(feedback.this);
                String url ="http://"+ip+":5000/feedback";

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
                                Toast.makeText(getApplicationContext(),"feedback send successfully",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(),home.class));

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
                        params.put("fb", fb);
                        params.put("uid",sp.getString("lid",""));

                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);




            }
        });


    }
}
