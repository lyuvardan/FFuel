package com.example.lyudvigv.ffuel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by LyudvigV on 12/15/2017.
 */

public class RequestManager {

    public static String post(String url, Map<String,String> headers, Object bodyParams )
    {
        InputStream inputStream = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        String jsonParametersToPost = bodyParams.toString();
        for (int i = 0; i< headers.size(); i++)
        {
            httpPost.setHeader(headers.keySet().toArray()[i].toString(),headers.values().toArray()[i].toString());
        }
        try {
            httpPost.setEntity(new StringEntity(jsonParametersToPost));

            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
        }catch(Exception e){
            e.getMessage();
        }
        return convertStreamToString(inputStream);
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


}
