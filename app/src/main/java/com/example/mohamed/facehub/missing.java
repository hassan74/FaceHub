package com.example.mohamed.facehub;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mohamed.facehub.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class missing extends AppCompatActivity {

    private static final int TAKE_PICTURE = 1;
    private ImageView img;
    private Button reportMissing ;
    Bitmap bp;
    String imgStr;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing);
        img = (ImageView)findViewById(R.id.img);
        reportMissing=(Button)findViewById(R.id.submitMissing);
        reportMissing.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                     bp = (Bitmap) data.getExtras().get("data");
                    imgStr=BitMapToString(bp);
                    byte [] encodeByte=Base64.decode(imgStr,Base64.DEFAULT);
                    Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                    img.setImageBitmap(bitmap);

                }
        }
    }


void report()
{

    String serviceUrl=GetURL.url+"missing";
    SharedPreferences myprefs= getSharedPreferences("user", MODE_WORLD_READABLE);
    user_id= myprefs.getString("user_id", null);

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
                Toast.makeText(missing.this,status,Toast.LENGTH_LONG).show();

            }
            else
            {
                Toast.makeText(missing.this,"problem",Toast.LENGTH_LONG).show();

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
            return  params ;
        }
    };
    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
    requestQueue.add(stringRequest);

}


    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
