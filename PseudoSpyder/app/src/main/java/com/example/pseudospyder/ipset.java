package com.example.pseudospyder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ipset extends AppCompatActivity {
    EditText e1;
    Button b;
    String ip="192.168.43.29";
    SharedPreferences sh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipset);

        e1=(EditText)findViewById(R.id.editTextTextPersonName);
        b=(Button)findViewById(R.id.button3);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip=e1.getText().toString();
                if(ip.equals(""))
                {
                    e1.setError("Enter valid ip address");
                }
                else
                {
                    SharedPreferences.Editor ed=sh.edit();
                    ed.putString("ipaddress",ip);
                    ed.commit();

                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                }

            }
        });
    }
}