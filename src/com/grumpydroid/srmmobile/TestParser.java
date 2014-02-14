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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Optimus on 10/12/13.
 */
public class TestParser {
    Context mContext;
    Boolean exists;
    public TestParser(Context context)
    {
        this.mContext = context;
    }

    public TestMarks testParser(String user,String pass,boolean refresh)
    {
        File cFile = new File(mContext.getCacheDir(),"tests.json");
        Log.d("srm","File checking");
        if(!cFile.exists()||refresh)
        {
            Log.d("srm","File not exists");
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
        TestMarks testMarks = new TestMarks();
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
                        JsonFactory jsonFactory = new JsonFactory();
                        JsonParser jParser = jsonFactory.createParser(objectNode.toString());
                        TestMarks.Marks marks = objectMapper.readValue(jParser,TestMarks.Marks.class);
                        if(marks.Subject == "" && marks.Marks =="" && marks.code!="")
                        {
                            marks.Subject =  marks.code;
                            marks.code = "";
                        }
                        testMarks.marksList.add(marks);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            else
            {
                Toast.makeText(mContext, rootNode.get("error").asText(), Toast.LENGTH_SHORT).show();
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
        return  testMarks;
    }
    void onRefresh(File cFile,String user,String pass)
    {
        JsonParse jParse = new JsonParse();
        Log.d("srm","Downloading data");
        String Json = jParse.getJson("http://api2.grumpydroid.org/evarsity.php","2",user,pass);
        Log.d("srm1", Json);
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
