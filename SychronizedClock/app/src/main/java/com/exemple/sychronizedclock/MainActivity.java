package com.exemple.sychronizedclock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private TextView message;
    private Button pauseButton;
    private TextView textView;
    private TextView textView2;
    private static Date NTPTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //NTPTime retriever
       /* SNTPClient.getDate(TimeZone.getTimeZone("Asia/Colombo"), (rawDate, date, ex) -> {

        });
        SNTPClient.getDate(TimeZone.getTimeZone("Asia/Colombo"), (rawDate, date, ex) -> {
            textView2 = (TextView) findViewById(R.id.textView2);
            SimpleDateFormat NTPtimeFormat = new SimpleDateFormat("kk:mm:ss");
            String ntimeString = NTPtimeFormat.format(date);
            textView2.setText(ntimeString);
        });*/
        // A short popup message, can be used for internet connection info
        Toast.makeText(getApplicationContext(),"Hello App",Toast.LENGTH_SHORT).show();


        /*SimpleDateFormat timeStringFormat = new SimpleDateFormat("kk:mm:ss");
        String NTPString = timeStringFormat.format(getNTPTime());
        textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setText(NTPString);*/
       /* SNTPClient.getDate(TimeZone.getTimeZone("Asia/Colombo"), (rawDate, date, ex) -> {

        });*/

        // A Thread that keeps the system time updated.
            Thread t = new Thread(){
                @Override
                public void run() {
                    try {
                            while(!isInterrupted()) {
                                Thread.sleep(1000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //if no internet
                                        //set SystemTime in textview
                                        textView = (TextView) findViewById(R.id.textView);
                                        long systemTimeNow = System.currentTimeMillis();
                                        SimpleDateFormat timeStringFormat = new SimpleDateFormat("kk:mm:ss");
                                        String timeString = timeStringFormat.format(systemTimeNow);
                                        textView.setText(timeString);

                                        //else internet
                                        // set NtpTime
                                       SNTPClient.getDate(TimeZone.getTimeZone("Asia/Colombo"), (rawDate, date, ex) -> {
                                            Calendar.getInstance().setTimeZone(TimeZone.getTimeZone("Asia/Colombo"));
                                            textView2 = (TextView) findViewById(R.id.textView2);
                                            SimpleDateFormat timeFormat = new SimpleDateFormat("kk:mm:ss");
                                            String ntimeString = timeFormat.format(date);
                                            textView2.setText(ntimeString);
                                        });

                                    }

                                });
                            }
                    } catch(InterruptedException e){
                }
            }

            };
        Thread t2 = new Thread(){
            @Override
            public void run() {
                try {
                    while(!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //if no internet
                                    //set SystemTime in textview
                                    SNTPClient.getDate(TimeZone.getTimeZone("Asia/Colombo"), (rawDate, date, ex) -> {

                                    });
                                    Calendar c = Calendar.getInstance();
                                    textView2 = (TextView) findViewById(R.id.textView2);
                                    SimpleDateFormat timeStringFormat = new SimpleDateFormat("kk:mm:ss");
                                    String ntimeString = timeStringFormat.format(c);
                                    textView2.setText(ntimeString);

                                    //else internet
                                    // set NtpTime

                                } catch (Exception e){
                                System.out.println("ex caught");
                                }
                            }
                        });
                    }
                } catch(InterruptedException e){
                }
            }

        };


            t.start();
            //t2.start();
        //Calendar.getInstance().getTimeZone();


        //Create Button with a click function
        //Will be pause button, right now just a message.
        pauseButton = (Button) findViewById(R.id.button);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String currentTime = DateFormat.getTimeInstance().format(new Date());
                textView = (TextView) findViewById(R.id.textView);
                textView.setText(currentTime);
            }
        });

    }

   /* public static Date getNTPTime() {
        NTPUDPClient timeClient = new NTPUDPClient();
        timeClient.setDefaultTimeout(4000);
        try {

            InetAddress inetAddress = InetAddress.getByName("time.google.com");
            TimeInfo timeInfo = timeClient.getTime(inetAddress);
            long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
            NTPTime = new Date(returnTime);
            return NTPTime;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }*/


}
// Kör en runnable för konstant uppdatering.
//Lägg till en check för internet access.
//Lägg till fält för att säga