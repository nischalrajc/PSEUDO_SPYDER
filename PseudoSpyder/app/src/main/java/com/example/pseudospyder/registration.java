package com.example.pseudospyder;

import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;


public class registration extends AppCompatActivity {
    EditText e1,e2,e3,e4,e5,e6,e7,e8,e9,e10;
    Button b,P;
    RadioButton m,f;
    ImageView img;

    String fname,mname,lname,plc,dist,ip,email,dob,gender,un,pw,phone;
    SharedPreferences sp;
    private static final int FILE_SELECT_CODE = 0;
    int res;
    String fileName="",path="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        e1=(EditText)findViewById(R.id.editTextTextPersonName4);
        e2=(EditText)findViewById(R.id.editTextTextPersonName5);
        e3=(EditText)findViewById(R.id.editTextTextPersonName6);
        e4=(EditText)findViewById(R.id.editTextTextPersonName7);
        e5=(EditText)findViewById(R.id.editTextTextPersonName8);
        e6=(EditText)findViewById(R.id.editTextTextPersonName10);
        e7=(EditText)findViewById(R.id.editTextDate);
        e8=(EditText)findViewById(R.id.editTextTextPersonName11);
        e9=(EditText)findViewById(R.id.editTextTextPassword2);
        e10=(EditText)findViewById(R.id.editTextPhone);

        b=(Button)findViewById(R.id.button7);
        m=(RadioButton) findViewById(R.id.radioButton3);
        f=(RadioButton) findViewById(R.id.radioButton2);
        P=(Button)findViewById(R.id.button12);

        img=(ImageView)findViewById(R.id.imageView);

        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sp.getString("ipaddress","");
        if(android.os.Build.VERSION.SDK_INT>9)
        {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        P.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //getting all types of files
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(Intent.createChooser(intent, ""), FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {

                    Toast.makeText(getApplicationContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                }

            }
            });


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname=e1.getText().toString();
                mname=e2.getText().toString();
                lname=e3.getText().toString();
                plc=e4.getText().toString();
                dist=e5.getText().toString();
                email=e6.getText().toString();
                dob=e7.getText().toString();
                un=e8.getText().toString();
                pw=e9.getText().toString();
                phone=e10.getText().toString();

                if(m.isChecked())
                {
                    gender=m.getText().toString();
                }
                else {
                    gender = f.getText().toString();
                }
                res = uploadFile(path);

                if (res == 1) {
                    Toast.makeText(getApplicationContext(), " registered", Toast.LENGTH_LONG).show();
                    Intent ik = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(ik);

                } else {
                    Toast.makeText(getApplicationContext(), " error", Toast.LENGTH_LONG).show();
                }


        }

    });

}

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("File Uri", "File Uri: " + uri.toString());
                    // Get the path
                    try {
                        path = FileUtils.getPath(this, uri);
                        //e4.setText(path1);
                        Log.d("path", path);
                        File fil = new File(path);
                        int fln = (int) fil.length();
                        //  e2.setText(path);

                        File file = new File(path);

                        byte[] byteArray = null;
                        try {
                            InputStream inputStream = new FileInputStream(fil);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            byte[] b = new byte[fln];
                            int bytesRead = 0;

                            while ((bytesRead = inputStream.read(b)) != -1) {
                                bos.write(b, 0, bytesRead);
                            }

                            byteArray = bos.toByteArray();
                            inputStream.close();
                            Bitmap bmp= BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                            if(bmp!=null){
//
//
                                img.setVisibility(View.VISIBLE);
                                img.setImageBitmap(bmp);
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    } catch (URISyntaxException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Please select suitable file", Toast.LENGTH_LONG).show();
                }
                break;


        }


    }

    public int uploadFile(String sourceFileUri) {
        try {
            fileName = sourceFileUri;
            String upLoadServerUri = "http://" + sp.getString("ipaddress", "") + ":5000/reg";
            Toast.makeText(getApplicationContext(), upLoadServerUri, Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), path, Toast.LENGTH_LONG).show();
            FileUpload fp = new FileUpload(fileName);
            Map mp = new HashMap<String, String>();
            mp.put("fname", fname);
            mp.put("mname", mname);
            mp.put("lname", lname);
            mp.put("plc", plc);
            mp.put("dist", dist);
            mp.put("email", email);
            mp.put("dob", dob);
            mp.put("un", un);
            mp.put("pw", pw);
            mp.put("phone", phone);
            mp.put("gender", gender);

            fp.multipartRequest(upLoadServerUri, mp, fileName, "files", "application/octet-stream");
            return 1;

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"error"+e,Toast.LENGTH_LONG).show();
            return 0;
        }
    }
}

