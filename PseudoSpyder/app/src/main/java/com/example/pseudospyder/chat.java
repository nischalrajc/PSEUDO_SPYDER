package com.example.pseudospyder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

public class chat extends AppCompatActivity {
    LinearLayout lt;
    EditText ed;
    Button b1;
    TextView t1;


    String fid="";
    String id="";
    String ip="";
    Handler hd;
    static String prv="";

    String lastid;
    public static ArrayList<String> From_id,Toid,Message,Date ;

    public static String ur="",url2;
//    TelephonyManager tm;
    SharedPreferences  sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip= ((SharedPreferences) sp).getString("ip","");
        String msg=sp.getString("content","")+"\n"+ sp.getString("status","");


        ed=(EditText)findViewById(R.id.editText1);
        lt=(LinearLayout)findViewById(R.id.linear1);
        b1=(Button)findViewById(R.id.button1);
        t1=(TextView)findViewById(R.id.textView1);
    if(!msg.equals("")) {
        ed.setText(msg);
    }

        lastid="0";
        t1.setText(getIntent().getStringExtra("name"));

        fid=getIntent().getStringExtra("fid");


        id=sp.getString("lid", "");

        hd=new Handler();
        hd.post(r);


        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final String message=ed.getText().toString();
                if(message.equals(""))
                {
                    ed.setError("Enter message");
                    ed.requestFocus();
                }
                else {
                    ur = "http://" + sp.getString("ipaddress", "") + ":5000/sendmessage";

                    final RequestQueue res = Volley.newRequestQueue(chat.this);
                    StringRequest string = new StringRequest(Request.Method.POST, ur, new Response.Listener<String>() {
                                public void onResponse(String respo) {

                                    if (respo.equals("success")) {
                                        ed.setText("");

                                        res.stop();

                                    } else {


                                    }
                                }
                            }

                            , new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error+"",Toast.LENGTH_LONG ).show();


                        }
                    })

                    {
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("fromid", id);
                            params.put("toid", fid);
                            params.put("msg", message);


                            return params;
                        }
                    };
                    res.add(string);
                }

            }
        });


    }


    public Runnable r=new Runnable() {

        @Override
        public void run() {

            url2 = "http://" + sp.getString("ipaddress", "") + ":5000/viewmessage";
            final RequestQueue mqueu = Volley.newRequestQueue(chat.this);

            StringRequest string = new StringRequest(Request.Method.POST, url2,
                    new Response.Listener<String>() {
                        public void onResponse(String respo) {

                            try {
                                JSONArray arr = new JSONArray(respo);
                                if (respo.length() > 0) {


                                    From_id = new ArrayList<String>();
                                    Toid = new ArrayList<String>();
                                    Message = new ArrayList<String>();
                                    Date = new ArrayList<String>();
                                    lt.removeAllViews();
                                    for (int i = 0; i < arr.length(); i++) {
                                        JSONObject c = arr.getJSONObject(i);

                                        From_id.add(c.getString("fromid"));
                                        Toid.add(c.getString("toid"));
                                        Message.add(c.getString("message"));
                                        Date.add(c.getString("Date"));

                                        TextView tv = new TextView(getApplicationContext());
                                        TextView tv1 = new TextView(getApplicationContext());
                                        if (!c.getString("Date").equals(prv)) {
                                            //Toast.makeText(getApplicationContext(), "result is"+prv, Toast.LENGTH_LONG).show();
                                            tv1.setText(c.getString("Date"));
                                            tv1.setGravity(Gravity.CENTER);
                                            prv = c.getString("Date");
                                        }

                                        if (From_id.get(i).equalsIgnoreCase(id)) {
                                            tv.setTextColor(Color.RED);
                                            tv.setText("Me" + ": " + Message.get(i));
                                            tv.setGravity(Gravity.RIGHT);

                                            tv.setBackgroundColor(Color.WHITE);

                                            //tv1.setTextColor(Color.RED);
                                            //tv1.setText(date.get(i)+"");


                                            tv1.setBackgroundColor(Color.WHITE);


                                        } else {
                                            tv.setTextColor(Color.BLUE);
                                            tv.setText(Message.get(i));
                                            tv.setGravity(Gravity.LEFT);

                                            tv.setBackgroundColor(Color.YELLOW);

                                            //tv1.setTextColor(Color.BLACK);
                                            //tv1.setText(date.get(i));
                                            //tv1.setGravity(Gravity.CENTER);

                                            tv1.setBackgroundColor(Color.YELLOW);
                                        }

                                        lt.addView(tv);
                                        lt.addView(tv1);


                                    }

                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                Toast.makeText(getApplicationContext(), "err" + e, Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                            hd.postDelayed(r, 2000);
                        }
                    }

                    , new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("uid", id);
                    params.put("fid", fid);


                    return params;
                }
            };
            mqueu.add(string);
        }
    };
}