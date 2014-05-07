package com.grumpydroid.srmmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/* Created by Ashik on 9/1/13.
*/
public class AttendanceParse {

    Context mContext;
    Boolean exists;
    public AttendanceParse(Context context)
    {
        this.mContext = context;
    }

    public Attendance parse(String user,String pass,boolean refresh)
    {
    File cFile = new File(mContext.getCacheDir(),"attendance.json");
    if(!cFile.exists()||refresh)
    {
     onRefresh(cFile,user,pass);
    }
    String json = "";
    try{
          json = readFile(cFile);
        } catch (IOException e) {
            e.printStackTrace();
      }
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    Attendance attendance = new Attendance();
    try
    {
    JsonNode rootNode = objectMapper.readTree(json);
    JsonNode status   = rootNode.get("status");
    if(status.asText()=="true")
    {
        JsonNode arrayNode = rootNode.get("rows");
        for(JsonNode objectNode : arrayNode)
        {
            String str="";
               try {
                   JSONObject jsonObject = new JSONObject(objectNode.toString());
                   Attendance.Subjects subjects = objectMapper.readValue(jsonObject.toString(),Attendance.Subjects.class);
                   
                   attendance.mSubjects.add(subjects);
                  }
               catch (Exception e)
                 {
                   e.printStackTrace();
                 }
        }
        JsonNode totalNode = rootNode.path("total");
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jParser = jsonFactory.createParser(totalNode.toString());
        Attendance.Total total = objectMapper.readValue(jParser,Attendance.Total.class);
        attendance.total = total;
    }
    else
    {
        Toast.makeText(mContext,rootNode.get("error").asText(),Toast.LENGTH_SHORT).show();
        Util util = new Util();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext()).edit();
        editor.clear();
        editor.commit();
        if(util.deleteCache(mContext.getCacheDir()))
        {
            Toast.makeText(mContext.getApplicationContext(),"Logged out",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext,LoginActivity.class);
            mContext.startActivity(intent);
        }
    }
    }catch (IOException e)
    {
        e.printStackTrace();
    }
        return  attendance;
    }
    void onRefresh(File cFile,String user,String pass)
    {
        JsonParse jParse = new JsonParse();
        String Json = jParse.getJson("1",user,pass);
        Log.d("srm1",Json);
        if(Json!=null)
        try {
            FileOutputStream outputStream = new FileOutputStream(cFile);
            if(!cFile.exists())
            {
                if(cFile.createNewFile())
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
