package com.example.ghostiny_singledevice;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button  instructionButton,settingButton,singleButton,multiButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO: 25/02/2019

        instructionButton=(Button)findViewById(R.id.icon_home_instruction);
        settingButton=(Button)findViewById(R.id.icon_home_setting);
        singleButton=(Button)findViewById(R.id.icon_home_SingleGame);
        multiButton=(Button)findViewById(R.id.icon_home_multiplayer);

        instructionButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,InstructionActivity.class);
                startActivity(intent);
            }
        });


       settingButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });

        singleButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this, SingleNumberActivity.class);
                startActivity(intent);
            }
        });

        multiButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,MultiplayerActivity.class);
                startActivity(intent);
            }
        });

    }

}
