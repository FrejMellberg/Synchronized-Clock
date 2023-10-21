package com.exemple.sychronizedclock;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private Button checkButton;
    private TextView textView;
    private TextView textView2;
    private ConnectivityManager connectivityManager;
    private InternetChecker internetChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Needed so the onPause and onResume methods can keep track on
        // the connection changes.
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        internetChecker = new InternetChecker();


        // Create Mediaplayers to play soundeffects for the checkButtonutton
        // These sounds are locally accessed in the /res/raw folder
        MediaPlayer happySound = MediaPlayer.create(this, R.raw.sound_happy);
        MediaPlayer sadSound = MediaPlayer.create(this, R.raw.sound_sad);


        //Matches the textView variables with the right textView Widgets
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);


        // Create a button which displays a short message and sound
        // communicating whether or not internet is accessible.
        checkButton = (Button) findViewById(R.id.button);
        checkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(InternetBoolean.connectedTo==false){
                    Toast.makeText(getApplicationContext(),"No Internet Access",Toast.LENGTH_LONG).show();
                    sadSound.start();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Internet Access",Toast.LENGTH_LONG).show();
                    happySound.start();
                }
            }
        });

        /*
        A Thread that keeps time updated.
        Will check every second for changes.
        Switches between SystemTime and NTPTime depending on
        if internet connection is available.
        */
            Thread t = new Thread(){
                @Override
                public void run() {
                    try {
                            while(!isInterrupted()) {

                                Thread.sleep(1000);
                                runOnUiThread(() -> {
                                    if(InternetBoolean.connectedTo==false) {
                                        //If no internet is accessible this will run
                                        //Sets SystemTime in textview and "SystemTime" message in textView2
                                        long systemTimeNow = System.currentTimeMillis();
                                        SimpleDateFormat timeStringFormat = new SimpleDateFormat("kk:mm:ss");
                                        String timeString = timeStringFormat.format(systemTimeNow);
                                        textView.setText(timeString);
                                        textView2.setText("System Time:");
                                    }
                                    else{
                                    /*
                                    If internet is accessible this will run.
                                    Sets the time to NtpTime, as well as changes the internet message.
                                    The SNTPClient uses "time.google.com" as default host.
                                    */
                                   SNTPClient.getDate(TimeZone.getTimeZone("Asia/Colombo"), (rawDate, date, ex) -> {
                                        Calendar.getInstance().setTimeZone(TimeZone.getTimeZone("Asia/Colombo"));
                                        SimpleDateFormat timeFormat = new SimpleDateFormat("kk:mm:ss");
                                        String ntimeString = timeFormat.format(date);
                                        textView.setText(ntimeString);
                                        textView2.setText("NTP Time:");
                                    });}

                                });
                            }
                    } catch(InterruptedException e){
                        System.out.println("InterruptedException in Thread");
                }
            }

            };


            // Starts up the Thread "t"
            t.start();



    }

// InternetChecker methods for keeping track on if the internet connection is on/off
// while the app is running. A change will switch the Global static boolean "connectedTo" to true/false
    @Override
    protected void onResume() {
        super.onResume();
        // Register the NetworkCallback
        connectivityManager.registerDefaultNetworkCallback(internetChecker);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister the NetworkCallback when the activity is paused
        connectivityManager.unregisterNetworkCallback(internetChecker);

    }

}
