package com.grumpydroid.srmmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Optimus on 10/7/13.
 */
public class Util {

    Context context;
    public boolean Util(Context context)
    {
        return  deleteCache(context.getCacheDir());
    }
    public boolean deleteCache(File files)
    {
        if(files!=null && files.isDirectory())
        {
            String[] childDir = files.list();
            for(String file:childDir)
            {
                boolean flag = deleteCache(new File(files,file));
                if(!flag)
                    return false;
            }
        }
        return files.delete();
    }
    public void Logout(Context context)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
        if(deleteCache(context.getCacheDir()))
        {
            Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context,LoginActivity.class);
            context.startActivity(intent);
        }
    }

}
