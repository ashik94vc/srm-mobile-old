package com.grumpydroid.srmmobile;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ashik on 9/1/13.
 */
public class ListAdapter extends BaseAdapter {

    List<Attendance.Subjects> arrayList;
    Context mContext;
    public ListAdapter(List<Attendance.Subjects> arrayList , Context context)
    {
        this.mContext = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View mView = view;
        if(mView==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = layoutInflater.inflate(R.layout.list_main,null);
        }

        Attendance.Subjects mSubjects = arrayList.get(i);
        TextView subject = (TextView) mView.findViewById(R.id.subject);
        TextView netattn = (TextView) mView.findViewById(R.id.netperc);
        subject.setText(mSubjects.subj);
        netattn.setText(String.valueOf(mSubjects.total));
        return mView;
    }
}
