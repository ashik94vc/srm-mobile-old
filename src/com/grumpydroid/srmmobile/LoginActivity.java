package com.grumpydroid.srmmobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

public class LoginActivity extends SherlockActivity {

    String user = "" , pass ="";
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(sharedPreferences.getBoolean("logged",false))
        {
            Intent mIntent = new Intent(this,BaseActivity.class);
            mIntent.putExtra("user",sharedPreferences.getString("user",""));
            mIntent.putExtra("pass",sharedPreferences.getString("pass",""));
            startActivity(mIntent);
        }
        setContentView(R.layout.activity_main);
        final EditText editText = (EditText) findViewById(R.id.userId);
        final EditText editText1 = (EditText) findViewById(R.id.passId);
        Button button = (Button) findViewById(R.id.LoginButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = editText.getText().toString();
                pass = editText1.getText().toString();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("user",user);
                editor.putString("pass",pass);
                editor.putBoolean("logged",true);
                editor.commit();
                Intent intent = new Intent(context,BaseActivity.class);
                if(!isOnline())
                {
                    Toast.makeText(context,"Connection does not exist",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    intent.putExtra("user",user);
                    intent.putExtra("pass",pass);
                    Log.d("srm",pass);
                    startActivity(intent);
                }
            }
        });

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        Boolean status = cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();

            return status;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                AlertDialog.Builder builder= new AlertDialog.Builder(new ContextThemeWrapper(context,R.style.dialog));
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 builder.setView(layoutInflater.inflate(R.layout.dialog,null));
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
                break;


            default:
                break;
        }

        return true;
    }

}
