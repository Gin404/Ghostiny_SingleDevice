package com.example.ghostiny_singledevice;

import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class GameContinueActivity extends AppCompatActivity {
    Button continuebutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_continue);

        continuebutton = (Button)findViewById(R.id.buttoncontinue);

        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =  new Intent(GameContinueActivity.this,SingleNumberActivity.class );
                startActivity(intent);
            }
        });


    }
}
