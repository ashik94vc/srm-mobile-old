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
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * Created by Optimus on 10/13/13.
 */
public class TestFragment extends SherlockListFragment {
    PullToRefreshListView pullToRefreshListView;
    TestMarks testMarks;
    String user,pass,req;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        Bundle bundle = getActivity().getIntent().getExtras();
        user = bundle.getString("user");
        pass = bundle.getString("pass");
        req = "2";
        return inflater.inflate(R.layout.tests_layout,container,false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getSherlockActivity();
        new TestMarksFetch().execute();

        pullToRefreshListView = (PullToRefreshListView) activity.findViewById(R.id.listView2);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new TestMarksRefresh().execute();
            }
        });
    }
    private class TestMarksFetch extends AsyncTask<Void,Void,Void>
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
                Log.d("srm","this one doin");
                TestParser testParse = new TestParser(getActivity().getApplicationContext());
                testMarks = testParse.testParser(user,pass,false);
            return null;
        }
        @Override
        protected void onPostExecute(Void v)
        {
            super.onPostExecute(v);
            progressDialog.dismiss();
            try
            {
                List<TestMarks.Marks> mList = testMarks.marksList;
                TestsAdapter testsAdapter = new TestsAdapter(mList,getActivity());
                setListAdapter(testsAdapter);
            }
            catch(NullPointerException e)
            {
                Toast.makeText(getSherlockActivity(), "An Error Occured Please retry", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }

        }
    }
    private class TestMarksRefresh extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void...v) {
            Log.d("Json","This executing");
            try
            {
                Log.d("srm","this one doin");
                TestParser testParse = new TestParser(getActivity().getApplicationContext());
                testMarks = testParse.testParser(user, pass, true);
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
            try
            {
                List<TestMarks.Marks> mList = testMarks.marksList;
                TestsAdapter testsAdapter = new TestsAdapter(mList,getActivity());
                setListAdapter(testsAdapter);
                pullToRefreshListView.onRefreshComplete();
            }
            catch(NullPointerException e)
            {
                Toast.makeText(getActivity(),"An Error Occured Please retry",Toast.LENGTH_SHORT).show();
                Util util = new Util();
                util.Logout(getActivity());
                getActivity().finish();
            }

        }
    }

}
