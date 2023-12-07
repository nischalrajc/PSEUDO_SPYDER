
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
import android.widget.AdapterView;
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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    EditText e1,e2;
    Button b1,b2;
    String uname,pswd,ip="",url="";
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1=(EditText)findViewById(R.id.editTextTextPersonName3);
        e2=(EditText)findViewById(R.id.editTextTextPassword);
        b1=(Button)findViewById(R.id.button);
        b2=(Button)findViewById(R.id.button2);
        sp=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());




        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname=e1.getText().toString();
                pswd=e2.getText().toString();





                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                url = "http://" + sp.getString("ipaddress","") + ":5000/log";

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        Log.d("+++++++++++++++++", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            String res = json.getString("task");

                            if (res.equalsIgnoreCase("fail")) {
                                Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();


                            } else {
                                String type = json.getString("type");
                                if(type.equals("user")) {

                                    SharedPreferences.Editor edp = sp.edit();
                                    edp.putString("lid", res);
                                    edp.commit();
                                    Intent ik = new Intent(getApplicationContext(), home.class);
                                    startActivity(ik);
                                }
                                else if(type.equals("block"))
                                {
                                    Toast.makeText(MainActivity.this, "Sorry,Unable to login since,you're blocked" +
                                            "", Toast.LENGTH_SHORT).show();

                                }


                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "e" + e, Toast.LENGTH_LONG).show();

                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("un", uname);
                        params.put("ps", pswd);

                        return params;
                    }
                };
                queue.add(stringRequest);




            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getApplicationContext(),registration.class);
                startActivity(i);

            }
        });







    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {




    }
}