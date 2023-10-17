package com.exemple.sychronizedclock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private TextView message;
    private Button pauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link var to textview widget
        message = (TextView)findViewById(R.id.textView);


        //Create Button with a click function
        //Will be pause button, right now just message.
        pauseButton = (Button) findViewById(R.id.button);
        pauseButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                message.setText("Clicked");
            }
        });

    }


}