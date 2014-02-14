package com.grumpydroid.srmmobile;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashik on 9/1/13.
 */
public class JsonParse {

    InputStream inputStream = null;
    static String json = "";
    static JSONObject jsonObject = null;
    public String getJson(String url,String req,String user,String pass)
    {


        try{
            HttpParams params = new BasicHttpParams();
            int connectionTimeOut = 5000;
            int socketTimeOut = 5000;
            HttpConnectionParams.setConnectionTimeout(params,connectionTimeOut);
            HttpConnectionParams.setSoTimeout(params,socketTimeOut);
            DefaultHttpClient http = new DefaultHttpClient(params);
            HttpPost httpGet = new HttpPost(url);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
             nameValuePairs.add(new BasicNameValuePair("req",req));
            nameValuePairs.add(new BasicNameValuePair("apikey","ffd0236de8ee8222ddb43c2e5076e844"));
            nameValuePairs.add(new BasicNameValuePair("pass",pass));
            nameValuePairs.add(new BasicNameValuePair("user",user));
            httpGet.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse httpResponse = http.execute(httpGet);
            if(httpResponse!=null)
            {
            Log.d("srm","Response:"+httpResponse.getStatusLine().getStatusCode());
            HttpEntity httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();
            }
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (SocketTimeoutException e)
        {
            e.printStackTrace();
        }
        catch (ConnectTimeoutException e)
        {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(inputStream!=null)
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null)
                sb.append(line+ "\n");
                inputStream.close();
                json = sb.toString();

        } catch (Exception e) {
            Log.e("srm", "Error converting result " + e.toString());
        }


        String[] strings = json.split("<script");
        json = strings[0];

        return json;
    }
}
