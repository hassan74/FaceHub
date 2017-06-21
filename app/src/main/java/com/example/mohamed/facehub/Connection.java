package com.example.mohamed.facehub;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Connection {
    public static String connect (String serviceUrl ,String urlParameters ,String methodType ,String contentType) throws IOException
    {
        URL url;
        int responseCode=44;
        try {
            url = new URL(serviceUrl);
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod(methodType);
            connection.setConnectTimeout(600000); // 60 Seconds
            connection.setReadTimeout(600000); // 60 Seconds
            connection.setRequestProperty("Content-Type", contentType);
            java.io.OutputStreamWriter writer =new java.io.OutputStreamWriter(connection.getOutputStream());
            writer.write(urlParameters);
            writer.flush();
            writer.close();
             responseCode = connection.getResponseCode();
            String line, retJson = "";
            java.io.BufferedReader reader=new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                retJson += line;
            }

            return retJson;

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return Integer.toHexString( responseCode) ;
    }
}
