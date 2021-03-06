package com.example.ghostiny_singledevice.single;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ghostiny_singledevice.R;

public class GameContinueActivity extends AppCompatActivity {

    private Button continuebutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_continue);

        continuebutton = (Button)findViewById(R.id.button_continue);

        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =  new Intent(GameContinueActivity.this, SingleNumberActivity.class );
                startActivity(intent);
            }
        });

        AssetManager mgr=getAssets();        //設置字體
        Typeface typeface=Typeface.createFromAsset(mgr,"font/TM.ttf");
        continuebutton.setTypeface(typeface);
    }

}
