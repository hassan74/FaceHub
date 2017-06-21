package com.example.mohamed.facehub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button login  ;
    private EditText email ,pass ,ip ;
    private TextView signup ,testlogin;
    String emailText ,passText ,ipAdress ;

     class RegisterLitenter implements View.OnClickListener{
         @Override
         public void onClick(View v) {
             Intent intent =new Intent(MainActivity.this ,RegisterActivity.class);
             startActivity(intent);
         }
     }
     void login ()
     {
         emailText = String.valueOf(email.getText());
         passText = String.valueOf(pass.getText());
         ipAdress=String.valueOf(ip.getText());
         GetURL.url="http://"+ipAdress+":8080/FaceHub/rest/";
         String serviceUrl=GetURL.url+"login";
         StringRequest stringRequest = new StringRequest(Request.Method.POST, serviceUrl, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 String status="" ;
                 String id ="";
                 JSONObject jsonObject = null;
                 try {
                     jsonObject = new JSONObject(response);
                     status= String.valueOf(jsonObject.get("status"));
                     id= String.valueOf(jsonObject.get("id")) ;
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
                 if (status.equals("1"))
                 {
                     Intent intent =new Intent(MainActivity.this ,HomePage.class);
                     intent.putExtra("id" ,id);
                     SharedPreferences myprefs= getSharedPreferences("user", Context.MODE_PRIVATE);
                     myprefs.edit().putString("user_id", id).commit();
                     startActivity(intent);
                 }

                 else
                 {
                     Toast.makeText(getApplicationContext(),"Invalid Email Or Password" ,Toast.LENGTH_LONG).show();

                 }

             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {

             }
         })
         {
             @Override
             public String getBodyContentType() {
                 return "application/x-www-form-urlencoded; charset=UTF-8";
             }

             @Override
             protected Map<String, String> getParams() throws AuthFailureError {
                 Map  <String ,String >params =new HashMap<String, String>();
                 params.put("email" ,emailText);
                 params.put("pass",passText);
                 return  params ;
             }
         };
         RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
         requestQueue.add(stringRequest);
     }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=(EditText)findViewById(R.id.emailButton);
        pass=(EditText)findViewById(R.id.passButton);
        ip=(EditText)findViewById(R.id.ip);
        signup=(TextView) findViewById(R.id.link_signup);
        login=(Button)findViewById(R.id.logButton);
        testlogin =(TextView) findViewById(R.id.TestlogButton);

        signup.setOnClickListener(new RegisterLitenter());
        testlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this ,HomePage.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }
}
