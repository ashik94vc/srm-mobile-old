package com.grumpydroid.srmmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Optimus on 10/15/13.
 */
public class ProfileParser {
    Context context;
    boolean flag =false;

    public ProfileParser(Context context)
    {
        this.context = context;
    }

    public Map<String,String> parse(String user,String pass)
    {
        Map<String,String> map = new HashMap<String, String>();
        File cFile = new File(context.getCacheDir(),"test.json");
        if(!cFile.exists())
        {
            Log.d("srm","So now it goes to download data");
            downloaddata(user,pass,cFile);
        }
        String json = "";
        try{
            json = readFile(cFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        String[] keys = context.getResources().getStringArray(R.array.profile);
        try
        {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode status   = rootNode.get("status");
            if(status.asText()=="true")
            {
                JsonNode arrayNode = rootNode.get("profile");

                int i=0;
                for(JsonNode objectNode : arrayNode)
                {
                    try
                    {
                        map.put(keys[i],objectNode.asText());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    i++;
                }
                try
                {

                downloadimage(map.get("photo"),user);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(context, rootNode.get("error").asText(), Toast.LENGTH_SHORT).show();
                Util util = new Util();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).edit();
                editor.clear();
                editor.commit();
                if(util.deleteCache(context.getCacheDir()))
                {
                    Toast.makeText(context.getApplicationContext(),"Logged out",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context,LoginActivity.class);
                    context.startActivity(intent);
                }
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }

        return map;
    }
    void downloadimage(String url,String user) throws Exception
    {
        File file = new File(context.getFilesDir(),user+".png");
        if(!file.exists())
        {
        Bitmap bmp = BitmapFactory.decodeStream(new URL(url).openStream());
        bmp.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(file));
        }
    }
    void downloaddata(String user,String pass,File file)
    {
        JsonParse jParse = new JsonParse();
        String Json = jParse.getJson("http://api2.grumpydroid.org/evarsity.php","3",user,pass);
        Log.d("srm1", Json);
        if(Json!=null)
            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                if(!file.exists())
                {
                    if(file.createNewFile())
                        outputStream.write(Json.getBytes());
                }
                else
                {
                    outputStream.write(Json.getBytes());
                }
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    String readFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder builder = new StringBuilder();
        String line = "";
        while ((line=reader.readLine())!=null)
            builder.append(line);
        reader.close();
        return builder.toString();
    }
}
