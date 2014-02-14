package com.grumpydroid.srmmobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * Created by Optimus on 10/11/13.
 */
public class AttendanceFragment extends SherlockListFragment {

    PullToRefreshListView pullToRefreshListView;
    Attendance attendance;
    String user,pass,req;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
     Bundle bundle = getActivity().getIntent().getExtras();
     user = bundle.getString("user");
     pass = bundle.getString("pass");
     req = "1";
     return inflater.inflate(R.layout.attendance_layout,container,false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getSherlockActivity();
        new AttendanceFetch().execute();

        pullToRefreshListView = (PullToRefreshListView) activity.findViewById(R.id.listView);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new AttendanceRefresh().execute();
            }
        });
    }
    private class AttendanceFetch extends AsyncTask<Void,Void,Void>
    {

        ProgressDialog progressDialog;
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
                AttendanceParse attendanceParse = new AttendanceParse(getActivity().getApplicationContext());
                attendance = attendanceParse.parse(user,pass,false);
            }
            catch (final Exception e)
            {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().finish();
                        Log.d("srm","Erron Occured "+e.toString());
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
            TextView textView2 = (TextView) getSherlockActivity().findViewById(R.id.textView1);
            TextView textView = (TextView) getSherlockActivity().findViewById(R.id.textView);
            textView.setText("Total");
            try
            {
                List<Attendance.Subjects> mList = attendance.mSubjects;
                textView2.setText(String.valueOf(attendance.getTotal()));
                ListAdapter listAdapter = new ListAdapter(mList,getSherlockActivity());
                setListAdapter(listAdapter);
            }
            catch(NullPointerException e)
            {
                Toast.makeText(getSherlockActivity(), "An Error Occured Please retry", Toast.LENGTH_SHORT).show();
                Util util = new Util();
                util.Logout(getActivity());
                getActivity().finish();
            }

        }
    }
    private class AttendanceRefresh extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void...v) {
            Log.d("Json","This executing");
            try
            {
                Log.d("srm","this one doin");
                AttendanceParse attendanceParse = new AttendanceParse(getSherlockActivity().getApplicationContext());
                attendance = attendanceParse.parse(user,pass,true);
            }
            catch (final Exception e)
            {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().finish();
                        Log.d("srm","Erron Occured "+e.toString());
                    }
                });
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void v)
        {

            super.onPostExecute(v);
            TextView textView2 = (TextView) getSherlockActivity().findViewById(R.id.textView1);
            TextView textView = (TextView) getSherlockActivity().findViewById(R.id.textView);
            textView.setText("Total");
            try
            {
                List<Attendance.Subjects> mList = attendance.mSubjects;
                textView2.setText(String.valueOf(attendance.getTotal()));
                ListAdapter listAdapter = new ListAdapter(mList,getActivity());
                setListAdapter(listAdapter);
                pullToRefreshListView.onRefreshComplete();
            }
            catch(NullPointerException e)
            {
                Toast.makeText(getActivity(),"An Error Occured Please retry",Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }

        }
    }

}
