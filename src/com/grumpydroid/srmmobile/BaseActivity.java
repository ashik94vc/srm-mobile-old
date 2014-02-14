package com.grumpydroid.srmmobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


/**
 * Created by Ashik on 9/1/13.
 */
public class BaseActivity extends SlidingFragmentActivity {
    Context context=this;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_base);
        setBehindContentView(R.layout.layout_navigation);
        AttendanceFragment attendanceFragment = new AttendanceFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,attendanceFragment).commit();
        setSlidingActionBarEnabled(true);
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setSelectorEnabled(true);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setShadowWidth(10);
        slidingMenu.setFadeDegree(0.5f);
        slidingMenu.setBehindOffset(300);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        String[] listArray = getResources().getStringArray(R.array.menu_list);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,R.layout.list_navigation,listArray);
        ListView mView = (ListView) findViewById(R.id.menulist1);
        mView.setAdapter(mAdapter);
        mView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i)
                {
                    case 0:
                        Fragment attendanceFragment = new AttendanceFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,attendanceFragment).commit();
                        break;
                    case 1:

                        Fragment testFragment = new TestFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,testFragment).commit();
                        break;
                    case 2:
                        Fragment profileFragment = new ProfileFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,profileFragment).commit();
                        getSlidingMenu().showContent();
                        break;
                    case 3:
                        AlertDialog.Builder builder= new AlertDialog.Builder(new ContextThemeWrapper(context,R.style.dialog));
                        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View mView = layoutInflater.inflate(R.layout.dialog, null);
                        builder.setView(mView);
                        builder.setIcon(R.drawable.ic_action_about);
                        builder.setTitle("About");
                        builder.setNeutralButton("Okay",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.create();
                        builder.show();
                        ImageView fb = (ImageView) mView.findViewById(R.id.imagebutton);
                        fb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/405612459539921")));
                                } catch (Exception e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://touch.facebook.com/grumpydroidinc")));
                                }
                            }
                        });
                        break;
                    case 4:
                        Util util = new Util();
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                        editor.clear();
                        editor.commit();
                        if(util.deleteCache(context.getCacheDir()))
                        {
                            Toast.makeText(getApplicationContext(),"Logged out",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context,LoginActivity.class);
                            startActivity(intent);
                        }
                        break;
                }
            }
        });
    }

}