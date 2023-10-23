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
import java.util.Date;
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

        // Runs a check on whether there are internet access or not at startup
        connectivityManager.registerDefaultNetworkCallback(internetChecker);


        // Create Mediaplayers to play soundeffects for the checkButtonutton
        // These sounds are locally accessed in the /res/raw folder
        MediaPlayer happySound = MediaPlayer.create(this, R.raw.sound_happy);
        MediaPlayer sadSound = MediaPlayer.create(this, R.raw.sound_sad);


        //Matches the textView variables with the right textView Widgets
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);


        // Creates a button which displays a short message and sound
        // communicating whether or not internet is accessible.
        checkButton = (Button) findViewById(R.id.button);
        checkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                connectivityManager.registerDefaultNetworkCallback(internetChecker);
                if (InternetBoolean.connectedTo == false) {

                    Toast.makeText(getApplicationContext(), "No Internet Access", Toast.LENGTH_LONG).show();
                    sadSound.start();
                } else {
                    Toast.makeText(getApplicationContext(), "Internet Access", Toast.LENGTH_LONG).show();
                    happySound.start();
                }
            }
        });

        /*
        A Thread that keeps time updated.
        Will check every second for changes.
        Switches between System time and NTP-time depending on
        the availability of internet connection.
        */
        Thread timeThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {

                        Thread.sleep(1000);

                        // Makes sure the UI objects (textview) access the UI-thread correctly
                        runOnUiThread(() -> {
                           /*
                           The SNTPClient uses "time.google.com" as default host.
                           The timezone used may shift depending on system settings.
                           If the timezone is set to "use location" the one stated below will be used.
                           */
                            SNTPClient.getDate(TimeZone.getTimeZone("America/New_York"), new SNTPClient.Listener() {
                                @Override
                                public void onTimeResponse(String rawDate, Date date, Exception ex) {
                                    /*
                                    If "date" is returned as null, internet is inaccessible,
                                    or the NTPserver can't be reached.
                                    Sets System time in textview and "SystemTime" message in textView2
                                    */
                                    if (date == null) {
                                        long systemTimeNow = System.currentTimeMillis();
                                        SimpleDateFormat systemTimeFormat = new SimpleDateFormat("kk:mm:ss");
                                        String timeString = systemTimeFormat.format(systemTimeNow);
                                        textView.setText(timeString);
                                        textView2.setText("System Time :");

                                    } else {
                                        /*
                                        If internet is accessible this will run.
                                        Sets the time to NtpTime, as well as changes the time-type message.
                                        */
                                        SimpleDateFormat NTPtimeFormat = new SimpleDateFormat("kk:mm:ss");
                                        String ntimeString = NTPtimeFormat.format(date);
                                        textView.setText(ntimeString);
                                        textView2.setText("NTP Time :");
                                    }
                                }
                            });


                        });
                    }
                } catch (InterruptedException e) {
                    System.out.println("InterruptedException in Thread");
                }
            }

        };


        // Starts up the Thread "timeThread"
        timeThread.start();


    }
}
