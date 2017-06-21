package com.example.mohamed.facehub;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mohamed.facehub.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class found extends AppCompatActivity {
    private static final int TAKE_PICTURE = 1;
    private ImageView img;
    private Button submit ;
    private EditText info;
    String imgStr ;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found);
        img = (ImageView)findViewById(R.id.img);
        submit = (Button)findViewById(R.id.submitFound);
        info = (EditText)findViewById(R.id.extrInfo);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report();
            }
        });
    }

    public void takePhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
        //startActivity(intent);
    }
    void report()
    {
        SharedPreferences myprefs= getSharedPreferences("user", MODE_WORLD_READABLE);
        user_id= myprefs.getString("user_id", null);
        String serviceUrl=GetURL.url+"found";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serviceUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String status="" ;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    status= String.valueOf(jsonObject.get("status"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(status.equals("true"))
                {
                    Toast.makeText(found.this,status,Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(found.this,"problem",Toast.LENGTH_LONG).show();

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
                params.put("img" ,imgStr);
                params.put("id",user_id);
                params.put("info", String.valueOf(info.getText()));
                return  params ;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap bp = (Bitmap) data.getExtras().get("data");
                    img.setImageBitmap(bp);
                     imgStr = BitMapToString(bp);
                }
        }
    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
