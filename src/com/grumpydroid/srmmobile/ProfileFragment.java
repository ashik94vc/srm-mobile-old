package com.grumpydroid.srmmobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Created by Optimus on 10/15/13.
 */
public class ProfileFragment extends SherlockFragment {

    String user,pass,req;
    Activity activity;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        Bundle bundle = getActivity().getIntent().getExtras();
        user = bundle.getString("user");
        pass = bundle.getString("pass");
        req = "2";
        return inflater.inflate(R.layout.profile,container,false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        activity = getSherlockActivity();
        new ProfileFetch().execute();
    }

    private class ProfileFetch extends AsyncTask<Void,Void,Void>
    {

        ProgressDialog progressDialog;
        Map<String,String> map;
        protected void onPreExecute()
        {
            progressDialog = new ProgressDialog(getSherlockActivity());
            progressDialog.setMessage("Loading Results....");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void...v) {
            Log.d("Json", "This executing");
            try
            {
                Log.d("srm","this one doin");
                ProfileParser profileParser = new ProfileParser(activity.getApplicationContext());
                map = profileParser.parse(user,pass);
            }
            catch (final Exception e)
            {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().finish();
                        Log.d("srm","Error Occured "+e.toString());
                    }
                });
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void v)
        {
            super.onPostExecute(v);
            progressDialog.dismiss();

               RoundedImageView imageView = (RoundedImageView) activity.findViewById(R.id.photo);
               File image = new File(activity.getFilesDir(),user+".png");
               Log.d("srm",image.toString());
               if(image.exists())
               try
               {
                   FileInputStream fileInputStream = new FileInputStream(image);
                   Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                   imageView.setImageBitmap(bitmap);
               }
               catch(FileNotFoundException e)
               {
                   e.printStackTrace();
               }
               TextView textView1 = (TextView) activity.findViewById(R.id.name);
               TextView textView2 = (TextView) activity.findViewById(R.id.fname);
               TextView textView3 = (TextView) activity.findViewById(R.id.dept);
               TextView textView4 = (TextView) activity.findViewById(R.id.branch);
               TextView textView5 = (TextView) activity.findViewById(R.id.addr);
               TextView textView6 = (TextView) activity.findViewById(R.id.email);
               TextView textView7 = (TextView) activity.findViewById(R.id.dob);
               TextView textView8 = (TextView) activity.findViewById(R.id.expire);
               try
               {
               textView1.setText(map.get("name"));
               textView2.setText(map.get("regno"));
               textView3.setText(map.get("campus"));
               textView4.setText(map.get("branch"));
               textView5.setText(map.get("address"));
               textView6.setText(map.get("email"));
               textView7.setText(map.get("dob"));
               textView8.setText(map.get("expiry"));
               }
               catch(NullPointerException e)
               {
                   e.printStackTrace();
               }

        }
    }
}
