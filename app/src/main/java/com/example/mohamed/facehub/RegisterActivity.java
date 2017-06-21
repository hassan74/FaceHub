package com.example.mohamed.facehub;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText name ,email ,pass ,phone ,address ;
    String _name,_email,_pass,_phone,_address ;
    Button reg ;

    String urlParameters ;
    public void test ()
    {
        String url ="http://192.168.42.50:8080/FaceHub/rest/test";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        System.out.println("Responseis: "+ response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();

                System.out.println("That didn't work!");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
    public void register ()
    {
        _name =name.getText().toString();
        _email =email.getText().toString();
        _pass =pass.getText().toString();
        _phone =phone.getText().toString();
        _address =address.getText().toString();
        String url =GetURL.url+"signup";
        StringRequest stringRequest =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                String status ="",id="" ;
                try {
                    jsonObject = new JSONObject(response);
                    status= String.valueOf(jsonObject.get("status"));
                    id= String.valueOf(jsonObject.get("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status.equals("1"))
                {
                    Toast.makeText(getApplicationContext(),"register successfully",Toast.LENGTH_LONG).show();
                    Intent intent =new Intent(RegisterActivity.this ,MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid registration",Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                System.out.println("getError : "+error.getMessage());
                String cause = null;
                if (error.getCause() != null) {
                    cause = error.getCause().getMessage();
                }
                System.out.println("getcause : "+cause);
                if (error instanceof AuthFailureError) {
                    System.out.println("AuthFailureError");
                }
                if (error instanceof NoConnectionError) {
                    System.out.println("NoConnectionError");
                }
            }
        })
        {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("name", _name);
                params.put("email", _pass);
                params.put("pass", _pass);
                params.put("phone",_phone);
                params.put("address", _address);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name =(EditText)findViewById(R.id.userName);
        email=(EditText)findViewById(R.id.userEmail);
        pass=(EditText)findViewById(R.id.userPass);
        phone=(EditText)findViewById(R.id.userPhone);
        address=(EditText)findViewById(R.id.userAddress);
        reg =(Button)findViewById(R.id.register);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }

});

    }}

