package com.android_developer.jaipal.sim;

/**
 * Created by hp on 2018-03-28.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 200;
    private SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //new PrefetchData().execute();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i;
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                if(sharedpreferences.getBoolean("loggedIn", false)) {
                    i = new Intent( SplashScreen.this, MainActivity.class );
                }
                else{
                    i = new Intent( SplashScreen.this, LoginActivity.class );
                }
                startActivity( i );
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    private class PrefetchData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            if (internetConnectionCheck(SplashScreen.this)) {
//                Toast.makeText(getApplicationContext(), "Internet Connection is available. Abhijeet !!!", 4000).show();
                //code for reteriving pending Actions
                Toast.makeText(getApplicationContext(), "Internet Connected", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute( aVoid );
            Intent i = new Intent( SplashScreen.this, MainActivity.class);
            startActivity(i);

            // close this activity
            finish();
        }
        public boolean internetConnectionCheck(Activity CurrentActivity) {
            Boolean Connected = false;
            ConnectivityManager connectivity = (ConnectivityManager) CurrentActivity.getApplicationContext()
                    .getSystemService( Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        Log.e("My Network is: ", "Connected ");
                        Connected = true;
                    } else {}

            } else {
                Log.e("My Network is: ", "Not Connected");
                Toast.makeText(CurrentActivity.getApplicationContext(),"Please Check Your internet connection",Toast.LENGTH_LONG).show();
                Connected = false;
            }
            return Connected;

        }
    }

}
